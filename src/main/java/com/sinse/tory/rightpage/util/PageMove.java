package com.sinse.tory.rightpage.util;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;


public class PageMove {
	JPanel rightPage;
	CardLayout cardLayout;
	public List<JPanel>list = new ArrayList<>();
	
	public void showPage(int page1,int page2) {
		list.get(page1).setVisible(true);
		list.get(page2).setVisible(false);
	}
}
