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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.sinse.tory.rightpage.util.Pages;
import com.sinse.tory.stt.AudioRecorder;
import com.sinse.tory.stt.GoogleSpeechToText;
import com.sinse.tory.tts.GoogleTextToSpeech;
import com.sinse.tory.tts.AudioPlayer;

public class MicrophoneForm extends Pages{
	JButton micButton; // 마이크 버튼
	JLabel titleLabel; // 제목 라벨
	JLabel helpButton; // 도움말 버튼
	JPanel helpPanel; // 도우미창을 활성화 시킬 패널
	JWindow helpWindow; // 설명창
	JWindow micWindow; // 음성 입력창
	JTextArea textArea; // 음성 인식 결과 출력 영역
	JScrollPane scrollPane;
	
	// 메인 화면에 표시할 결과 영역 추가
	JTextArea mainResultArea; // 메인 화면 결과 표시 영역
	JScrollPane mainResultScrollPane; // 메인 화면 결과 스크롤 패널
	
	// 로딩 및 결과 표시 창 추가
	JWindow resultWindow; // 결과 표시 창
	JLabel loadingLabel; // 로딩 애니메이션 라벨
	JLabel voiceResultLabel; // 음성 인식 결과 라벨
	JTextArea dbResultArea; // DB 조회 결과 영역
	JScrollPane dbResultScrollPane; // DB 결과 스크롤 패널
	
	// 녹음 관련 필드 추가
	private AudioRecorder audioRecorder;
	private boolean isRecording = false;
	private GoogleSpeechToText speechToText;
	
	// DB 조회 서비스
	private VoiceQueryService voiceQueryService;
	
	// TTS 관련 필드
	private GoogleTextToSpeech textToSpeech;
	private AudioPlayer audioPlayer;
	
	// 로딩 애니메이션 관련
	private javax.swing.Timer loadingTimer;
	private int loadingDots = 0;
	
	public MicrophoneForm(Object testmain) {
		super(testmain);
		
		// AudioRecorder 초기화
		this.audioRecorder = new AudioRecorder();
		this.speechToText = new GoogleSpeechToText();
		this.voiceQueryService = new VoiceQueryService();
		this.textToSpeech = new GoogleTextToSpeech();
		this.audioPlayer = new AudioPlayer();
		
		// 제목 라벨 생성
		titleLabel = new JLabel("🎤 음성 명령하기");
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		titleLabel.setForeground(new Color(52, 73, 94));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		// 마이크 버튼
		micButton = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				
				int width = getWidth();
				int height = getHeight();
				int centerX = width / 2;
				int centerY = height / 2;
				
				// 마이크 아이콘 그리기
				Image img = null;
				URL url = this.getClass().getClassLoader().getResource("images/mic.png");
				try {
					BufferedImage bufferImage = ImageIO.read(url);
					img = bufferImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (img != null) {
					int x = centerX - 16;
					int y = centerY - 16;
					g2.drawImage(img, x, y, this);
				}
				
				// 녹음 중일 때만 빨간색 원 표시
				if (isRecording) {
					g2.setColor(new Color(231, 76, 60));
					g2.setStroke(new java.awt.BasicStroke(2));
					g2.drawOval(centerX - 20, centerY - 20, 40, 40);
				}
			}
		};
		
