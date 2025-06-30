package com.sinse.tory.stt;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Google Cloud Speech-to-Text API를 사용한 실제 STT 구현체
 */
public class GoogleSpeechToText implements SpeechToTextInterface {
    
    private SpeechClient speechClient;
    private boolean isInitialized = false;
    
    // 음성 인식 설정
    private static final RecognitionConfig RECOGNITION_CONFIG = RecognitionConfig.newBuilder()
        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
        .setSampleRateHertz(16000)
        .setLanguageCode("ko-KR")  // 한국어 설정
        .addAlternativeLanguageCodes("en-US")  // 영어도 지원
        .setEnableAutomaticPunctuation(true)   // 자동 구두점
        .setModel("latest_long")               // 최신 모델 사용
        .build();
    
    @Override
    public boolean initialize() {
        try {
            System.out.println("🌐 Speech-to-Text API를 초기화합니다...");
            
            // 현재 작업 디렉토리 출력 (디버깅용)
            String currentDir = System.getProperty("user.dir");
            System.out.println("📁 현재 작업 디렉토리: " + currentDir);
            
            // 고정된 JSON 키 파일 경로 사용
            String credentialsPath = "./keys/google-stt-key.json";
            Path absolutePath = Paths.get(credentialsPath).toAbsolutePath();
            
            // 인증 파일 존재 확인
            if (!Files.exists(Paths.get(credentialsPath))) {
                System.err.println("❌ 인증 파일을 찾을 수 없습니다: " + credentialsPath);
                System.err.println("📋 필요한 것: 서비스 계정 키 (OAuth 클라이언트 ID 아님)");
                return false;
            }
            
            System.out.println("🔑 인증 파일: " + absolutePath.toString());
            
            // JSON 파일에서 직접 GoogleCredentials 로드
            GoogleCredentials credentials;
            try (FileInputStream serviceAccountStream = new FileInputStream(absolutePath.toFile())) {
                credentials = GoogleCredentials.fromStream(serviceAccountStream);
            }
            
            // SpeechClient를 credentials와 함께 생성
            SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();
            
            speechClient = SpeechClient.create(speechSettings);
            isInitialized = true;
            
            System.out.println("🔧 설정: 한국어 인식, 16kHz, 자동 구두점, 최신 모델");
            
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ API 초기화 실패: " + e.getMessage());
            if (e.getMessage().contains("invalid_grant") || e.getMessage().contains("private_key")) {
                System.err.println("💡 JSON 파일이 올바른 서비스 계정 키인지 확인해주세요.");
                System.err.println("   OAuth 클라이언트 ID가 아닌 서비스 계정 키가 필요합니다.");
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ 예상치 못한 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String convertSpeechToText(String audioFilePath) throws Exception {
        if (!isInitialized) {
            throw new IllegalStateException("STT 서비스가 초기화되지 않았습니다.");
        }
        
        try {
            System.out.println("\n🎵 음성 파일 분석 시작: " + audioFilePath);
            
            // 오디오 파일 읽기
            Path path = Paths.get(audioFilePath);
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("오디오 파일을 찾을 수 없습니다: " + audioFilePath);
            }
            
            byte[] audioData = Files.readAllBytes(path);
            System.out.println("📁 파일 크기: " + (audioData.length / 1024) + " KB");
            
            return performSpeechRecognition(audioData, audioFilePath);
            
        } catch (IOException e) {
            throw new Exception("파일 읽기 오류: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String convertSpeechToText(byte[] audioData) throws Exception {
        if (!isInitialized) {
            throw new IllegalStateException("STT 서비스가 초기화되지 않았습니다.");
        }
        
        if (audioData == null || audioData.length == 0) {
            throw new IllegalArgumentException("음성 데이터가 유효하지 않습니다.");
        }
        
        System.out.println("\n🎵 음성 데이터 분석 시작 (크기: " + (audioData.length / 1024) + " KB)");
        
        return performSpeechRecognition(audioData, "메모리 데이터");
    }
    
    /**
     * 실제 Google STT API를 사용한 음성 인식 수행
     */
    private String performSpeechRecognition(byte[] audioData, String sourceInfo) throws Exception {
        try {
            System.out.println("🔄 STT 처리 중...");
            
            // 오디오 데이터를 ByteString으로 변환
            ByteString audioBytes = ByteString.copyFrom(audioData);
            
            // RecognitionAudio 객체 생성
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(audioBytes)
                .build();
            
            // Google STT API 호출
            RecognizeResponse response = speechClient.recognize(RECOGNITION_CONFIG, audio);
            
            // 결과 처리
            if (response.getResultsList().isEmpty()) {
                System.out.println("⚠️ 음성을 인식할 수 없습니다. (무음이거나 인식 불가능한 음성)");
                return "";
            }
            
            // 가장 높은 신뢰도의 결과 선택
            SpeechRecognitionResult result = response.getResults(0);
            SpeechRecognitionAlternative alternative = result.getAlternatives(0);
            
            float confidence = alternative.getConfidence();
            String transcript = alternative.getTranscript();
            
            // 결과 출력
            System.out.println("\n" + "🎉".repeat(20));
            System.out.println("✅ 음성 인식 완료!");
            System.out.println("📊 신뢰도: " + String.format("%.1f", confidence * 100) + "%");
            System.out.println("📝 인식된 텍스트: \"" + transcript + "\"");
            System.out.println("📍 소스: " + sourceInfo);
            System.out.println("🎉".repeat(20));
            
            return transcript;
            
        } catch (Exception e) {
            System.err.println("❌ API 호출 오류: " + e.getMessage());
            throw new Exception("STT 처리 실패: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isAvailable() {
        return isInitialized && speechClient != null;
    }
    
    @Override
    public void cleanup() {
        if (speechClient != null) {
            try {
                speechClient.close();
                System.out.println("🔚 STT 클라이언트를 종료했습니다.");
            } catch (Exception e) {
                System.err.println("⚠️ STT 클라이언트 종료 중 오류: " + e.getMessage());
            }
        }
        
        isInitialized = false;
    }
}
