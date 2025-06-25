package com.sinse.tory.rightpage.datamodification.view;

import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.view.ShowMessage;

/**
 * DataManagementFromTable
 * - 수정 창에서 사용자가 입력한 데이터를 수집 및 검증한 후 DB에 등록하는 기능 담당
 * - 저장 버튼 클릭 시 호출됨
 */
final class DataManagementFromTable {

	private ImageUploadPanel imageUploadPanel; // 이미지 업로드 패널 (현재 사용하지 않음)
	private IdentificationPanel identificationPanel; // 상품 카테고리 및 이름 입력 영역
	private DetailDataTable detailDataTable; // 상품 상세 정보 입력 영역

	/**
	 * 생성자
	 * 
	 * @param imageUploadPanel    이미지 업로드 패널
	 * @param identificationPanel 식별 정보 패널 (카테고리, 이름 등)
	 * @param detailDataTable     상세 정보 테이블 패널
	 */
	public DataManagementFromTable(ImageUploadPanel imageUploadPanel, IdentificationPanel identificationPanel,
			DetailDataTable detailDataTable) {
		this.imageUploadPanel = imageUploadPanel;
		this.identificationPanel = identificationPanel;
		this.detailDataTable = detailDataTable;
	}

	/**
	 * add
	 * - 저장 버튼 클릭 시 호출되는 메서드
	 * - 모든 입력이 완료되었는지 검증한 뒤 DB에 상품 정보를 저장
	 *
	 * @param pageMove 페이지 전환 유틸 (성공 시 목록 페이지로 이동)
	 */
	void add(PageMove pageMove) {
		// 입력 검증: 모든 필수 입력이 완료되었는지 확인
		if (detailDataTable.isInputAll() && identificationPanel.isSelectAll()) {
			// 사용자 입력으로부터 SubCategory, ProductDetail 객체 생성
			SubCategory subCategory = identificationPanel.createSubCategoryFromInputted();
			ProductDetail productDetail = detailDataTable.createProductDetailFromInputted(subCategory,
					identificationPanel.getProductName());

			// 사용자에게 최종 확인 요청
			if (ShowMessage.showConfirm(null, "상품 추가", "상품을 추가 하시겠습니까?") == false) {
				return; // 사용자가 취소한 경우 처리 중단
			}

			// DB에 저장
			RightPageProductDetailDAO.insert(productDetail);

			// 입력 필드 초기화 및 페이지 전환
			identificationPanel.reset();
			detailDataTable.reset();
			pageMove.showPage(0, 1); // 상품 등록 후 목록 페이지로 이동
		} else {
			// 입력 누락 시 경고 메시지 표시
			ShowMessage.showAlert(null, "상품 추가", "상품 정보를 모두 입력해주세요.");
		}
	}
}
