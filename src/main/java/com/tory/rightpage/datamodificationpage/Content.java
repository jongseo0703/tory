package com.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

// 헤더 구역 외 나머지. 
public final class Content extends JPanel
{
	private JPanel simpleDataPanel;	
	private ImageUploadPanel imageUploadPanel;
	private JPanel identificationDataPanel;
	
	private JPanel detailDataPanel;
	
	private static final int HORIZONTAL_MARGIN = 12;
	
	
	
	public Content()
	{
		simpleDataPanel = new JPanel();
		imageUploadPanel = new ImageUploadPanel();
		identificationDataPanel = new JPanel();
		detailDataPanel = new JPanel();
		
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(simpleDataPanel);
		add(detailDataPanel);
		
		simpleDataPanel.setLayout(new BoxLayout(simpleDataPanel, BoxLayout.X_AXIS));
		simpleDataPanel.setBackground(Color.red);
		simpleDataPanel.add(imageUploadPanel);
		simpleDataPanel.add(Box.createRigidArea(new Dimension(16, 0)));
		simpleDataPanel.add(identificationDataPanel);
		
		detailDataPanel.setBackground(Color.blue);
	}
}