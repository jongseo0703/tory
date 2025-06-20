package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.sinse.tory.db.common.util.PageUtil;

public class Testmain extends JFrame{
	JPanel leftPage;
	JPanel rightPage ;
	JPanel[]pages;
	public Testmain() {
		rightPage = new JPanel();
		leftPage = new JPanel();
		creatPage();
		add(leftPage);
		rightPage.add(pages[0]);
		rightPage.add(pages[1]);
		add(rightPage);
		showPage(1,0);
		setLayout(new GridLayout(1,2));
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new Testmain();
	}
	public void creatPage() {
		pages = new JPanel[2];
		pages[0] = new ProductShip(this);
		
	}
	public void showPage(int page1,int page2) {
		pages[page1].setVisible(true);
		pages[page2].setVisible(false);
	}
}
