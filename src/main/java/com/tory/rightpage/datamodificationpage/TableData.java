package com.tory.rightpage.datamodificationpage;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
}