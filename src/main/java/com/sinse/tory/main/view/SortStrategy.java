package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;

public interface SortStrategy {
	public List<String> sort(List<String> categoryOrder, List<Product> products);
}
