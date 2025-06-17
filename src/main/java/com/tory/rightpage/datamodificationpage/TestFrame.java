package com.tory.rightpage.datamodificationpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

final class TestFrame extends JFrame
{
	private TestFrame()
	{
		JPanel leftPage = new JPanel();
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1440, 810));
		setLayout(new GridLayout(1, 2));
		add(leftPage);
		add(new Page());
	}
	
	
	
	public static void main(String[] args)
	{
		new TestFrame();
	}
}