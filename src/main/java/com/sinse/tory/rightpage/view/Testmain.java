package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.db.common.util.PageUtil;

public class Testmain extends JFrame{
	JPanel leftPage;
	public Testmain() {
		leftPage = new JPanel();
		MainPage mainPage = new MainPage();
		add(leftPage);
		add(mainPage);
		
		setLayout(new GridLayout(1,2));
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new Testmain();
	}
}