		// 마이크 버튼 스타일링
		micButton.setPreferredSize(new Dimension(60, 60));
		micButton.setFocusPainted(false);
		micButton.setBorderPainted(false);
		micButton.setContentAreaFilled(false);
		micButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		// 도움말 버튼 생성
		helpButton = new JLabel("?");
		helpButton.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		helpButton.setForeground(Color.WHITE);
		helpButton.setBackground(new Color(149, 165, 166));
		helpButton.setOpaque(true);
		helpButton.setHorizontalAlignment(JLabel.CENTER);
		helpButton.setPreferredSize(new Dimension(20, 20));
		helpButton.setMinimumSize(new Dimension(20, 20));
		helpButton.setMaximumSize(new Dimension(20, 20));
		helpButton.setBorder(BorderFactory.createEmptyBorder());
		helpButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		// 도움말 패널
		helpPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// 둥근 모서리 배경
				g2.setColor(new Color(149, 165, 166));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			}
		};
		helpPanel.setLayout(new BorderLayout());
		helpPanel.add(helpButton, BorderLayout.CENTER);
		helpPanel.setPreferredSize(new Dimension(20, 20));
		helpPanel.setMinimumSize(new Dimension(20, 20));
		helpPanel.setMaximumSize(new Dimension(20, 20));
		helpPanel.setOpaque(false);
		
		// 도움말 창 내용
		JLabel helpContent = new JLabel("<html><body style='width:280px; padding:15px;'>"
				+ "<div style='font-size:14px; font-weight:bold; color:#2c3e50; margin-bottom:10px;'>🎤 음성 창고 조회 사용법</div>"
				+ "<div style='font-size:12px; color:#34495e; line-height:1.5;'>"
				+ "<b>📋 사용 가능한 명령어:</b><br>"
				+ "• '재고 조회' - 전체 재고 현황<br>"
				+ "• '입고 내역' - 최근 입고 현황<br>"
				+ "• '출고 내역' - 최근 출고 현황<br>"
				+ "• '브랜드 조회' - 브랜드별 현황<br>"
				+ "• '위치 확인' - 창고 위치 정보<br><br>"
				+ "<b>🔄 사용 방법:</b><br>"
				+ "1. 마이크 버튼 클릭<br>"
				+ "2. 명령어 말하기<br>"
				+ "3. 다시 클릭하여 녹음 중지<br>"
				+ "4. 결과 확인 및 음성 안내"
				+ "</div></body></html>");
		helpContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		helpContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// 도움말 창
		helpWindow = new JWindow();
		helpWindow.setLayout(new BorderLayout());
		helpWindow.add(helpContent);
		helpWindow.setAlwaysOnTop(true);
		helpWindow.setBackground(new Color(248, 249, 250));
		helpWindow.getRootPane().setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
			)
		);
		helpWindow.pack();
		
		// 음성 입력 결과 창
		textArea = new JTextArea();
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		textArea.setBackground(new Color(248, 249, 250));
		textArea.setForeground(new Color(52, 73, 94));
		textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 200));
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
		scrollPane.getViewport().setBackground(new Color(248, 249, 250));
		
		// 메인 화면 결과 표시 영역 초기화
		mainResultArea = new JTextArea();
		mainResultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		mainResultArea.setBackground(new Color(255, 255, 255));
		mainResultArea.setForeground(new Color(52, 73, 94));
		mainResultArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainResultArea.setLineWrap(true);
		mainResultArea.setWrapStyleWord(true);
		mainResultArea.setEditable(false);
		mainResultArea.setRows(3); // 3줄 높이로 설정
		
		mainResultScrollPane = new JScrollPane(mainResultArea);
		mainResultScrollPane.setPreferredSize(new Dimension(0, 80));
		mainResultScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		mainResultScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
		
		// 텍스트가 추가될 때 자동으로 스크롤을 맨 아래로 이동
		textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				SwingUtilities.invokeLater(() -> {
					textArea.setCaretPosition(textArea.getDocument().getLength());
				});
			}
			
			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {}
			
			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {}
		});
		
		// 메인 결과 영역에도 자동 스크롤 기능 추가
		mainResultArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				SwingUtilities.invokeLater(() -> {
					mainResultArea.setCaretPosition(mainResultArea.getDocument().getLength());
				});
			}
			
			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {}
			
			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {}
		});
		
		micWindow = new JWindow();
		micWindow.setLayout(new BorderLayout());
		micWindow.add(scrollPane, BorderLayout.CENTER);
		micWindow.setBackground(new Color(248, 249, 250));
		micWindow.pack();
		
		// 로딩 및 결과 표시 창 초기화
		initializeResultWindow();
		
		// 레이아웃 구성
		setLayout(new BorderLayout());
		setBackground(new Color(236, 240, 241));
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(189, 195, 199)),
			BorderFactory.createEmptyBorder(25, 25, 25, 25)
		));
		
		// 상단 제목과 도움말
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
		topPanel.setBackground(new Color(236, 240, 241));
		topPanel.add(titleLabel); // 제목 숨김
		topPanel.add(helpPanel);
		topPanel.add(micButton);
		
		// 중앙 결과 표시 영역
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(new Color(236, 240, 241));
		centerPanel.add(mainResultScrollPane, BorderLayout.CENTER); // 결과 영역 다시 활성화
		
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER); // 중앙 패널 다시 활성화
		
		// 이벤트 연결
		micButton.addActionListener(e -> {
			if (!isRecording) {
				// 녹음 시작
				startRecording();
			} else {
				// 녹음 중지
				stopRecording();
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
		
		setPreferredSize(new Dimension(0, 80));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
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
		
		// 8초 후 자동으로 닫기 (더 오래 표시)
		javax.swing.Timer timer = new javax.swing.Timer(8000, e -> micWindow.setVisible(false));
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
	
	/**
	 * 녹음 시작
	 */
	private void startRecording() {
		if (isRecording) {
			return;
		}
		
		// UI 상태 변경
		String recordingText = "🎤 녹음 중... 말씀해 주세요.\n(녹음 중지하려면 마이크 버튼을 다시 클릭하세요)";
		textArea.setText(recordingText);
		mainResultArea.setText(recordingText); // 메인 화면에도 표시
		showVoiceWindow();
		
		// 녹음 시작
		if (audioRecorder.startRecording()) {
			isRecording = true;
			System.out.println("🎙️ 녹음 시작됨");
		} else {
			System.err.println("❌ 녹음 시작 실패");
			String errorText = "❌ 녹음 시작에 실패했습니다.";
			textArea.setText(errorText);
			mainResultArea.setText(errorText); // 메인 화면에도 표시
		}
	}
	
	/**
	 * 녹음 중지
	 */
	private void stopRecording() {
		if (!isRecording) {
			return;
		}
		
		// 녹음 중지
		audioRecorder.stopRecording();
		isRecording = false;
		
		System.out.println("⏹️ 녹음 중지됨");
		
		// 파일 저장
		String filename = "voice_" + System.currentTimeMillis() + ".wav";
		audioRecorder.saveToWavFile(filename);
		
		String completionText = "✅ 녹음 완료!\n파일명: " + filename + "\n\n이제 STT 변환을 진행합니다...";
		textArea.setText(completionText);
		mainResultArea.setText(completionText); // 메인 화면에도 표시
		System.out.println("💾 녹음 파일 저장됨: " + filename);
		
		// STT 변환 실행
		performSTTConversion(filename);
	}
	
	/**
	 * STT 변환 수행
	 */
	private void performSTTConversion(String filename) {
		// 백그라운드에서 STT 변환 실행
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws Exception {
				// STT 서비스 초기화
				if (!speechToText.initialize()) {
					throw new Exception("STT 서비스를 초기화할 수 없습니다.");
				}
				
				// 음성 파일을 텍스트로 변환
				return speechToText.convertSpeechToText(filename);
			}
			
			@Override
			protected void done() {
				try {
					String result = get();
					
					if (result != null && !result.trim().isEmpty()) {
						String displayText = result;
						textArea.setText(displayText);
						mainResultArea.setText(displayText); // 메인 화면에도 표시
						System.out.println("✅ STT 변환 완료: " + result);
						
						// 결과 창 표시 및 DB 조회 시작
						SwingUtilities.invokeLater(() -> {
							textArea.setCaretPosition(textArea.getDocument().getLength());
							mainResultArea.setCaretPosition(mainResultArea.getDocument().getLength());
							
							// 기존 팝업 창들 숨기기
							micWindow.setVisible(false);
							helpWindow.setVisible(false);
							
							// 결과 창 표시
							showResultWindow();
							
							// DB 조회 수행
							performDatabaseQuery(result);
						});
					} else {
						String errorText = "⚠️ 음성을 인식할 수 없습니다.\n다시 시도해보세요.";
						textArea.setText(errorText);
						mainResultArea.setText(errorText); // 메인 화면에도 표시
						System.out.println("⚠️ STT 변환 실패: 음성 인식 불가");
						
						// 오류 메시지도 창에 표시
						SwingUtilities.invokeLater(() -> {
							textArea.setCaretPosition(textArea.getDocument().getLength());
							mainResultArea.setCaretPosition(mainResultArea.getDocument().getLength());
							showVoiceWindow();
						});
					}
					
				} catch (Exception e) {
					String errorText = "❌ STT 변환 중 오류 발생: " + e.getMessage();
					textArea.setText(errorText);
					mainResultArea.setText(errorText); // 메인 화면에도 표시
					System.err.println("❌ STT 변환 오류: " + e.getMessage());
					
					// 오류 메시지도 창에 표시
					SwingUtilities.invokeLater(() -> {
						textArea.setCaretPosition(textArea.getDocument().getLength());
						mainResultArea.setCaretPosition(mainResultArea.getDocument().getLength());
						showVoiceWindow();
					});
				}
			}
		};
		
		worker.execute();
	}
	
	/**
	 * 로딩 및 결과 표시 창 초기화
	 */
	private void initializeResultWindow() {
		// 로딩 및 결과 표시 창 초기화
		resultWindow = new JWindow();
		resultWindow.setLayout(new BorderLayout());
		
		// 로딩 애니메이션 라벨 (텍스트 기반)
		loadingLabel = new JLabel("🔄 DB 조회 중...");
		loadingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		loadingLabel.setForeground(new Color(52, 73, 94));
		loadingLabel.setHorizontalAlignment(JLabel.CENTER);
		loadingLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		loadingLabel.setBackground(new Color(255, 255, 255));
		loadingLabel.setOpaque(true);
		
		// 음성 인식 결과 라벨
		voiceResultLabel = new JLabel("음성 인식 결과:");
		voiceResultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		voiceResultLabel.setForeground(new Color(52, 73, 94));
		voiceResultLabel.setHorizontalAlignment(JLabel.CENTER);
		voiceResultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		voiceResultLabel.setBackground(new Color(255, 255, 255));
		voiceResultLabel.setOpaque(true);
		
		// DB 조회 결과 영역
		dbResultArea = new JTextArea();
		dbResultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		dbResultArea.setBackground(new Color(255, 255, 255));
		dbResultArea.setForeground(new Color(52, 73, 94));
		dbResultArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		dbResultArea.setLineWrap(true);
		dbResultArea.setWrapStyleWord(true);
		dbResultArea.setEditable(false);
		
		dbResultScrollPane = new JScrollPane(dbResultArea);
		dbResultScrollPane.setPreferredSize(new Dimension(500, 200));
		dbResultScrollPane.setMinimumSize(new Dimension(400, 150));
		dbResultScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
		dbResultScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		
		// 초기에는 로딩 라벨만 표시
		resultWindow.add(loadingLabel, BorderLayout.CENTER);
		
		resultWindow.setAlwaysOnTop(true);
		resultWindow.setBackground(new Color(255, 255, 255));
		resultWindow.setSize(600, 300); // 명시적 크기 설정
		
		// 창에 테두리 추가
		resultWindow.getRootPane().setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
			)
		);
		
		resultWindow.pack();
		
		// 로딩 애니메이션 타이머 초기화
		loadingTimer = new javax.swing.Timer(500, e -> {
			loadingDots = (loadingDots + 1) % 4;
			String dots = ".".repeat(loadingDots);
			loadingLabel.setText("🔄 DB 조회 중" + dots);
		});
	}
	
	/**
	 * 결과 창 표시 (화면 중앙)
	 */
	private void showResultWindow() {
		// 화면 중앙에 표시
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - resultWindow.getWidth()) / 2;
		int y = (screenSize.height - resultWindow.getHeight()) / 2;
		resultWindow.setLocation(x, y);
		
		// 창을 최상위로 설정하고 포커스
		resultWindow.setAlwaysOnTop(true);
		resultWindow.toFront();
		resultWindow.requestFocus();
		resultWindow.setVisible(true);
		
		System.out.println("🪟 결과 창 표시됨 - 위치: (" + x + ", " + y + ")");
		
		// 로딩 애니메이션 시작
		loadingTimer.start();
	}
	
	/**
	 * 결과 창 숨기기
	 */
	private void hideResultWindow() {
		loadingTimer.stop();
		System.out.println("⏹️ 로딩 애니메이션 중지됨");
		System.out.println("타이머 실행 상태: " + loadingTimer.isRunning());
		resultWindow.setVisible(false);
	}
	
	/**
	 * DB 조회 수행
	 */
	private void performDatabaseQuery(String voiceText) {
		// 백그라운드에서 DB 조회 실행
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws Exception {
				// 잠시 대기 (로딩 애니메이션 효과)
				Thread.sleep(2000);
				
				// 음성 텍스트를 기반으로 DB 조회 로직
				return queryDatabaseByVoiceText(voiceText);
			}
			
			@Override
			protected void done() {
				try {
					String result = get();
					
					System.out.println("🎯 DB 조회 완료, 결과 창 업데이트 시작");
					System.out.println("생성된 결과: " + result.substring(0, Math.min(100, result.length())) + "...");
					
					// 로딩 애니메이션 중지
					loadingTimer.stop();
					System.out.println("⏹️ 로딩 애니메이션 중지됨");
					System.out.println("타이머 실행 상태: " + loadingTimer.isRunning());
					
					// 기존 결과 창 숨기기
					resultWindow.setVisible(false);
					
					// 새로운 결과 창 생성
					JWindow newResultWindow = new JWindow();
					newResultWindow.setLayout(new BorderLayout());
					newResultWindow.setAlwaysOnTop(true);
					newResultWindow.setBackground(new Color(255, 255, 255));
					newResultWindow.setSize(600, 300);
					
					// 창에 테두리 추가
					newResultWindow.getRootPane().setBorder(
						BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)
						)
					);
					
					// 음성 인식 결과 라벨 새로 생성
					JLabel newVoiceLabel = new JLabel("음성 인식: " + voiceText);
					newVoiceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
					newVoiceLabel.setForeground(new Color(52, 73, 94));
					newVoiceLabel.setHorizontalAlignment(JLabel.CENTER);
					newVoiceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					newVoiceLabel.setBackground(new Color(255, 255, 255));
					newVoiceLabel.setOpaque(true);
					
					// DB 결과 영역 새로 생성
					JTextArea newDbResultArea = new JTextArea(result);
					newDbResultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
					newDbResultArea.setBackground(new Color(255, 255, 255));
					newDbResultArea.setForeground(new Color(52, 73, 94));
					newDbResultArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
					newDbResultArea.setLineWrap(true);
					newDbResultArea.setWrapStyleWord(true);
					newDbResultArea.setEditable(false);
					
					JScrollPane newDbScrollPane = new JScrollPane(newDbResultArea);
					newDbScrollPane.setPreferredSize(new Dimension(500, 200));
					newDbScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
					
					// 새 창에 컴포넌트 추가
					newResultWindow.add(newVoiceLabel, BorderLayout.NORTH);
					newResultWindow.add(newDbScrollPane, BorderLayout.CENTER);
					
					System.out.println("🆕 새로운 결과 창 생성됨");
					System.out.println("새 창 컴포넌트 수: " + newResultWindow.getComponentCount());
					
					// 새 창 표시
					newResultWindow.pack();
					newResultWindow.revalidate();
					newResultWindow.repaint();
					
					// 화면 중앙에 표시
					Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
					int x = (screenSize.width - newResultWindow.getWidth()) / 2;
					int y = (screenSize.height - newResultWindow.getHeight()) / 2;
					newResultWindow.setLocation(x, y);
					newResultWindow.setVisible(true);
					newResultWindow.toFront();
					newResultWindow.requestFocus();
					
					System.out.println("✅ 새로운 결과 창 표시됨 - 위치: (" + x + ", " + y + ")");
					System.out.println("새 창 크기: " + newResultWindow.getWidth() + " x " + newResultWindow.getHeight());
					System.out.println("새 창 표시 상태: " + newResultWindow.isVisible());
					
					// TTS로 결과 읽어주기
					performTTS(result, newResultWindow);
					
					// 60초 후 자동으로 닫기 (긴 TTS 재생 시간 고려)
					javax.swing.Timer closeTimer = new javax.swing.Timer(60000, evt -> newResultWindow.setVisible(false));
					closeTimer.setRepeats(false);
					closeTimer.start();
					
				} catch (Exception e) {
					System.err.println("❌ 결과 창 업데이트 중 오류: " + e.getMessage());
					e.printStackTrace();
					
					// 오류 발생 시
					loadingTimer.stop();
					resultWindow.removeAll();
					resultWindow.setLayout(new BorderLayout());
					
					JLabel errorLabel = new JLabel("❌ DB 조회 중 오류 발생: " + e.getMessage());
					errorLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
					errorLabel.setForeground(new Color(231, 76, 60));
					errorLabel.setHorizontalAlignment(JLabel.CENTER);
					errorLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
					
					resultWindow.add(errorLabel, BorderLayout.CENTER);
					resultWindow.pack();
					resultWindow.revalidate();
					resultWindow.repaint();
					
					// 5초 후 자동으로 닫기
					javax.swing.Timer closeTimer = new javax.swing.Timer(5000, evt -> hideResultWindow());
					closeTimer.setRepeats(false);
					closeTimer.start();
				}
			}
		};
		
		worker.execute();
	}
	
	/**
	 * 음성 텍스트를 기반으로 DB 조회
	 */
	private String queryDatabaseByVoiceText(String voiceText) {
		System.out.println("🔍 실제 DB 조회 시작: " + voiceText);
		
		// VoiceQueryService를 사용하여 실제 DB 조회 수행
		String result = voiceQueryService.processVoiceQuery(voiceText);
		
		System.out.println("📋 DB 조회 완료");
		System.out.println("결과 길이: " + result.length());
		
		return result;
	}
	
	/**
	 * TTS로 결과 읽어주기
	 */
	private void performTTS(String text, JWindow windowToClose) {
		// 백그라운드에서 TTS 수행
		SwingWorker<Void, Void> ttsWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					System.out.println("🔊 TTS 시작: " + text.substring(0, Math.min(50, text.length())) + "...");
					
					// TTS 서비스 초기화
					if (!textToSpeech.initialize()) {
						System.err.println("❌ TTS 서비스 초기화 실패");
						return null;
					}
					
					// 텍스트를 음성으로 변환하여 파일 저장
					String audioFile = textToSpeech.convertTextToSpeech(text);
					
					if (audioFile != null) {
						System.out.println("✅ TTS 변환 완료: " + audioFile);
						
						// 오디오 재생 (재생 완료까지 대기)
						java.io.File file = new java.io.File(audioFile);
						if (audioPlayer.loadAudio(file)) {
							audioPlayer.playAndWait(); // 재생 완료까지 대기
							System.out.println("🎵 TTS 재생 완료");
						} else {
							System.err.println("❌ 오디오 파일 로드 실패");
						}
					} else {
						System.err.println("❌ TTS 변환 실패");
					}
					
				} catch (Exception e) {
					System.err.println("❌ TTS 처리 중 오류: " + e.getMessage());
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void done() {
				System.out.println("🔊 TTS 작업 완료");
				// TTS 재생이 완료되면 2초 후 창 닫기
				javax.swing.Timer delayedCloseTimer = new javax.swing.Timer(2000, evt -> {
					if (windowToClose != null) {
						windowToClose.setVisible(false);
						System.out.println("✅ TTS 완료 후 결과 창 자동 닫기");
					}
				});
				delayedCloseTimer.setRepeats(false);
				delayedCloseTimer.start();
			}
		};
		
		ttsWorker.execute();
	}
}
