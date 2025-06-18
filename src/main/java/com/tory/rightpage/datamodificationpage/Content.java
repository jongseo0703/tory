package com.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

// 헤더 구역 외 나머지. 
public final class Content extends JPanel
{
	private JPanel simpleDataPanel;	
	private ImageUploadPanel imageUploadPanel;
	private IdentificationPanel identificationPanel;
	
	private DetailDataTable detailDataTable;
	
	private static final int HORIZONTAL_MARGIN = 12;
	
	
	
	public Content()
	{
		simpleDataPanel = new JPanel();
		imageUploadPanel = new ImageUploadPanel();
		identificationPanel = new IdentificationPanel();
		detailDataTable = new DetailDataTable();
		
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));
		setLayout(new GridLayout(2, 1, 0, 16));
		
		add(simpleDataPanel);
		add(detailDataTable);
		
		simpleDataPanel.setLayout(new GridLayout(1, 2, 16, 0));
		simpleDataPanel.setBackground(Color.red);
		simpleDataPanel.add(imageUploadPanel);
		simpleDataPanel.add(identificationPanel);
	}
}