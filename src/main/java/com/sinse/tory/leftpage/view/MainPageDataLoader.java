package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

//메인페이지에 필요한 데이터를 디비로부터 불러오는 클래스
public class MainPageDataLoader {
	
	//TopCategory 리스트를 반환하는 메서드
	public List<TopCategory> loadTopCategories() {
		TopCategoryDAO topcategoryDAO = new TopCategoryDAO();
		return topcategoryDAO.selectAll();
	}
	
	//Product 리스트를 반환하는 메서드
	public List<Product> loadProducts() {
		ProductDAO productDAO = new ProductDAO();
		return productDAO.selectAll();
	}
}
