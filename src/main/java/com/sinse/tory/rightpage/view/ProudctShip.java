package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProudctShip extends JPanel{
	JButton[]bt;
	JComboBox[] box;
	JLabel[]la;
	JTextField t_count;
	JPanel p_img;
	public ProudctShip() {
		bt = new JButton[4];
		box = new JComboBox[3];
		la = new JLabel[4];
		t_count = new JTextField();
		p_img = new JPanel();
		
		for(int i=0; i<bt.length;i++) {
			bt[i] = new JButton();
		}
		for(int i=0; i<la.length;i++) {
			la[i] = new JLabel();
		}
		for(int i=0; i<box.length;i++) {
			box[i] = new JComboBox();
		}
		
		
		bt[0].setText("상품정보 수정");
		bt[1].setText("상품추가");
		bt[2].setText("출고");
		bt[3].setText("입고");
		
		la[0].setText("상위카테고리");
		la[1].setText("하위카테고리");
		la[2].setText("상품명");
		la[3].setText("수량");
		
		setLayout(null);
		for(int i=0; i<box.length;i++) {
			la[i].setBounds(350,50+75*i,320,40);
			box[i].setBounds(350,90+i*70,320,40);
		}
		la[3].setBounds(ALLBITS, ABORT, WIDTH, HEIGHT);
		
		p_img.setBounds(10,50,317,257);
		p_img.setBackground(Color.cyan);
		
		
		
		add(bt[0]);
		add(bt[1]);
		add(p_img);
		for(int i=0; i<box.length;i++) {
			add(la[i]);
			add(box[i]);
		}
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(668,581));
	}

}
