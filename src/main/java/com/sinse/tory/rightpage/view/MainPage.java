package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainPage extends JFrame{
	JPanel p_mic;
	public MainPage() {
		p_mic = new MicrophoneForm();
		add(p_mic,BorderLayout.SOUTH);
		add(new ProudctShip());
		
		
		setSize(new Dimension(720,810));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new MainPage();
	}
}
