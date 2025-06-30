package com.sinse.tory.stt;

import java.util.Scanner;

/**
 * Google STT 전용 음성 인식 프로그램
 * 녹음 시작 → Google STT 음성 인식 → 콘솔 출력
 */
public class VoiceRecognitionDemo {
    
    private AudioRecorder audioRecorder;
    private GoogleSpeechToText speechToText;
    private Scanner scanner;
    
    public VoiceRecognitionDemo() {
        this.audioRecorder = new AudioRecorder();
        this.scanner = new Scanner(System.in);
        this.speechToText = new GoogleSpeechToText();
    }
    
    public static void main(String[] args) {
        VoiceRecognitionDemo demo = new VoiceRecognitionDemo();
        demo.run();
    }
    
    public void run() {
        System.out.println("🎤 음성 인식 프로그램");
        System.out.println("=".repeat(50));
        
        // Google STT 서비스 초기화
//        if (!speechToText.initialize()) {
//            System.err.println("❌ 서비스 초기화에 실패했습니다.");
//            return;
//        }
        
        // 사용법 안내
        showUsageGuide();
        
        // 무한 루프로 계속 음성 인식 수행
        while (true) {
            performVoiceRecognition();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.print("🔄 다시 음성 인식을 하시겠습니까? (Enter: 계속, 'q': 종료): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if ("q".equals(input) || "quit".equals(input) || "exit".equals(input)) {
                System.out.println("👋 프로그램을 종료합니다.");
                break;
            }
        }
        
        // 정리
        cleanup();
    }
    
    /**
     * 사용법 안내 출력
     */
    private void showUsageGuide() {
        System.out.println("💡 사용법:");
        System.out.println("   1. Enter를 누르면 녹음 시작");
        System.out.println("   2. 녹음 중 Enter를 누르면 수동으로 중단 가능");
        System.out.println("   3. 5초간 무음이 지속되면 자동으로 종료됩니다");
        System.out.println("   4. 프로그램을 종료하려면 'q'를 입력하세요");
        
        System.out.println("=".repeat(50));
    }
    
    /**
     * 음성 인식 수행
     */
    private void performVoiceRecognition() {
        System.out.println("\n🎙️ 음성 인식을 시작합니다!");
        System.out.print("녹음을 시작하려면 Enter를 누르세요...");
        scanner.nextLine();
        
        // 1단계: 녹음 시작
        System.out.println("\n📍 녹음 시작!");
        System.out.println("🗣️ 말씀하세요... (5초간 무음시 자동 종료)");
        System.out.println("⏹️ 녹음을 중단하려면 Enter를 누르세요!");
        System.out.println("=".repeat(30));
        
        if (audioRecorder.startRecording()) {
            // 녹음이 자동으로 중지되거나 사용자가 수동으로 중단할 때까지 대기
            waitForRecordingToComplete();
            
            // 2단계: 파일 저장
            System.out.println("\n📍 음성 파일 저장 중...");
            String filename = "voice_" + System.currentTimeMillis() + ".wav";
            
            // 파일 저장 완료
            audioRecorder.saveToWavFile(filename);
//            if (audioRecorder.saveToWavFile(filename)) {
//                // 3단계: Google STT 변환 및 결과 출력
//                performSTTConversion(filename);
//            } else {
//                System.err.println("❌ 음성 파일 저장에 실패했습니다.");
//            }
        } else {
            System.err.println("❌ 녹음 시작에 실패했습니다.");
        }
    }
    
    /**
     * Google STT 변환 수행
     */
    private void performSTTConversion(String filename) {
        System.out.println("\n📍 음성을 텍스트로 변환 중...");
        
        try {
            speechToText.convertSpeechToText(filename);
            // Google STT는 자체적으로 결과를 출력하므로 추가 출력 불필요
            
        } catch (Exception e) {
            System.err.println("❌ 음성 인식 중 오류 발생: " + e.getMessage());
        }
    }
    
    /**
     * 녹음 완료까지 대기 (사용자가 Enter를 누르면 수동 중단 가능)
     */
    private void waitForRecordingToComplete() {
        // 별도 스레드에서 사용자 입력 감지
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read(); // Enter 키 입력 대기
                if (audioRecorder.isRecording()) {
                    System.out.println("\n⏹️ 사용자가 녹음을 중단했습니다.");
                    audioRecorder.stopRecording();
                }
            } catch (Exception e) {
                // 입력 대기 중 오류 무시
            }
        });
        
        inputThread.setDaemon(true); // 메인 스레드 종료시 같이 종료
        inputThread.start();
        
        try {
            // 녹음이 진행 중인 동안 대기 (자동 중단 또는 수동 중단)
            while (audioRecorder.isRecording()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ 녹음 대기 중 중단되었습니다.");
        }
        
        // 입력 스레드 정리
        inputThread.interrupt();
    }
    
    /**
     * 리소스 정리
     */
    private void cleanup() {
        if (audioRecorder != null) {
            audioRecorder.cleanup();
        }
        
        if (speechToText != null) {
            speechToText.cleanup();
        }
        
        if (scanner != null) {
            scanner.close();
        }
        
        System.out.println("✅ 프로그램이 정상적으로 종료되었습니다.");
    }
} 
