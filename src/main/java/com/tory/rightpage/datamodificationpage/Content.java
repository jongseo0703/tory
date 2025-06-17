package com.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

// 헤더 구역 외 나머지. 
public final class Content extends JPanel
{
	private JPanel identificationPanel;
	private JPanel dataPanel;
	
	private static final int HORIZONTAL_MARGIN = 12;
	
	
	
	public Content()
	{
		identificationPanel = new JPanel();
		dataPanel = new JPanel();
		
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(identificationPanel);
		add(dataPanel);
		
		identificationPanel.setBackground(Color.red);
		dataPanel.setBackground(Color.blue);
	}
}