package com.sinse.tory.tts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Google TTS를 사용한 텍스트-음성 변환 프로그램
 * 텍스트 입력 → Google TTS 음성 변환 → 오디오 재생
 */
public class TextToSpeechDemo extends JFrame {
    
    private JTextArea textArea;
    private JButton speakButton;
    private JButton clearButton;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    
    // TTS 관련 컴포넌트
    private GoogleTextToSpeech textToSpeech;
    private AudioPlayer audioPlayer;
    private String lastGeneratedFile;
    
    public TextToSpeechDemo() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        initializeTTSService();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // 기본 Look and Feel 사용
            }
            
            
            new TextToSpeechDemo().setVisible(true);
        });
    }
    
    /**
     * UI 컴포넌트 초기화
     */
    private void initializeComponents() {
        setTitle("🔊 텍스트 음성 변환 (TTS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        // 텍스트 입력 영역
        textArea = new JTextArea(10, 40);
        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createTitledBorder("📝 변환할 텍스트를 입력하세요"));
        textArea.setText("안녕하세요! 텍스트를 음성으로 변환하는 프로그램입니다.");
        
        // 버튼들
        speakButton = new JButton("🔊 음성 변환");
        speakButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        speakButton.setBackground(new Color(173, 216, 230)); // 연한 파란색 배경
        speakButton.setForeground(Color.BLACK); // 검정색 글씨
        speakButton.setFocusPainted(false);
        speakButton.setPreferredSize(new Dimension(150, 40));
        
        clearButton = new JButton("🗑️ 텍스트 지우기");
        clearButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        clearButton.setBackground(new Color(220, 220, 220)); // 연한 회색 배경
        clearButton.setForeground(Color.BLACK); // 검정색 글씨
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(150, 40));
        
        // 오디오 컨트롤 버튼들
        playButton = new JButton("▶️ 재생");
        playButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        playButton.setBackground(new Color(144, 238, 144)); // 연한 녹색 배경
        playButton.setForeground(Color.BLACK);
        playButton.setFocusPainted(false);
        playButton.setPreferredSize(new Dimension(100, 40));
        playButton.setEnabled(false);
        
        pauseButton = new JButton("⏸️ 일시정지");
        pauseButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        pauseButton.setBackground(new Color(255, 218, 185)); // 연한 주황색 배경
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFocusPainted(false);
        pauseButton.setPreferredSize(new Dimension(120, 40));
        pauseButton.setEnabled(false);
        
        stopButton = new JButton("⏹️ 정지");
        stopButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        stopButton.setBackground(new Color(255, 182, 193)); // 연한 빨강색 배경
        stopButton.setForeground(Color.BLACK);
        stopButton.setFocusPainted(false);
        stopButton.setPreferredSize(new Dimension(100, 40));
        stopButton.setEnabled(false);
        
        // 상태 표시
        statusLabel = new JLabel("📋 텍스트를 입력하고 '음성 변환' 버튼을 클릭하세요.");
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        
        // 진행률 표시
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("대기 중...");
        progressBar.setVisible(false);
    }
    
    /**
     * 레이아웃 설정
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // 헤더 패널
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("🔊 텍스트 음성 변환 (TTS)", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 58, 64));
        headerPanel.add(titleLabel);
        
        // 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 텍스트 입력 패널
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        
        // TTS 버튼 패널
        JPanel ttsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        ttsButtonPanel.setBorder(BorderFactory.createTitledBorder("텍스트 변환"));
        ttsButtonPanel.add(speakButton);
        ttsButtonPanel.add(clearButton);
        
        // 오디오 컨트롤 패널
        JPanel audioControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        audioControlPanel.setBorder(BorderFactory.createTitledBorder("오디오 컨트롤"));
        audioControlPanel.add(playButton);
        audioControlPanel.add(pauseButton);
        audioControlPanel.add(stopButton);
        
        buttonPanel.add(ttsButtonPanel, BorderLayout.NORTH);
        buttonPanel.add(audioControlPanel, BorderLayout.SOUTH);
        
        // 상태 패널
        JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
        statusPanel.add(statusLabel, BorderLayout.NORTH);
        statusPanel.add(progressBar, BorderLayout.SOUTH);
        
        // 메인 패널에 컴포넌트 추가
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 프레임에 패널 추가
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 이벤트 핸들러 설정
     */
    private void setupEventHandlers() {
        // 음성 변환 버튼
        speakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTextToSpeech();
            }
        });
        
        // 텍스트 지우기 버튼
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
            }
        });
        
        // 오디오 컨트롤 버튼들
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playLastAudio();
            }
        });
        
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseAudio();
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAudio();
            }
        });
        
        // Enter 키로 음성 변환 실행 (Ctrl+Enter)
        textArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl ENTER"), "speak");
        textArea.getActionMap().put("speak", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTextToSpeech();
            }
        });
    }
    
    /**
     * 텍스트를 음성으로 변환
     */
    private void performTextToSpeech() {
        String text = textArea.getText().trim();
        
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "변환할 텍스트를 입력해주세요!", 
                "입력 오류", 
                JOptionPane.WARNING_MESSAGE);
            textArea.requestFocus();
            return;
        }
        
        // UI 상태 업데이트
        speakButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        progressBar.setString("음성 변환 중...");
        statusLabel.setText("🔄 Google TTS로 음성을 생성하고 있습니다...");
        
        // 별도 스레드에서 TTS 수행 (UI 블로킹 방지)
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Google TTS API 호출
                if (textToSpeech != null && textToSpeech.isInitialized()) {
                    String generatedFile = textToSpeech.convertTextToSpeech(text);
                    if (generatedFile != null) {
                        lastGeneratedFile = generatedFile;
                    } else {
                        throw new Exception("음성 파일 생성에 실패했습니다.");
                    }
                } else {
                    throw new Exception("TTS 서비스가 초기화되지 않았습니다.");
                }
                return null;
            }
            
            @Override
            protected void done() {
                // UI 상태 복구
                speakButton.setEnabled(true);
                progressBar.setVisible(false);
                progressBar.setIndeterminate(false);
                
                try {
                    get(); // 예외 확인
                    statusLabel.setText("✅ 음성 변환 및 재생이 완료되었습니다!");
                    
                    // 성공 메시지
                    JOptionPane.showMessageDialog(TextToSpeechDemo.this,
                        "음성 변환이 완료되고 재생이 시작되었습니다!\n" +
                        "시스템 기본 플레이어 또는 명령어로 재생됩니다.",
                        "변환 및 재생 완료",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // 오디오 컨트롤 버튼 활성화
                    enableAudioControls(true);
                        
                } catch (Exception e) {
                    statusLabel.setText("❌ 음성 변환 중 오류가 발생했습니다.");
                    JOptionPane.showMessageDialog(TextToSpeechDemo.this,
                        "음성 변환 중 오류가 발생했습니다:\n" + e.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * 텍스트 영역 지우기
     */
    private void clearText() {
        textArea.setText("");
        textArea.requestFocus();
        statusLabel.setText("📋 텍스트를 입력하고 '음성 변환' 버튼을 클릭하세요.");
    }
    
    /**
     * TTS 서비스 초기화
     */
    private void initializeTTSService() {
        // 별도 스레드에서 TTS 서비스 초기화 (UI 블로킹 방지)
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                textToSpeech = new GoogleTextToSpeech();
                return textToSpeech.initialize();
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setText("✅ Google TTS 서비스가 준비되었습니다.");
                        speakButton.setEnabled(true);
                    } else {
                        statusLabel.setText("❌ Google TTS 서비스 초기화에 실패했습니다.");
                        speakButton.setEnabled(false);
                        
                        JOptionPane.showMessageDialog(TextToSpeechDemo.this,
                            "Google Cloud TTS 서비스를 초기화할 수 없습니다.\n" +
                            "API 키와 인증 정보를 확인해주세요.",
                            "서비스 초기화 실패",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    statusLabel.setText("❌ TTS 서비스 초기화 중 오류가 발생했습니다.");
                    speakButton.setEnabled(false);
                    
                    JOptionPane.showMessageDialog(TextToSpeechDemo.this,
                        "TTS 서비스 초기화 중 오류:\n" + e.getMessage(),
                        "초기화 오류",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        // 초기화 시작
        statusLabel.setText("🔄 Google TTS 서비스를 초기화하고 있습니다...");
        speakButton.setEnabled(false);
        worker.execute();
    }
    
    /**
     * 마지막으로 생성된 오디오 파일 재생
     */
    private void playLastAudio() {
        if (lastGeneratedFile == null || lastGeneratedFile.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "재생할 오디오 파일이 없습니다.\n먼저 텍스트를 음성으로 변환해주세요!", 
                "재생 오류", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (audioPlayer == null) {
            // AudioPlayer 초기화
            AudioPlayer.initializeJavaFX();
            audioPlayer = new AudioPlayer();
        }
        
        File audioFile = new File(lastGeneratedFile);
        if (!audioFile.exists()) {
            JOptionPane.showMessageDialog(this, 
                "오디오 파일을 찾을 수 없습니다: " + lastGeneratedFile, 
                "파일 오류", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean loaded = audioPlayer.loadAudio(audioFile);
        if (loaded) {
            audioPlayer.play();
            statusLabel.setText("▶️ 오디오 재생 중...");
        } else {
            JOptionPane.showMessageDialog(this, 
                "오디오 파일 로드에 실패했습니다.", 
                "재생 오류", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 오디오 일시정지
     */
    private void pauseAudio() {
        if (audioPlayer != null) {
            audioPlayer.pause();
            statusLabel.setText("⏸️ 오디오 일시정지됨");
        }
    }
    
    /**
     * 오디오 정지
     */
    private void stopAudio() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            statusLabel.setText("⏹️ 오디오 정지됨");
        }
    }
    
    /**
     * 오디오 컨트롤 버튼 활성화/비활성화
     */
    private void enableAudioControls(boolean enabled) {
        playButton.setEnabled(enabled);
        pauseButton.setEnabled(enabled);
        stopButton.setEnabled(enabled);
    }
    
    /**
     * 프로그램 종료 시 리소스 정리
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (audioPlayer != null) {
                audioPlayer.dispose();
            }
            if (textToSpeech != null) {
                textToSpeech.cleanup();
            }
        } finally {
            super.finalize();
        }
    }
} 