package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

// 컬렉션 관련 패키지 임포트
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

// 시간 관련 패키지 임포트
import java.time.ZoneId;

// 선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.InventoryLog;

/**
 * 카테고리별 최근 입고·출고 시점을 기준으로 정렬해 주는 전략 클래스.
 * <p>
 * - type  : IN / OUT 중 어떤 로그를 대상으로 할지 지정.
 * - ascending : true  ➜ 오래된 순(오름차순)
 *               false ➜ 가장 최근 순(내림차순)
 */
public class SortByRecentShipment implements SortStrategy {

    private final InventoryLog.ChangeType type;
    private final boolean ascending;
    private final Map<String, Long> categoryToTimestamp; // 카테고리 ➜ 가장 최근(또는 가장 오래된) 로그 시각

    public SortByRecentShipment(List<Product> products,
                                List<InventoryLog> inventoryLogs,
                                InventoryLog.ChangeType type,
                                boolean ascending) {
        this.type = type;
        this.ascending = ascending;

        /* 제품 ID ➜ 상위 카테고리명 매핑 */
        Map<Integer, String> productIdToCategory = new HashMap<>();
        for (Product p : products) {
            String topCategoryName = p.getLocation()
                                      .getBrand()
                                      .getSubCategory()
                                      .getTopCategory()
                                      .getTopCategoryName()
                                      .trim();
            productIdToCategory.put(p.getProductId(), topCategoryName);
        }

        /* 카테고리 ➜ (최근) 타임스탬프 */
        categoryToTimestamp = new HashMap<>();
        for (InventoryLog log : inventoryLogs) {
            if (log.getChangeType() != type) continue; // 타입 불일치 건 스킵

            String category = productIdToCategory.get(
                    log.getProductDetail().getProduct().getProductId());
            if (category == null) continue; // 매핑 누락 시 방어

            /*
             * LocalDateTime ↔ Instant ↔ epochMilli
             *  - atStartOfDay() 를 제거해 시/분/초까지 보존합니다.
             */
            long ts = log.getChangedAt()                    // LocalDateTime으로 변환 (00:00:00)
                    .atZone(ZoneId.systemDefault())         // ZonedDateTime
                    .toInstant()
                    .toEpochMilli();


            // 카테고리마다 가장 최근(또는 가장 오래된) 시각만 남김
            categoryToTimestamp.merge(category, ts, ascending ? Math::min    // 오름차순: 최소값 유지
                                                             : Math::max);  // 내림차순: 최대값 유지
        }
    }

    @Override
    public List<String> sort(List<String> categoryOrder, List<Product> products) {
        /* 기본 Comparator: 타임스탬프 비교 */
        Comparator<String> cmp = Comparator.comparingLong(k -> categoryToTimestamp.getOrDefault(k, 0L));
        if (!ascending) {
            cmp = cmp.reversed();
        }
        return categoryOrder.stream()
                             .sorted(cmp)
                             .collect(Collectors.toList());
    }
}