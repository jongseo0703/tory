package com.sinse.tory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.leftpage.view.InventoryUI;

public class MainPage extends JFrame{
	
	InventoryUI inventoryUI;
	JPanel rightPanel;
	
	public MainPage() {
		
		// 왼쪽 패널 (InventoryUI)
		inventoryUI = new InventoryUI();
		inventoryUI.setPreferredSize(new Dimension(960,1080));
		add(inventoryUI, BorderLayout.WEST);
		
		// 임시로 오른쪽 패널 삽입
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.YELLOW);
	
		setTitle("음성기반 창고관리 Tory");
		setSize(1920,1080);
		setBackground(Color.PINK);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1,2));
		add(inventoryUI);
		add(rightPanel);
	
		setVisible(true);
		
	}
    public static void main( String[] args ){
    	new MainPage();
    }
}
