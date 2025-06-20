package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class LabelComponentSet extends JPanel
{
	private JLabel label;
	private JComponent component;
	
	
	
	public LabelComponentSet(String labelText, JComponent component)
	{
		label = new JLabel();
		this.component = component;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		add(label, BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension(0, 8)));
		add(component, BorderLayout.SOUTH);
		
		label.setText(labelText);
		label.setAlignmentX(LEFT_ALIGNMENT);
		component.setAlignmentX(LEFT_ALIGNMENT);
		
		component.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
}