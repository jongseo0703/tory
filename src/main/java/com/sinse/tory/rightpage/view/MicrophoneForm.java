
package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.sinse.tory.rightpage.util.PageUtil;
import com.sinse.tory.rightpage.util.Pages;

public class MicrophoneForm extends Pages{
	JButton bt;//마이크가 있는 버튼
	JLabel la_explain;// 설명문이 들어있는 라벨
	JPanel p_helper;// 도우미창을 활설화 시킬 라벨
	JWindow d_helper; // 설명라벨이 부착되에 있는 곳
	JWindow w_mic;// 버튼을 누르면 나오는 곳
	JTextArea area;// 음성이식해서 출력될 곳
	JScrollPane scroll;
	boolean fig = false;
	public MicrophoneForm(Testmain testmain) {
		super(testmain);
		bt = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBorderPainted(false);//버튼의 윤각 지우기
				setContentAreaFilled(false);
				Image img = null;
				URL url=this.getClass().getClassLoader().getResource("images/Mic.png");//main/resources/images/안에있는 예비용 이미지
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
		//설명문이 부착될 패널
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
		setPreferredSize(new Dimension(0,150)); // 현 패널의 크기
		setMaximumSize(new Dimension(Integer.MAX_VALUE,150)); // 현 패널의 크기
		p_helper.setPreferredSize(new Dimension(24,24)); // 도우미 크기
		
		Color ff = Color.decode("#F4F5F6");
		setBackground(ff);
		
		w_mic = new JWindow(testmain);
		area = new JTextArea();
		scroll = new JScrollPane(area);
		w_mic.setLayout(new BorderLayout());
		scroll.setPreferredSize(new Dimension(getPreferredSize().getSize().width-8,90));//텍스트에리아 넓이 설정
		w_mic.setFocusableWindowState(true);
		area.setBackground(ff);
		scroll.setBorder(null);
		w_mic.add(scroll, BorderLayout.CENTER);
		w_mic.pack(); // 크기 자동 조절
		
		//이벤트 부여
		//버튼을 클릭하면 버튼이 있는 패널의 높이가 커진다
		bt.addActionListener(e->{
			//MicrophoneForm.this는 현재 객체 참조
			//getWindowAncestor(받은 컴포넌트)는 받는 컴포넌트가 포함된 최상위 window 반환
			//SwingUtilitiesd는 Swing작업을 편하게 해주는 정석 헬퍼세트
			//스레드 문제·부모창 찾기·좌표 꼬임을 처리할 수 있게 해준다
			Window parentWindow = SwingUtilities.getWindowAncestor(MicrophoneForm.this);		
			  //w_mic 속성 설정
		    w_mic.setFocusableWindowState(true);//키보드입력이나 클릭 반응 허용
		    w_mic.setFocusable(true);//포커스를 받을 준비되도록 허용 

		    fig = !fig; //버튼 하나로 팝업창 열고닫기 하기위한 논리값 
		    w_mic.setVisible(fig);
		    area.setText(fig ? area.getText() : ""); //끄면 텍스트 초기화
		    SwingUtilities.invokeLater(() -> area.requestFocusInWindow());

		    if (parentWindow != null) {
		        // 팝업창 위치/크기 계산하는 함수
		        Runnable updateHelperLocationAndSize = () -> {
		            Point panelOnScreen = MicrophoneForm.this.getLocationOnScreen();

		            int newWidth = parentWindow.getWidth() / 2 - 10; //팝업창의 넓이
		            int newHeight = parentWindow.getHeight() / 7;// 팝업창의 높이
		            w_mic.setSize(newWidth, newHeight);
		            
		            w_mic.setLocation(
		            		panelOnScreen.x, //팝업창의 x값
		            		panelOnScreen.y -newHeight //팡업창의 y값
		            		);
		        };
		        updateHelperLocationAndSize.run();//메서드 호출

		        // 한 번만 리스너 등록
		        parentWindow.removeComponentListener(parentWindow.getComponentListeners().length > 0 ? parentWindow.getComponentListeners()[0] : null);
		        parentWindow.addComponentListener(new java.awt.event.ComponentAdapter() {
		            @Override
		            public void componentMoved(ComponentEvent e) {
		                updateHelperLocationAndSize.run();
		            }

		            @Override
		            public void componentResized(ComponentEvent e) {
		                updateHelperLocationAndSize.run();
		            }
		        });
		    }
		});
		
		
		
		//물음표그림에 마우스이밴트 연결
		p_helper.addMouseListener(new MouseAdapter() {
			//마우스와 접촉하면 설명다이얼그램이 보이게 한다
			public void mouseEntered(MouseEvent e) {				
				follow(d_helper, p_helper,30,0);
		        d_helper.setVisible(true);
				
			}
			//마우스가 나가면 설명다이얼그램이 안 보이게 한다
			public void mouseExited(MouseEvent e) {
				d_helper.setVisible(false);
			}
		});
		
		setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		add(bt);
		add(p_helper);
		setVisible(true);
	}
	public void follow(JWindow window, JPanel p,int x,int y) {
     // 부모 창 위치 구하기
        Window parentWindow = SwingUtilities.getWindowAncestor(MicrophoneForm.this);
        if (parentWindow != null) {

            // 팝업 위치 갱신 함수
            Runnable updateHelperLocation = () -> {
                // 패널의 화면 위치 계산
                Point panelOnScreen = MicrophoneForm.this.getLocationOnScreen();
                int helperX = panelOnScreen.x + p.getX() + x;
                int helperY = panelOnScreen.y + p.getY() + y - window.getHeight();
                
                window.setLocation(helperX, helperY);
            };
            updateHelperLocation.run();

            // 한 번만 리스너 등록 (중복 등록 방지)
            for (ComponentListener listener : parentWindow.getComponentListeners()) {
                if (listener instanceof ComponentAdapter) {
                    parentWindow.removeComponentListener(listener);
                }
            }

            parentWindow.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    updateHelperLocation.run();
                }

                @Override
                public void componentResized(ComponentEvent e) {
                    updateHelperLocation.run();
                }
            });
	
        }
	}
	
}






