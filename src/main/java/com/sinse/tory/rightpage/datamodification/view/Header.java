package com.sinse.tory.rightpage.datamodification.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.view.ShowMessage;

/**
 * Header
 * - 수정 페이지 상단 헤더 영역
 * - 좌측: 뒤로가기 버튼
 * - 우측: 삭제 버튼, 저장 버튼
 */
public final class Header extends JPanel {

	private JButton backButton; // 뒤로 가기 버튼
	private JPanel rightPanel; // 삭제/저장 버튼을 담는 우측 패널
	private JButton deleteButton; // 삭제 버튼
	private JButton saveButton; // 저장 버튼

	private DataManagementFromTable dataManagementFromTable; // 저장 로직 수행 객체

	// 레이아웃 상수
	private static final int HEADER_HEIGHT = 29; // 전체 헤더 높이
	private static final int MARGIN = 16; // 좌우 마진
	private static final int BUTTON_WIDTH_MARGIN = 24; // 버튼 내부 가로 여백

	/**
	 * Header 생성자
	 *
	 * @param pageMove                페이지 전환 유틸 (뒤로가기 용도)
	 * @param dataManagementFromTable 저장 버튼 클릭 시 동작할 처리 객체
	 */
	public Header(PageMove pageMove, DataManagementFromTable dataManagementFromTable) {
		this.dataManagementFromTable = dataManagementFromTable;

		// 버튼 및 패널 생성
		backButton = new JButton("<");
		rightPanel = new JPanel();
		deleteButton = new JButton("삭제");
		saveButton = new JButton("저장");

		// 헤더 자체의 크기 설정
		setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN)); // 좌우 마진

		// ← 뒤로가기 버튼 설정
		backButton.setBorder(DataModificationPage.EMPTY_BORDER);
		backButton.setPreferredSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		backButton.setMaximumSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 인덱스 0 페이지로 돌아감 (예: ProductShip)
				pageMove.showPage(0, 1);
			}
		});

		// 오른쪽 버튼 패널 설정
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 버튼 수평 정렬
		rightPanel.setBorder(DataModificationPage.EMPTY_BORDER);
		rightPanel.add(deleteButton);
		rightPanel.add(Box.createRigidArea(new Dimension(24, 0))); // 삭제/저장 사이 여백
		rightPanel.add(saveButton);

		// 버튼 크기 통일
		updateButtonSize(deleteButton);
		updateButtonSize(saveButton);

		// 삭제 버튼 클릭 시 확인 팝업
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowMessage.showConfirm(null, "title", "content");
			}
		});

		// 저장 버튼 클릭 시 데이터 저장 처리 위임
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataManagementFromTable.add(pageMove);
			}
		});

		// 좌/우 패널 배치
		add(backButton, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
	}

	/**
	 * updateButtonSize
	 * - 버튼의 높이와 내부 좌우 여백을 동일하게 설정하여 일관된 UI 구성
	 *
	 * @param button 대상 버튼 컴포넌트
	 */
	private void updateButtonSize(JButton button) {
		button.setBorder(BorderFactory.createEmptyBorder(0, BUTTON_WIDTH_MARGIN, 0, BUTTON_WIDTH_MARGIN));
		int buttonPreferredWidth = button.getPreferredSize().width;
		button.setPreferredSize(new Dimension(buttonPreferredWidth, HEADER_HEIGHT));
		button.setMaximumSize(new Dimension(buttonPreferredWidth, HEADER_HEIGHT));
	}
}
