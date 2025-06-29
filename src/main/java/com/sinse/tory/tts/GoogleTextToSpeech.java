package com.sinse.tory.tts;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.gax.core.FixedCredentialsProvider;

// MP3 재생을 위한 import (JavaFX MediaPlayer 사용)
import java.awt.Desktop;

/**
 * Google Cloud Text-to-Speech API를 사용한 음성 변환 서비스
 * 
 * 이 클래스는 Google Cloud TTS API를 사용하여 텍스트를 음성으로 변환하고,
 * 생성된 MP3 파일을 시스템 기본 플레이어로 재생할 수 있습니다.
 * try-with-resources 패턴을 사용하여 리소스를 안전하게 관리합니다.
 */
public class GoogleTextToSpeech {
    
    private TextToSpeechClient textToSpeechClient;
    private boolean isInitialized = false;
    private AudioPlayer audioPlayer;
    
    /**
     * Google TTS 서비스 초기화
     * 
     * 프로젝트 내 서비스 계정 키 파일을 직접 사용하여 TextToSpeechClient를 생성합니다.
     * 환경변수가 없어도 IDE의 RUN 버튼으로 바로 실행할 수 있습니다.
     * 
     * @return 초기화 성공 여부
     */
    public boolean initialize() {
        try {
            System.out.println("🔄 Google TTS 서비스 초기화 중...");
            
            // 1. 먼저 환경변수 확인
            String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                // 환경변수가 있으면 기본 방식 사용
                System.out.println("✅ 환경변수에서 인증 정보 로드: " + credentialsPath);
                textToSpeechClient = TextToSpeechClient.create();
            } else {
                // 2. 환경변수가 없으면 프로젝트 내 키 파일 사용
                System.out.println("🔍 환경변수가 없습니다. 프로젝트 내 키 파일을 찾는 중...");
                
                // 프로젝트 내에서 JSON 키 파일 찾기
                String[] possibleKeyFiles = {
                    "keys/google-stt-key.json",                             // keys 폴더 (우선순위)
                    "stt-test-461905-5fc6be6e4e99.json",                    // 현재 디렉토리
                    "stt/stt-test-461905-5fc6be6e4e99.json",                // stt 하위 디렉토리
                    "./stt/stt-test-461905-5fc6be6e4e99.json",              // 명시적 상대 경로
                    "service-account-key.json",                              // 일반적인 이름
                    "stt/service-account-key.json",                          // stt 하위
                    "google-credentials.json",                               // 대안 이름
                    "stt/google-credentials.json"                            // stt 하위
                };
                
                File keyFile = null;
                for (String fileName : possibleKeyFiles) {
                    File file = new File(fileName);
                    System.out.println("🔍 키 파일 확인 중: " + file.getAbsolutePath() + " - 존재: " + file.exists());
                    if (file.exists()) {
                        keyFile = file;
                        break;
                    }
                }
                
                if (keyFile != null) {
                    System.out.println("✅ 키 파일 발견: " + keyFile.getAbsolutePath());
                    
                    // FileInputStream을 사용하여 인증 정보 로드
                    try (FileInputStream serviceAccountStream = new FileInputStream(keyFile)) {
                        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                            .createScoped("https://www.googleapis.com/auth/cloud-platform");
                        
                        // TextToSpeechSettings로 클라이언트 생성
                        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                            .build();
                        
                        textToSpeechClient = TextToSpeechClient.create(settings);
                        System.out.println("✅ 프로젝트 키 파일로 TTS 클라이언트 생성 완료!");
                    }
                } else {
                    System.err.println("❌ 인증 키 파일을 찾을 수 없습니다.");
                    System.err.println("💡 다음 중 하나를 수행하세요:");
                    System.err.println("   1. 환경변수 설정: export GOOGLE_APPLICATION_CREDENTIALS=\"/path/to/key.json\"");
                    System.err.println("   2. 프로젝트 루트에 키 파일 배치: stt-test-461905-5fc6be6e4e99.json");
                    return false;
                }
            }
            
            // 3. 연결 테스트
            testConnection();
            
            // 4. AudioPlayer 초기화
            AudioPlayer.initializeJavaFX();
            audioPlayer = new AudioPlayer();
            
            isInitialized = true;
            System.out.println("✅ Google TTS 서비스 초기화 완료!");
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Google TTS 서비스 초기화 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 연결 테스트를 위한 간단한 API 호출
     */
    private void testConnection() {
        try {
            // 한국어 음성 목록 조회로 연결 테스트
            ListVoicesResponse response = textToSpeechClient.listVoices("ko-KR");
            int voiceCount = response.getVoicesCount();
            System.out.println("📋 사용 가능한 한국어 음성: " + voiceCount + "개");
            
        } catch (Exception e) {
            System.err.println("⚠️ 연결 테스트 실패: " + e.getMessage());
        }
    }
    
    /**
     * 텍스트를 음성으로 변환하고 재생
     * 
     * @param text 변환할 텍스트
     * @return 생성된 파일명 (실패 시 null)
     */
    public String convertTextToSpeech(String text) {
        if (!isInitialized) {
            System.err.println("❌ TTS 서비스가 초기화되지 않았습니다.");
            return null;
        }
        
        if (text == null || text.trim().isEmpty()) {
            System.err.println("❌ 변환할 텍스트가 비어있습니다.");
            return null;
        }
        
        try {
            System.out.println("🔄 텍스트를 음성으로 변환 중: " + text);
            
            // 1. 텍스트 입력 설정 (공식 문서 예제 참고)
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();
            
            // 2. 음성 설정 (한국어 - 공식 문서 예제 참고)
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ko-KR")  // 한국어
                    .setSsmlGender(SsmlVoiceGender.FEMALE)  // 여성 음성
                    .setName("ko-KR-Standard-A")  // 한국어 표준 음성 A
                    .build();
            
            // 3. 오디오 설정 (공식 문서 예제 참고)
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)  // MP3 형식
                    .setSpeakingRate(1.0)  // 말하기 속도 (1.0 = 보통)
                    .setPitch(0.0)  // 음높이 (0.0 = 기본)
                    .setVolumeGainDb(0.0)  // 볼륨 (0.0 = 기본)
                    .build();
            
            // 4. TTS 요청 생성 및 실행 (공식 문서 예제 참고)
            SynthesizeSpeechRequest request = SynthesizeSpeechRequest.newBuilder()
                    .setInput(input)
                    .setVoice(voice)
                    .setAudioConfig(audioConfig)
                    .build();
            
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(request);
            
            // 5. 오디오 데이터 추출
            ByteString audioContents = response.getAudioContent();
            
            if (audioContents.isEmpty()) {
                System.err.println("❌ 음성 데이터가 생성되지 않았습니다.");
                return null;
            }
            
            System.out.println("✅ 음성 변환 완료! 크기: " + audioContents.size() + " bytes");
            
            // 6. 음성 파일 저장
            String filename = "tts_output_" + System.currentTimeMillis() + ".mp3";
            boolean saved = saveAudioToFile(audioContents, filename);
            
            // 7. 파일 저장 완료 후 파일명 반환 (재생은 호출자에서 처리)
            if (saved) {
                System.out.println("✅ TTS 파일 생성 완료: " + filename);
                return filename; // 생성된 파일명 반환
            } else {
                return null; // 저장 실패
            }
            
        } catch (Exception e) {
            System.err.println("❌ 음성 변환 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 오디오 데이터를 파일로 저장
     * 
     * @param audioContents 저장할 오디오 데이터
     * @param filename 저장할 파일명
     * @return 저장 성공 여부
     */
    private boolean saveAudioToFile(ByteString audioContents, String filename) {
        try (FileOutputStream out = new FileOutputStream(filename)) {
            out.write(audioContents.toByteArray());
            System.out.println("💾 음성 파일 저장: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("⚠️ 파일 저장 실패: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 오디오 파일 재생 (JavaFX MediaPlayer 사용)
     * 
     * @param audioContents 재생할 오디오 데이터
     * @param filename 저장된 파일명
     */
    private void playAudio(ByteString audioContents, String filename) {
        try {
            System.out.println("🔊 음성 재생 중... (파일: " + filename + ")");
            System.out.println("📊 오디오 크기: " + audioContents.size() + " bytes");
            
            File audioFile = new File(filename);
            
            // 1. 먼저 JavaFX MediaPlayer로 재생 시도
            if (audioPlayer != null) {
                boolean loaded = audioPlayer.loadAudio(audioFile);
                if (loaded) {
                    System.out.println("✅ JavaFX MediaPlayer로 재생을 시작합니다!");
                    audioPlayer.playAndWait();
                    return;
                } else {
                    System.out.println("⚠️ JavaFX MediaPlayer 로드 실패, 기본 플레이어로 폴백합니다.");
                }
            }
            
            // 2. JavaFX 실패 시 Desktop API로 폴백
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    try {
                        desktop.open(audioFile);
                        System.out.println("✅ 시스템 기본 플레이어로 재생을 시작했습니다!");
                        return;
                    } catch (Exception e) {
                        System.out.println("⚠️ 시스템 기본 플레이어 실행 실패: " + e.getMessage());
                    }
                }
            }
            
            // 3. Desktop API가 실패하면 OS별 명령어로 재생 시도
            String os = System.getProperty("os.name").toLowerCase();
            String[] command = null;
            
            if (os.contains("mac")) {
                // macOS: afplay 사용
                command = new String[]{"afplay", filename};
            } else if (os.contains("win")) {
                // Windows: start 사용
                command = new String[]{"cmd", "/c", "start", "", filename};
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux: 여러 플레이어 시도
                String[] players = {"mpg123", "mpv", "vlc", "mplayer"};
                for (String player : players) {
                    try {
                        command = new String[]{player, filename};
                        Process process = Runtime.getRuntime().exec(command);
                        System.out.println("✅ " + player + "로 재생을 시작했습니다!");
                        return;
                    } catch (Exception e) {
                        // 다음 플레이어 시도
                        continue;
                    }
                }
            }
            
            // 4. OS별 명령어 실행
            if (command != null) {
                try {
                    Process process = Runtime.getRuntime().exec(command);
                    System.out.println("✅ 음성 재생을 시작했습니다!");
                    
                    // 백그라운드에서 재생하므로 프로세스를 기다리지 않음
                    // process.waitFor();
                    
                } catch (Exception e) {
                    System.err.println("❌ 명령어 실행 실패: " + e.getMessage());
                    showManualPlayInstructions(filename);
                }
            } else {
                showManualPlayInstructions(filename);
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ 재생 중 오류: " + e.getMessage());
            showManualPlayInstructions(filename);
        }
    }
    
    /**
     * 수동 재생 안내 메시지 표시
     */
    private void showManualPlayInstructions(String filename) {
        System.out.println("💡 자동 재생에 실패했습니다. 수동으로 재생해주세요:");
        System.out.println("   - macOS: afplay " + filename);
        System.out.println("   - Windows: start " + filename + " 또는 파일을 더블클릭");
        System.out.println("   - Linux: mpg123 " + filename + " 또는 vlc " + filename);
        System.out.println("📁 파일 위치: " + new File(filename).getAbsolutePath());
    }
    
    /**
     * 사용 가능한 음성 목록 조회 (공식 문서 예제 참고)
     */
    public void listAvailableVoices() {
        if (!isInitialized) {
            System.err.println("❌ TTS 서비스가 초기화되지 않았습니다.");
            return;
        }
        
        try {
            // 한국어 음성 목록 조회 (공식 문서 예제 참고)
            String languageCode = "ko-KR";
            ListVoicesResponse response = textToSpeechClient.listVoices(languageCode);
            
            System.out.println("\n🎤 사용 가능한 한국어 음성:");
            System.out.println("=".repeat(70));
            System.out.println("| 음성명                    | 성별     | 언어코드   | 자연도    |");
            System.out.println("|---------------------------|----------|------------|-----------|");
            
            for (Voice voice : response.getVoicesList()) {
                String naturalSampleRate = voice.getNaturalSampleRateHertz() > 0 ? 
                    voice.getNaturalSampleRateHertz() + "Hz" : "N/A";
                
                System.out.printf("| %-25s | %-8s | %-10s | %-9s |\n", 
                    voice.getName(), 
                    voice.getSsmlGender(), 
                    voice.getLanguageCodesList().isEmpty() ? "N/A" : voice.getLanguageCodesList().get(0),
                    naturalSampleRate);
            }
            
            System.out.println("=".repeat(70));
            System.out.println("총 " + response.getVoicesCount() + "개의 음성이 사용 가능합니다.");
            
        } catch (Exception e) {
            System.err.println("❌ 음성 목록 조회 실패: " + e.getMessage());
        }
    }
    
    /**
     * 리소스 정리
     */
    public void cleanup() {
        try {
            if (audioPlayer != null) {
                audioPlayer.dispose();
                audioPlayer = null;
            }
            
            if (textToSpeechClient != null) {
                textToSpeechClient.close();
                System.out.println("✅ Google TTS 클라이언트가 정리되었습니다.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ 리소스 정리 중 오류: " + e.getMessage());
        } finally {
            isInitialized = false;
        }
    }
    
    /**
     * 초기화 상태 확인
     */
    public boolean isInitialized() {
        return isInitialized;
    }
} 