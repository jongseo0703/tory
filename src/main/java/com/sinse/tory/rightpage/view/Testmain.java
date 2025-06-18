package com.sinse.tory.rightpage.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.sinse.tory.db.common.util.PageUtil;

public class Testmain extends JFrame{
	public Testmain() {
		
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new Testmain();
	}
}
