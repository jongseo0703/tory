package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;

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
		setAlignmentX(LEFT_ALIGNMENT);
		add(label);
		add(component);
		
		label.setText(labelText);
		label.setAlignmentX(LEFT_ALIGNMENT);
		label.setHorizontalAlignment(JLabel.LEFT);
		int preferredHeight = label.getPreferredSize().height;
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));
		
		int componentPreferredHeight = component.getPreferredSize().height;
		component.setMaximumSize(new Dimension(Integer.MAX_VALUE, componentPreferredHeight));
	}
}