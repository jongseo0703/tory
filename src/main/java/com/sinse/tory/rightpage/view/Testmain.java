package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.rightpage.datamodificationpage.DataModificationPage;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.util.PageUtil;

public class Testmain extends JFrame{
	JPanel leftPage;
	JPanel rightPage ;
	private JPanel rightPageContent;
	PageMove pageShow;
	ProductShip productShip;
	DataModificationPage dataModificationPage;
	PageMove pageMove;
	
	private static final int WIDTH_MARGIN = 16;
	private static final int HEIGHT_MARGIN = 48;
	
	
	
	public Testmain()
	{
		pageMove = new PageMove();
		productShip = new ProductShip(this);
		dataModificationPage = new DataModificationPage(pageMove, null);
		
		creatPage();
		rightPage = new JPanel();
		rightPageContent = new JPanel();
		leftPage = new JPanel();
		MicrophoneForm microphoneForm = new MicrophoneForm(this);
		
		setLayout(new GridLayout(1,2));
		add(leftPage);
		add(rightPage);
		setSize(new Dimension(PageUtil.Tory_Width,PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		rightPage.setLayout(new BoxLayout(rightPage, BoxLayout.Y_AXIS));
		rightPage.add(rightPageContent);
		rightPage.add(microphoneForm);
		rightPage.setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		
		rightPageContent.setLayout(new CardLayout());
		rightPageContent.setPreferredSize(new Dimension(0, 0));
		rightPageContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		for(int i=0;i<pageMove.list.size();i++)
		{
			rightPageContent.add(pageMove.list.get(i));
		}
		
		pageMove.showPage(0,1);
	}
	public void creatPage() {
		pageMove.list.add(productShip);
		pageMove.list.add(dataModificationPage);
		
	}
	
	public static void main(String[] args) {
		new Testmain();
	}
}
