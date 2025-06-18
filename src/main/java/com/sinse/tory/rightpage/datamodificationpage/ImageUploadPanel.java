package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

final class ImageUploadPanel extends JPanel
{
	private JButton uploadButton;
	private JLabel orLabel;
	private JTextField urlInputField;
	
	private static final int WIDTH_MARGIN = 12;
	private static final int HEIGHT_MARGIN = 32;
	
	
	
	ImageUploadPanel()
	{
		uploadButton = new JButton("이미지 [png, jpg, ...]");
		orLabel = new JLabel("혹은");
		urlInputField = new JTextField();
		setBackground(Color.red);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		add(Box.createVerticalGlue());
		add(uploadButton);
		add(orLabel);
		add(urlInputField);
		add(Box.createVerticalGlue());
		
		uploadButton.setAlignmentX(CENTER_ALIGNMENT);
		int uploadButtonPreferredHeight = uploadButton.getPreferredSize().height;
		uploadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, uploadButtonPreferredHeight));
		
		orLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		urlInputField.setAlignmentX(CENTER_ALIGNMENT);
		urlInputField.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 14));
		int urlInputFieldPreferredHeight = urlInputField.getPreferredSize().height;
		urlInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, urlInputFieldPreferredHeight));
	}
}