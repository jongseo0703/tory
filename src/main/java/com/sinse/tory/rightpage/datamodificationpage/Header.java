package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

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
	private static final int BUTTON_WIDTH_MARGIN = 24;
	
	
	
	public Header()
	{
		backButton = new JButton("<");
		rightPanel = new JPanel();
		deleteButton = new JButton("삭제");
		saveButton = new JButton("저장");

		setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN));
		add(backButton, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		backButton.setBorder(DataModificationPage.EMPTY_BORDER);
		backButton.setPreferredSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		backButton.setMaximumSize(new Dimension(HEADER_HEIGHT, HEADER_HEIGHT));
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		rightPanel.setBorder(DataModificationPage.EMPTY_BORDER);
		rightPanel.add(deleteButton);
		// setBorder 함수는 패널의 자식 사이의 거리 뿐만 아니라 테두리 부분까지 영량을 미치기 때문에 별도의 여백을 삽입
		rightPanel.add(Box.createRigidArea(new Dimension(24, 0)));
		rightPanel.add(saveButton);
		
		updateButtonSize(deleteButton);
		updateButtonSize(saveButton);
	}
	
	
	
	private void updateButtonSize(JButton button)
	{
		button.setBorder(BorderFactory.createEmptyBorder(0, BUTTON_WIDTH_MARGIN, 0, BUTTON_WIDTH_MARGIN));
		int buttonPreferredWidth = button.getPreferredSize().width;
		button.setPreferredSize(new Dimension(buttonPreferredWidth, HEADER_HEIGHT));
		button.setMaximumSize(new Dimension(buttonPreferredWidth, HEADER_HEIGHT));
	}
	
	
	
	private void updateToSearchPage()
	{
		
	}
}