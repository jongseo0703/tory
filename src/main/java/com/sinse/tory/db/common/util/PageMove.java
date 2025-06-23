package com.sinse.tory.db.common.util;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;


public class PageMove {
	JPanel rightPage;
	CardLayout cardLayout;
	List<JPanel>list = new ArrayList<>();
	public PageMove(JPanel rightPage,CardLayout cardLayout) {
		this.rightPage = rightPage;
		this.cardLayout = cardLayout;
		rightPage.setLayout(new CardLayout());
	}
	
	public void creatPage(JPanel panel) {
		list.add(panel);
		rightPage.add(panel);
	}
	public void showPage(int page1,int page2) {
		list.get(page1).setVisible(true);
		list.get(page2).setVisible(true);
	}
}
