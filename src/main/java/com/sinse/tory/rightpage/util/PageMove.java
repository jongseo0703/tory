package com.sinse.tory.rightpage.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import com.sinse.tory.db.model.ProductDetail;

/**
 * PageMove
 * - setVisible 방식으로 페이지 전환을 처리하는 유틸 클래스
 * - 오른쪽 패널에서 보여줄 JPanel 페이지들을 관리하고, 전환 기능을 제공
 */
public class PageMove {
	public List<JPanel> list = new ArrayList<>(); // 전환 대상이 되는 페이지 컴포넌트 리스트

	/**
	 * showPage(int page1, int page2)
	 * - 특정 인덱스의 두 페이지 중 page1은 보여주고, page2는 숨김 처리
	 * 
	 * @param page1 보여줄 페이지 인덱스
	 * @param page2 숨길 페이지 인덱스
	 */
	public void showPage(int page1, int page2) {
		list.get(page1).setVisible(true);
		list.get(page2).setVisible(false);
	}

	/**
	 * showDataModificationPage(ProductDetail)
	 * - ProductAddPage를 보여주고 초기화
	 * 
	 * @param productDetail 상품 상세 정보 (현재는 사용하지 않음)
	 */
	public void showDataModificationPage(ProductDetail productDetail) {
		list.get(0).setVisible(false);
		list.get(1).setVisible(true);
		// ProductAddPage는 항상 새 상품 등록이므로 별도 초기화 불필요
	}
}
