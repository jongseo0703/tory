package com.sinse.tory.rightpage.datamodification.view;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;

/**
 * Content
 * - 수정 페이지에서 Header를 제외한 메인 콘텐츠 영역을 담당
 * - 이미지 업로드, 상품 식별 정보 입력, 상세 정보 테이블을 포함
 */
public final class Content extends JPanel {

	private JPanel simpleDataPanel; // 좌측: 이미지 업로드 / 우측: 식별자 드롭다운 패널을 담는 컨테이너
	private ImageUploadPanel imageUploadPanel; // 상품 이미지 업로드 패널
	private IdentificationPanel identificationPanel; // 상품 카테고리, 이름 등을 입력받는 식별자 패널

	private DetailDataTable detailDataTable; // 상품 상세 데이터를 입력하는 테이블 패널

	private ProductDetail currentModifyingProductDetail; // 현재 수정 중인 상세 정보 객체 (현재는 사용되지 않음)

	private static final int HORIZONTAL_MARGIN = 12;

	/**
	 * 생성자
	 * - 전체 콘텐츠 레이아웃 및 구성 요소 초기화
	 */
	public Content() {
		// 개별 컴포넌트 생성
		simpleDataPanel = new JPanel();
		imageUploadPanel = new ImageUploadPanel();
		identificationPanel = new IdentificationPanel();
		detailDataTable = new DetailDataTable();

		// 전체 Content 패널에 좌우 여백 설정
		setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));

		// Content 레이아웃: 2행 1열 (위: 간단 정보, 아래: 상세 테이블), 세로 간격 16px
		setLayout(new GridLayout(2, 1, 0, 16));
		add(simpleDataPanel); // 첫 행: 이미지 + 식별 정보
		add(detailDataTable); // 두 번째 행: 상세 데이터 테이블

		// simpleDataPanel 레이아웃: 1행 2열 (좌: 이미지, 우: 식별자), 가로 간격 16px
		simpleDataPanel.setLayout(new GridLayout(1, 2, 16, 0));
		simpleDataPanel.add(imageUploadPanel);
		simpleDataPanel.add(identificationPanel);
	}

	/**
	 * createDataManagementFromTable
	 * - Header에서 저장 버튼 클릭 시 데이터를 수집/저장할 수 있도록
	 * DataManagementFromTable 객체를 생성해 반환
	 *
	 * @return 데이터 저장을 처리할 관리 객체
	 */
	public DataManagementFromTable createDataManagementFromTable() {
		return new DataManagementFromTable(imageUploadPanel, identificationPanel, detailDataTable);
	}

	/**
	 * fillData
	 * - 외부에서 전달된 상품 정보를 기반으로 화면을 채움
	 * - 카테고리/이름은 식별 패널에, 상세 정보는 테이블에 표시
	 *
	 * @param topCategoryID 상위 카테고리 ID
	 * @param subCategoryID 하위 카테고리 ID
	 * @param name          상품 이름
	 */
	void fillData(int topCategoryID, int subCategoryID, String name) {
		// 식별자 입력 필드 채우기
		identificationPanel.insertProductIdentifier(topCategoryID, subCategoryID, name);

		// DB에서 상품 상세 정보 조회 후 첫 번째 항목을 테이블에 채움
		List<ProductDetail> productDetails = RightPageProductDetailDAO.selectProductDetailsByProductName(name);
		detailDataTable.insertDetailData(productDetails.get(0)); // 첫 항목만 채움 (TODO: 멀티 처리 고려 가능)
	}
}