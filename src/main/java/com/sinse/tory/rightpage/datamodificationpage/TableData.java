package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

// 상품 정보 테이블의 한 데이터
final class TableData extends JPanel
{
	private JPanel titlePanel;
	private JLabel titleLabel;
	
	
	
	TableData(String title, JComponent content)
	{
		titlePanel = new JPanel();
		titleLabel = new JLabel(title);
		setLayout(new GridLayout(1, 2));
		add(titlePanel);
		add(content);
		
		titlePanel.add(titleLabel);
		titlePanel.setBorder(BorderFactory.createLineBorder(new Color(0.8f, 0.8f, 0.8f)));
		
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
}