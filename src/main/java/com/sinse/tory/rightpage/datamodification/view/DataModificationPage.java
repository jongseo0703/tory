package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.rightpage.util.PageMove;

/**
 * DataModificationPage
 * - 오른쪽 페이지 영역에서 마이크 영역을 제외한
 * 상단(Header) + 중앙(Content) UI 전체를 포함하는 클래스
 * - 수정할 상품 정보를 받아 UI에 반영하는 역할 수행
 */
public final class DataModificationPage extends JPanel {
	private Header header; // 상단 헤더 영역 (뒤로가기, 제목 등)
	private Content content; // 입력 필드 및 상세 정보 수정 영역
	private ProductDetail currentModifyingProductDetail; // 현재 수정 중인 상품 상세 정보 객체

	// 여백 설정 상수
	private static final int HORIZONTAL_MARGIN = 16;
	private static final int VERTICAL_MARGIN = 48;
	public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);

	/**
	 * 생성자
	 * - PageMove를 통해 뒤로가기 또는 전환 동작을 처리하며,
	 * Content 영역에는 테이블에서 파생된 수정 폼을 전달
	 */
	public DataModificationPage(PageMove pageMove) {
		content = new Content(); // 중앙 입력 영역 생성
		header = new Header(pageMove, content.createDataManagementFromTable()); // 헤더 구성

		currentModifyingProductDetail = null;

		// 전체 레이아웃 설정: 수직 BoxLayout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(header); // 상단 헤더 추가
		add(Box.createRigidArea(new Dimension(0, 32))); // 헤더와 내용 사이 여백
		add(content); // 본문 콘텐츠 추가
	}

	/**
	 * fillIdentifier
	 * - 상품 식별자 정보(카테고리, 이름 등)를 Content에 전달하여 UI에 표시
	 *
	 * @param topCategoryID 상위 카테고리 ID
	 * @param subCategoryID 하위 카테고리 ID
	 * @param name          상품 이름
	 */
	public void fillIdentifier(int topCategoryID, int subCategoryID, String name) {
		content.fillData(topCategoryID, subCategoryID, name);
	}

	/**
	 * initializeProductDetail
	 * - 수정 대상이 되는 ProductDetail 객체를 초기화하고 ID만 설정
	 *
	 * @param productDetailID 수정 대상 상품 상세 ID
	 */
	public void initializeProductDetail(int productDetailID) {
		currentModifyingProductDetail = new ProductDetail();
		currentModifyingProductDetail.setProductDetailId(productDetailID);

		// 디버깅용 출력
		System.out.println(currentModifyingProductDetail.getProductDetailId());
	}

	/**
	 * onShow
	 * - 페이지가 열릴 때 호출되는 메서드
	 * - 외부에서 전달받은 ProductDetail 객체를 기반으로 화면을 초기화할 수 있음
	 * - 현재는 구현 미완 (향후 데이터 표시 로직 작성 예정)
	 *
	 * @param productDetail 외부에서 전달된 수정 대상 객체
	 */
	public void onShow(ProductDetail productDetail) {
		// TODO: productDetail을 기반으로 content 영역에 값 채우기 구현 예정
		// 예: content.setFields(productDetail)
	}
}