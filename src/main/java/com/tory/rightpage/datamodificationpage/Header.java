package com.tory.rightpage.datamodificationpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

// 뒤로 가기 버튼, 삭제 버튼 등이 있는 구역
public final class Header extends JPanel
{
	private JButton backButton;
	// 삭제, 저장 버튼이 들어갈 패널
	private JPanel rightPanel;
	private JButton deleteButton;
	private JButton saveButton;
	
	// 헤더 패널의 높이
	private static final int HEADER_HEIGHT = 29;
	// 패널 좌우 마진
	private static final int MARGIN = 16;
	
	
	
	public Header(int parentWidth, int parentMargin)
	{
		backButton = new JButton("<");
		rightPanel = new JPanel();
		deleteButton = new JButton("삭제");
		saveButton = new JButton("저장");

		// 크기 직접 조정 
		setPreferredSize(new Dimension(parentWidth - (2 * parentMargin), HEADER_HEIGHT));
		setMaximumSize(new Dimension(parentWidth - (2 * parentMargin), HEADER_HEIGHT));
		//
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN));
		add(backButton, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		backButton.setBorder(Page.EMPTY_BORDER);
		backButton.setPreferredSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		backButton.setMaximumSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		rightPanel.setBorder(Page.EMPTY_BORDER);
		rightPanel.add(deleteButton);
		// setBorder 함수는 패널의 자식 사이의 거리 뿐만 아니라 테두리 부분까지 영량을 미치기 때문에 별도의 여백을 삽입
		rightPanel.add(Box.createRigidArea(new Dimension(24, 0)));
		rightPanel.add(saveButton);
		
		deleteButton.setBorder(Page.EMPTY_BORDER);
		deleteButton.setPreferredSize(new Dimension(111, HEADER_HEIGHT));
		deleteButton.setMaximumSize(new Dimension(111, HEADER_HEIGHT));
		
		saveButton.setBorder(Page.EMPTY_BORDER);
		saveButton.setPreferredSize(new Dimension(98, HEADER_HEIGHT));
		saveButton.setMaximumSize(new Dimension(98, HEADER_HEIGHT));
	}
}