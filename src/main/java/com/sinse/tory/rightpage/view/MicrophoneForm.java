package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MicrophoneForm extends JPanel{
	JButton bt;//마이크가 있는 버튼
	JPanel p_helper;// 도우미창을 활설화 시킬 라벨
	public MicrophoneForm() {
		bt = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBorderPainted(false);//버튼의 윤각 지우기
				setContentAreaFilled(false);
				Image img = null;
				URL url=this.getClass().getClassLoader().getResource("images/mic.png");//main/resources/images/안에있는 예비용 이미지
				try {
					BufferedImage bufferImage= ImageIO.read(url);//예비 마이크 이미지
					img = bufferImage.getScaledInstance(96, 96, Image.SCALE_SMOOTH);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					g.drawImage(img, 0, 0, 96, 96, this);
				}
			};
		
		p_helper = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				URL url = this.getClass().getClassLoader().getResource("images/question.png");
				Image img =null;
				try {
					BufferedImage bufferImage = ImageIO.read(url);
					img = bufferImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g.drawImage(img, 0, 0, 24, 24, this);
				
			}
		};
			
		bt.setPreferredSize(new Dimension(96,96)); // 버튼의 크기
		setPreferredSize(new Dimension(668,120)); // 현 패널의 크기
		p_helper.setPreferredSize(new Dimension(24,24)); // 도우미 크기
		
		Color ff = Color.decode("#F4F5F6");
		setBackground(ff);
		
		//이벤트 부여
		//버튼을 클릭하면 버튼이 있는 패널의 높이가 커진다
		bt.addActionListener(e->{
			//계획중
		});
		
		
		add(bt);
		add(p_helper);
		setVisible(true);
	}
	
}






