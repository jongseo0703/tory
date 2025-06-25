package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * TableData
 * - 상품 정보 테이블의 한 항목(이름 + 입력 필드)을 나타내는 패널
 * - 좌측: 타이틀 라벨, 우측: 입력 필드 (예: JTextField, JComboBox 등)
 */
final class TableData extends JPanel {

	private JPanel titlePanel; // 라벨을 담는 패널
	private JLabel titleLabel; // 타이틀 텍스트 (예: "브랜드", "사이즈")

	/**
	 * 생성자
	 * 
	 * @param title   라벨에 표시될 제목 텍스트
	 * @param content 사용자 입력을 위한 컴포넌트 (예: JTextField, JComboBox 등)
	 */
	TableData(String title, JComponent content) {
		titlePanel = new JPanel();
		titleLabel = new JLabel(title);

		// 전체 레이아웃: 2열 그리드 (좌: 타이틀, 우: 입력 필드)
		setLayout(new GridLayout(1, 2));
		add(titlePanel); // 좌측: 라벨 영역
		add(content); // 우측: 입력 영역

		// 라벨을 타이틀 패널에 추가
		titlePanel.add(titleLabel);

		// 타이틀 패널 테두리 설정 (밝은 회색 선)
		titlePanel.setBorder(BorderFactory.createLineBorder(new Color(0.8f, 0.8f, 0.8f)));

		// 라벨 정렬 (수평 가운데 정렬 - 효과는 레이아웃에 따라 달라질 수 있음)
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
}
