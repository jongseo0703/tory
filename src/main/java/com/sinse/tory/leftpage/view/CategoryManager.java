package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.Color;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.TopCategory;

//카테고리별 상품을 담당하는 클래스
public class CategoryManager {
	//각 상위카테고리별 색상을 매핑하는 categoryColors
	private Map<String, Color> categoryColors;
	//각 상위카테고리 이름들을 가지고 있는 categoryOrder
	private List<String> categoryOrder;
	//각 상위카테고리별 상품 개수를 저장하는 맵
	private Map<String, Integer> productCountPerCategory;
	
	public void init(List<TopCategory> topCategories, List<Product> products) {
		//각 카테고리별 색상을 매핑하는 categoryColors
		categoryColors = new LinkedHashMap<>();
		//각 카테고리 이름들을 가지고 있는 categoryOrder
		categoryOrder = new ArrayList<>();
		productCountPerCategory = new LinkedHashMap<>();
		
		//TopCategoryDAO로 가져온 topCategories에서 TopCategoryName을 가져와서 list에 저장
		for(TopCategory tc : topCategories) {
			String name = tc.getTopCategoryName();
			categoryOrder.add(name);
			productCountPerCategory.put(name, 0);
		}
		
		//각 색상을 가진 배열
		Color[] colors = {
			new Color(255, 99, 71), new Color(60, 179, 113), new Color(65, 105, 225),
			new Color(238, 130, 238), new Color(255, 215, 0), new Color(70, 130, 180),
			new Color(255, 140, 0), new Color(123, 104, 238), new Color(199, 21, 133), 
			new Color(46, 139, 87)
		};
		
		//각 카테고리에 고유 색상 할당
		for(int i = 0; i < categoryOrder.size(); i++) {
			categoryColors.put(categoryOrder.get(i), colors[i % colors.length]);
		}
		
		//상품 목록을 순회하며 각 상품의 상위카테고리별로 개수를 누적
		for(Product p : products) {
			String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
			if(productCountPerCategory.containsKey(topCategoryName)) {
				int current = productCountPerCategory.get(topCategoryName);
				productCountPerCategory.put(topCategoryName, current + 1);
			}
		}
	}
	
	//상품 리스트를 상위카테고리 별로 분류하여 각 카테고리별 상품 리스트를 담은 맵을 반환
	public Map<String, List<Product>> categorizeProducts(List<Product> products) {
		// 1. 카테고리별 상품 리스트 초기화
		Map<String, List<Product>> categoryProductMap = new LinkedHashMap<>();
		for (String category : categoryOrder) {
		    categoryProductMap.put(category, new ArrayList<>());
		}
		// 2. 상품을 카테고리별로 분류
		for (Product p : products) {
		    String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
		    if (categoryProductMap.containsKey(topCategoryName)) {
		        categoryProductMap.get(topCategoryName).add(p);
		    }
		}
		return categoryProductMap;
	}
	
	//상위카테고리 이름을 담은 categoryOrder 리스트를 반환하는 getter 메서드
	public List<String> getCategoryOrder() {
		return categoryOrder;
	}
	
	//상위카테고리별 색상의 매핑을 담은 categoryColors를 반환하는 getter 메서드
	public Map<String, Color> getCategoryColors() {
		return categoryColors;
	}
	
	//상위카테고리별 재고량의 매핑을 담은 productCountPerCategory를 반환하는 getter 메서드
	public Map<String, Integer> getProductCountPerCategory() {
		return productCountPerCategory;
	}

}
