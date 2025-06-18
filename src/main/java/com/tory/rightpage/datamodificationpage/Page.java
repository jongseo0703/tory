package com.tory.rightpage.datamodificationpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

// 우측 페이지에서 마이크 영역을 제외한 영역들의 객체들을 모두 포함하는 객체
// 소희가 어떻게 구현했는지에 따라 결합 시 충돌이 발생할 수 있음.
// 결합할 때 같이 볼 예정
public final class Page extends JPanel
{
	private Header header;
	private Content content;
	
	// TODO : 나중에 공용으로 사용할 상수에 추가해야 함.
	public static final int WIDTH = 720;
	public static final int HEIGHT = 810;
	public static final int HORIZONTAL_MARGIN = 16;
	public static final int VERTICAL_MARGIN = 48;
	public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	//
	
	
	
	public Page()
	{
		header = new Header(WIDTH, HORIZONTAL_MARGIN);
		content = new Content();
		
		setBackground(Color.gray);
		setBorder(BorderFactory.createEmptyBorder(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(header);
		add(Box.createRigidArea(new Dimension(0, 32)));
		add(content);
	}
}