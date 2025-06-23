package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.db.common.util.PageMove;
import com.sinse.tory.db.common.util.PageUtil;
import com.sinse.tory.rightpage.datamodificationpage.DataModificationPage;

public class Testmain extends JFrame{
	JPanel leftPage;
	JPanel rightPage ;
	List<JPanel> pages;
	PageMove pageShow;
	ProductShip productShip;
	DataModificationPage dataModificationPage;
	public Testmain() {
		productShip = new ProductShip(this);
		dataModificationPage = new DataModificationPage();
		rightPage = new JPanel();
		leftPage = new JPanel();
		rightPage.setLayout(new CardLayout());
		
		
		creatPage();
		for(int i=0;i<pages.size();i++) {
			rightPage.add(pages.get(i));
		}
		add(leftPage);
		add(rightPage);
		showPage(0,1);
		setLayout(new GridLayout(1,2));
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void creatPage() {
		pages = new ArrayList<>();
		pages.add(productShip);
		pages.add(dataModificationPage);
		
	}
	public void showPage(int page1,int page2) {
		pages.get(page1).setVisible(true);
		pages.get(page2).setVisible(false);
	}
	
	public static void main(String[] args) {
		new Testmain();
	}
}
