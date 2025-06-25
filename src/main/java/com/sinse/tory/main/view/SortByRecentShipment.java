package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

//시간 관련 패키지 임포트
import java.time.ZoneId;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.InventoryLog;

public class SortByRecentShipment implements SortStrategy {
	
	InventoryLog.ChangeType type;
	boolean ascending; //true 면 오름차순, false 면 내림차순
	Map<Integer, String> productIdToCategory;
	Map<String, Long> categoryToRecentShipmentTimestamp; //카테고리별 최근 출고 시간
	
	public SortByRecentShipment(List<Product> products, List<InventoryLog> inventoryLogs, InventoryLog.ChangeType type, boolean ascending) {
		this.type = type;
		this.ascending = ascending;
		productIdToCategory = new HashMap<>();
		
		for(Product product : products) {
			productIdToCategory.put(product.getProductId(), product.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName());
		}
		
		categoryToRecentShipmentTimestamp = new HashMap<>();
		
		for(InventoryLog log : inventoryLogs) {
			if(log.getChangeType() == type) {
				String category = productIdToCategory.get(log.getProductDetail().getProduct().getProductId());
				long changedAt = log.getChangedAt().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				categoryToRecentShipmentTimestamp.merge(category, changedAt, Math::max);
			}
		}
	}
	
	public List<String> sort(List<String> categoryOrder, List<Product> products) {
		return categoryOrder.stream()
				.sorted((c1, c2) -> {
						long time1 = categoryToRecentShipmentTimestamp.getOrDefault(c2, 0L);
						long time2 = categoryToRecentShipmentTimestamp.getOrDefault(c1, 0L);
						return ascending ? Long.compare(time1, time2) : Long.compare(2, time1);
				})
				.collect(Collectors.toList());
	}
}
