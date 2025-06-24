package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.rightpage.datamodificationpage.DataModificationPage;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.util.PageUtil;

public class Testmain extends JFrame{
	JPanel leftPage;
	JPanel rightPage ;
	PageMove pageShow;
	ProductShip productShip;
	DataModificationPage dataModificationPage;
	PageMove pageMove;
	public Testmain() {
		pageMove = new PageMove();
		productShip = new ProductShip(this);
		dataModificationPage = new DataModificationPage(pageMove);
		
		creatPage();
		rightPage = new JPanel();
		leftPage = new JPanel();
		rightPage.setLayout(new CardLayout());
		
		for(int i=0;i<pageMove.list.size();i++) {
			rightPage.add(pageMove.list.get(i));
		}
		setLayout(new GridLayout(1,2));
		add(leftPage);
		add(rightPage);
		pageMove.showPage(0,1);
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void creatPage() {
		pageMove.list.add(productShip);
		pageMove.list.add(dataModificationPage);
		
	}
	
	public static void main(String[] args) {
		new Testmain();
	}
}
