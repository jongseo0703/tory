package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

public class MainPage extends JPanel{
	JPanel p_mic;
	public MainPage() {
		
		p_mic = new MicrophoneForm();
		add(p_mic,BorderLayout.SOUTH);
		add(new ProductShip());
		
		
		setPreferredSize(new Dimension(720,810));
		setVisible(true);
	}

}
