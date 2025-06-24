package com.sinse.tory.rightpage.view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class RightPage extends JPanel
{
	private JPanel rightPageContent;
	private MicrophoneForm microphonePanel;
	
	
	
	RightPage()
	{
		rightPageContent = new JPanel();
		microphonePanel = new MicrophoneForm(null);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(rightPageContent);
		add(microphonePanel);
		
		
	}
}
