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
		
		// 통합된 InventoryUI 사용 (main/view 기능 포함)
		inventoryUI = new InventoryUI();
		inventoryUI.setPreferredSize(new Dimension(960,1080));
		
		// 임시로 오른쪽 패널 삽입
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.YELLOW);
	
		setTitle("음성기반 창고관리 Tory - 통합 버전");
		setSize(1920,1080);
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
