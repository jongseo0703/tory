package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

import com.sinse.tory.rightpage.util.Pages;

public class MicrophoneForm extends Pages{
	JButton micButton; // 마이크 버튼
	JLabel titleLabel; // 제목 라벨
	JLabel helpButton; // 도움말 버튼
	JPanel helpPanel; // 도우미창을 활성화 시킬 패널
	JWindow helpWindow; // 설명창
	JWindow micWindow; // 음성 입력창
	JTextArea textArea; // 음성 인식 결과 출력 영역
	JScrollPane scrollPane;
	
	public MicrophoneForm(Object testmain) {
		super(testmain);
		
		// 제목 라벨 생성
		titleLabel = new JLabel("음성 입력");
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		titleLabel.setForeground(new Color(70, 70, 70));
		
		// 마이크 버튼
		micButton = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// 배경 원형 그리기
				g2.setColor(new Color(52, 144, 220));
				g2.fillOval(4, 4, getWidth()-8, getHeight()-8);
				
				// 호버 효과
				if(getModel().isRollover()) {
					g2.setColor(new Color(52, 144, 220, 50));
					g2.fillOval(0, 0, getWidth(), getHeight());
				}
				
				// 마이크 아이콘 그리기
				Image img = null;
				URL url = this.getClass().getClassLoader().getResource("images/mic.png");
				try {
					BufferedImage bufferImage = ImageIO.read(url);
					img = bufferImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(img != null) {
					int x = (getWidth() - 48) / 2;
					int y = (getHeight() - 48) / 2;
					g2.drawImage(img, x, y, this);
				}
			}
		};
		
		// 마이크 버튼 스타일링
		micButton.setPreferredSize(new Dimension(80, 80));
		micButton.setFocusPainted(false);
		micButton.setBorderPainted(false);
		micButton.setContentAreaFilled(false);
		micButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		// 도움말 버튼 생성
		helpButton = new JLabel("?");
		helpButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		helpButton.setForeground(Color.WHITE);
		helpButton.setBackground(new Color(108, 117, 125));
		helpButton.setOpaque(true);
		helpButton.setHorizontalAlignment(JLabel.CENTER);
		helpButton.setPreferredSize(new Dimension(24, 24));
		helpButton.setBorder(BorderFactory.createEmptyBorder());
		helpButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		// 도움말 패널
		helpPanel = new JPanel();
		helpPanel.setLayout(new BorderLayout());
		helpPanel.add(helpButton, BorderLayout.CENTER);
		helpPanel.setPreferredSize(new Dimension(24, 24));
		helpPanel.setBackground(new Color(108, 117, 125));
		
		// 도움말 창 내용
		JLabel helpContent = new JLabel("<html><body style='width:200px; padding:10px;'>"
				+ "<b>음성 입력 사용법</b><br><br>"
				+ "1. 마이크 버튼을 클릭하세요<br>"
				+ "2. 명확하게 말씀해 주세요<br>"
				+ "3. 인식된 텍스트를 확인하세요<br>"
				+ "4. 필요시 수정하여 사용하세요"
				+ "</body></html>");
		helpContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		helpContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 도움말 창
		helpWindow = new JWindow();
		helpWindow.setLayout(new BorderLayout());
		helpWindow.add(helpContent);
		helpWindow.setAlwaysOnTop(true);
		helpWindow.setBackground(Color.WHITE);
		helpWindow.getRootPane().setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
			)
		);
		helpWindow.pack();
		
		// 음성 입력 결과 창
		textArea = new JTextArea();
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		textArea.setBackground(Color.WHITE);
		textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 120));
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		
		micWindow = new JWindow();
		micWindow.setLayout(new BorderLayout());
		micWindow.add(scrollPane, BorderLayout.CENTER);
		micWindow.setBackground(Color.WHITE);
		micWindow.pack();
		
		// 레이아웃 구성
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(248, 249, 250));
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)
		));
		
		// 상단 제목과 도움말
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(new Color(248, 249, 250));
		topPanel.add(titleLabel);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(helpPanel);
		
		// 마이크 버튼 중앙 정렬
		JPanel micPanel = new JPanel();
		micPanel.setLayout(new BoxLayout(micPanel, BoxLayout.X_AXIS));
		micPanel.setBackground(new Color(248, 249, 250));
		micPanel.add(Box.createHorizontalGlue());
		micPanel.add(micButton);
		micPanel.add(Box.createHorizontalGlue());
		
		add(topPanel);
		add(Box.createRigidArea(new Dimension(0, 15)));
		add(micPanel);
		
		setPreferredSize(new Dimension(0, 140));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
		
		// 이벤트 연결
		micButton.addActionListener(e -> {
			// 음성 입력 시뮬레이션 (실제 음성 인식 대신)
			String[] sampleCommands = {
				"나이키 티셔츠 엘사이즈 29000원 10개 추가",
				"아디다스 바지 미디엄 45000원 5개 추가",
				"반팔티 라지 25000원 15개 추가"
			};
			
			// 랜덤 명령어 선택
			String selectedCommand = sampleCommands[(int)(Math.random() * sampleCommands.length)];
			textArea.setText("🎤 음성 인식 결과:\n" + selectedCommand);
			
			// 음성 창 표시
			showVoiceWindow();
			
			// ProductAddPage에 음성 명령 전달 (현재 페이지가 ProductAddPage인 경우)
			if (testmain != null) {
				try {
					Object productAddPage = testmain.getClass().getField("productAddPage").get(testmain);
					
					// ProductAddPage의 fillFromVoiceCommand 메서드 호출
					productAddPage.getClass().getMethod("fillFromVoiceCommand", String.class)
						.invoke(productAddPage, selectedCommand);
					
				} catch (Exception ex) {
					System.out.println("음성 명령 전달 실패: " + ex.getMessage());
				}
			}
		});
		
		// 도움말 마우스 이벤트
		helpPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				followMouse(helpWindow, helpPanel, 30, 0);
				helpWindow.setVisible(true);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				helpWindow.setVisible(false);
			}
		});
		
		setVisible(true);
	}
	
	/**
	 * 음성 입력 결과 창 표시
	 */
	private void showVoiceWindow() {
		// 화면 중앙에 표시
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - micWindow.getWidth()) / 2;
		int y = (screenSize.height - micWindow.getHeight()) / 2;
		micWindow.setLocation(x, y);
		micWindow.setVisible(true);
		
		// 3초 후 자동으로 닫기
		javax.swing.Timer timer = new javax.swing.Timer(3000, e -> micWindow.setVisible(false));
		timer.setRepeats(false);
		timer.start();
	}
	
	private void followMouse(JWindow window, JPanel panel, int offsetX, int offsetY) {
		Window parentWindow = SwingUtilities.getWindowAncestor(MicrophoneForm.this);
		if (parentWindow != null) {
			Runnable updateLocation = () -> {
				Point panelOnScreen = MicrophoneForm.this.getLocationOnScreen();
				int x = panelOnScreen.x + panel.getX() + offsetX;
				int y = panelOnScreen.y + panel.getY() + offsetY - window.getHeight();
				window.setLocation(x, y);
			};
			updateLocation.run();
			
			// 기존 리스너 제거
			for (ComponentListener listener : parentWindow.getComponentListeners()) {
				if (listener instanceof ComponentAdapter) {
					parentWindow.removeComponentListener(listener);
				}
			}
			
			parentWindow.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentMoved(ComponentEvent e) {
					updateLocation.run();
				}
				
				@Override
				public void componentResized(ComponentEvent e) {
					updateLocation.run();
				}
			});
		}
	}
}






