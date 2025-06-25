package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 이미지 업로드 패널
 */
final class ImageUploadPanel extends JPanel {
	private JButton uploadButton;
	private JLabel orLabel;
	private JTextField urlInputField;
	
	private static final int WIDTH_MARGIN = 12;
	private static final int HEIGHT_MARGIN = 32;
	private static final int INPUT_FIELD_WIDTH_MARGIN = 16;
	private static final int INPUT_FIELD_HEIGHT_MARGIN = 8;
	
	ImageUploadPanel() {
		uploadButton = new JButton("이미지 [png, jpg, ...]");
		orLabel = new JLabel("혹은");
		urlInputField = new JTextField();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		setBackground(Color.gray);
		add(Box.createVerticalGlue());
		add(uploadButton);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(orLabel);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(urlInputField);
		add(Box.createVerticalGlue());
		
		uploadButton.setAlignmentX(CENTER_ALIGNMENT);
		int uploadButtonPreferredHeight = uploadButton.getPreferredSize().height;
		uploadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, uploadButtonPreferredHeight));
		
		orLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		urlInputField.setAlignmentX(CENTER_ALIGNMENT);
		urlInputField.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 14));
		urlInputField.setBorder(BorderFactory.createEmptyBorder(INPUT_FIELD_HEIGHT_MARGIN, INPUT_FIELD_WIDTH_MARGIN, INPUT_FIELD_HEIGHT_MARGIN, INPUT_FIELD_WIDTH_MARGIN));
		int urlInputFieldPreferredHeight = urlInputField.getPreferredSize().height;
		urlInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, urlInputFieldPreferredHeight));
	}
}