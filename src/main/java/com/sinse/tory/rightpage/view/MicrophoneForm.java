package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MicrophoneForm extends JPanel{
	JButton bt;
	JPanel p_ex;
	
	public MicrophoneForm() {
		bt = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(null,0,0,96,96,bt);
			}
		};
		bt.setPreferredSize(new Dimension(96,96));
		bt.setBackground(null);
		Color ff = Color.decode("#F4F5F6");
		setBackground(ff);
		setPreferredSize(new Dimension(668,120));
		
		add(bt);
		setVisible(true);
	}
}

