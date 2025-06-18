package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

final class IdentificationPanel extends JPanel
{
	// TODO : 나중에 제네릭 타입 model 객체로 변경해야 함
	private JComboBox<String> topCategory;
	private JComboBox<String> subCategory;
	private JComboBox<String> name;
	//
	
	
	
	IdentificationPanel()
	{
		topCategory = new JComboBox<String>();
		subCategory = new JComboBox<String>();
		name = new JComboBox<String>();
		
		setBackground(Color.green);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topCategory);
		add(subCategory);
		add(name);
		
		topCategory.addItem("dummy top category");
		int topCategoryPreferredHeight = topCategory.getPreferredSize().height;
		topCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, topCategoryPreferredHeight));
		
		subCategory.addItem("dummy sub category");
		int subCategoryPreferredHeight = subCategory.getPreferredSize().height;
		subCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, subCategoryPreferredHeight));
		
		name.addItem("dummy name");
		int namePreferredHeight = name.getPreferredSize().height;
		name.setMaximumSize(new Dimension(Integer.MAX_VALUE, namePreferredHeight));
	}
}