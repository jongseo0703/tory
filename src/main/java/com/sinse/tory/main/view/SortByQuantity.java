package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;

public class SortByQuantity implements SortStrategy {
	public List<String> sort(List<String> categoryOrder, List<Product> products) {
		//카테고리별 재고 합 계산
		Map<String, Integer> quantityMap = new HashMap<>();
		
		for(String topcategoryName : categoryOrder) {
			quantityMap.put(topcategoryName, 0);
		}
		
		for(Product product : products) {
			String topCategory = product.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
			
			int totalQuantity = 0;
			if(product.getProductDetails() != null) {
				for(ProductDetail productDetail : product.getProductDetails()) {
					totalQuantity += productDetail.getProductQuantity();
				}
			}
			
			quantityMap.put(topCategory, quantityMap.getOrDefault(topCategory, 0) + totalQuantity);
		}
		return quantityMap.entrySet()
				.stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}
}
