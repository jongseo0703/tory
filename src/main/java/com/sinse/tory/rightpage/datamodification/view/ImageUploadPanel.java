package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * ImageUploadPanel
 * - 상품 수정 페이지 내 이미지 업로드를 위한 서브 패널
 * - 현재는 단일 버튼만 포함되어 있으며, 향후 파일 선택 또는 이미지 미리보기 기능을 확장할 수 있음
 */
final class ImageUploadPanel extends JPanel {

	private JButton uploadButton; // 이미지 업로드 버튼 (클릭 시 파일 선택 다이얼로그 예정)

	// 패널 여백 설정 상수
	private static final int WIDTH_MARGIN = 12;
	private static final int HEIGHT_MARGIN = 32;

	/**
	 * 생성자
	 * - 이미지 업로드 버튼을 초기화하고, 수직 정렬 및 여백이 포함된 스타일로 구성
	 */
	ImageUploadPanel() {
		// 버튼 생성 및 초기 텍스트 설정
		uploadButton = new JButton("이미지를 등록해주세요.");

		// 수직 정렬 박스 레이아웃 설정
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		setBackground(Color.gray); // TODO: 나중에 이미지 프리뷰 또는 테마 컬러로 교체 가능

		// 버튼 추가 및 중앙 정렬
		add(uploadButton);
		uploadButton.setAlignmentX(CENTER_ALIGNMENT);

		// 버튼의 가로는 꽉 채우고, 세로는 원래 높이 유지
		int buttonHeight = uploadButton.getPreferredSize().height;
		uploadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
	}

	/**
	 * getUploadButton
	 * - 외부에서 버튼에 이벤트 리스너를 추가할 수 있도록 getter 제공
	 * 
	 * @return 업로드 버튼 객체
	 */
	public JButton getUploadButton() {
		return uploadButton;
	}
}
