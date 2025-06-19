package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.sinse.tory.db.common.util.PageUtil;

public class MainPage extends JPanel{
	public MainPage() {
		ProductShip productShip = new ProductShip();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(productShip);
		
		
		
		setPreferredSize(new Dimension(PageUtil.InputOutput_Width,PageUtil.Tory_Hieght));
		setVisible(true);
	}

}
