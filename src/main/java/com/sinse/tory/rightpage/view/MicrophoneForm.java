package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.acl.Owner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.sinse.tory.db.common.config.Config;

public class MicrophoneForm extends JPanel{
	JButton bt;//마이크가 있는 버튼
	JLabel la_explain;
	JPanel p_helper;// 도우미창을 활설화 시킬 라벨
	JWindow d_helper;
	JWindow w_mic;
	JTextArea area;
	
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
		
		//설명할 내용을 포함할 라벨
		la_explain = new JLabel("<html><body style='width:180px'>"
				+ "설명서<br>"
				+ "내용<br>"
				+ "내용<br>"				
				+ "</body></html>"
				, JLabel.CENTER);
		
		//위,왼쪽,하단,오른쪽의 넓이 설정
		la_explain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		d_helper = new JWindow();//설명창으로 쓸 다이얼로그
		d_helper.setLayout(new BorderLayout());
		d_helper.add(la_explain); // 설명문 부착
		d_helper.setAlwaysOnTop(true);//항상 위에 있게 설정
		d_helper.pack();//크기를 자동으로 조절
		
		
		bt.setPreferredSize(new Dimension(96,96)); // 버튼의 크기
		setPreferredSize(new Dimension(668,120)); // 현 패널의 크기
		p_helper.setPreferredSize(new Dimension(24,24)); // 도우미 크기
		
		Color ff = Color.decode("#F4F5F6");
		setBackground(ff);
		
		w_mic = new JWindow();
		area = new JTextArea();
		w_mic.setLayout(new BorderLayout());
		area.setPreferredSize(new Dimension(getPreferredSize().getSize().width+30,getPreferredSize().getSize().height));
		w_mic.setFocusableWindowState(true);
		area.setBackground(ff);
		w_mic.add(new JScrollPane(area), BorderLayout.CENTER);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		w_mic.pack();
		
		
		//이벤트 부여
		//버튼을 클릭하면 버튼이 있는 패널의 높이가 커진다
		bt.addActionListener(e->{
//			follow(w_mic,p_helper, -380, 30);
//			w_mic.setVisible(true);
			Window parentWindow = SwingUtilities.getWindowAncestor(MicrophoneForm.this);
			w_mic.setFocusableWindowState(true);
			w_mic.setFocusable(true);
			
			    // 처음 위치 설정
			follow(w_mic, p_helper, -380, 30);
			w_mic.setVisible(true);
			area.requestFocusInWindow(); // 또는 area.requestFocus();
			
			if (parentWindow != null) {
			    // 창이 움직일 때마다 팝업 위치 갱신
			    parentWindow.addComponentListener(new ComponentAdapter() {
			        @Override
			        public void componentMoved(ComponentEvent e) {
			            follow(w_mic, p_helper, -380, 30);
			        }
			        @Override
			        public void componentResized(ComponentEvent e) {
			            follow(w_mic, p_helper, -380, 30);
			        }
			    });
			}
		});
		
		//물음표그림에 마우스이밴트 연결
		p_helper.addMouseListener(new MouseAdapter() {
			//마우스와 접촉하면 설명다이얼그램이 보이게 한다
			public void mouseEntered(MouseEvent e) {				
				follow(d_helper, p_helper,30,80);
		        d_helper.setVisible(true);
				
			}
			//마우스가 나가면 설명다이얼그램이 안 보이게 한다
			public void mouseExited(MouseEvent e) {
				d_helper.setVisible(false);
			}
		});
		
		
		add(bt);
		add(p_helper);
		setVisible(true);
	}
	public void follow(JWindow window, JPanel p,int x,int y) {
     // 부모 창 위치 구하기
        Window parentWindow = SwingUtilities.getWindowAncestor(MicrophoneForm.this);
        if (parentWindow != null) {
            Point windowLocation = parentWindow.getLocationOnScreen();
            Point helperLocal = p.getLocation(); // MicrophoneForm 내부 위치

            // 설명창 위치 계산
            int helperX = windowLocation.x + helperLocal.x + x;
            int helperY = windowLocation.y + MicrophoneForm.this.getY() + y - window.getHeight();

            window.setLocation(helperX, helperY);
        }
        
		
	}
	
}






