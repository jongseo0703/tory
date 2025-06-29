package com.sinse.tory.tts;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX MediaPlayer를 사용한 오디오 재생 클래스
 * MP3, WAV 등 다양한 오디오 포맷을 GUI에서 직접 재생할 수 있습니다.
 */
public class AudioPlayer {
    
    private MediaPlayer mediaPlayer;
    private boolean isInitialized = false;
    private static boolean javafxInitialized = false;
    
    /**
     * JavaFX 플랫폼 초기화
     */
    public static void initializeJavaFX() {
        if (!javafxInitialized) {
            // JavaFX Toolkit 초기화 (Swing 애플리케이션에서 JavaFX 사용)
            new JFXPanel(); // 이것만으로도 JavaFX 플랫폼이 초기화됩니다
            javafxInitialized = true;
            System.out.println("✅ JavaFX 플랫폼이 초기화되었습니다.");
        }
    }
    
    /**
     * 오디오 파일을 로드합니다
     * 
     * @param audioFile 재생할 오디오 파일
     * @return 로드 성공 여부
     */
    public boolean loadAudio(File audioFile) {
        if (!audioFile.exists()) {
            System.err.println("❌ 오디오 파일이 존재하지 않습니다: " + audioFile.getAbsolutePath());
            return false;
        }
        
        try {
            // 기존 MediaPlayer 정리
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }
            
            // 새로운 Media와 MediaPlayer 생성
            String mediaUrl = audioFile.toURI().toString();
            Media media = new Media(mediaUrl);
            mediaPlayer = new MediaPlayer(media);
            
            // MediaPlayer 이벤트 리스너 설정
            setupMediaPlayerListeners();
            
            isInitialized = true;
            System.out.println("✅ 오디오 파일 로드 완료: " + audioFile.getName());
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ 오디오 파일 로드 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 바이트 배열로부터 임시 파일을 생성하고 로드합니다
     * 
     * @param audioData 오디오 데이터 바이트 배열
     * @param filename 임시 파일명
     * @return 로드 성공 여부
     */
    public boolean loadAudioFromBytes(byte[] audioData, String filename) {
        try {
            // 임시 파일 생성
            File tempFile = new File(filename);
            
            // 파일이 이미 존재한다면 그것을 사용
            if (tempFile.exists()) {
                return loadAudio(tempFile);
            }
            
            System.err.println("❌ 오디오 파일을 찾을 수 없습니다: " + filename);
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ 바이트 배열로부터 오디오 로드 실패: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * MediaPlayer 이벤트 리스너 설정
     */
    private void setupMediaPlayerListeners() {
        mediaPlayer.setOnReady(() -> {
            System.out.println("🎵 오디오가 재생 준비되었습니다.");
            Duration duration = mediaPlayer.getTotalDuration();
            System.out.println("⏱️  재생 시간: " + formatDuration(duration));
        });
        
        mediaPlayer.setOnPlaying(() -> {
            System.out.println("▶️  오디오 재생 시작");
        });
        
        mediaPlayer.setOnPaused(() -> {
            System.out.println("⏸️  오디오 재생 일시정지");
        });
        
        mediaPlayer.setOnStopped(() -> {
            System.out.println("⏹️  오디오 재생 정지");
        });
        
        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("✅ 오디오 재생 완료");
        });
        
        mediaPlayer.setOnError(() -> {
            System.err.println("❌ 오디오 재생 중 오류 발생: " + mediaPlayer.getError());
        });
    }
    
    /**
     * 오디오 재생 시작
     */
    public void play() {
        if (!isInitialized || mediaPlayer == null) {
            System.err.println("❌ 오디오가 로드되지 않았습니다.");
            return;
        }
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.play();
            } catch (Exception e) {
                System.err.println("❌ 재생 시작 실패: " + e.getMessage());
            }
        });
    }
    
    /**
     * 오디오 재생 일시정지
     */
    public void pause() {
        if (!isInitialized || mediaPlayer == null) {
            return;
        }
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.pause();
            } catch (Exception e) {
                System.err.println("❌ 일시정지 실패: " + e.getMessage());
            }
        });
    }
    
    /**
     * 오디오 재생 정지
     */
    public void stop() {
        if (!isInitialized || mediaPlayer == null) {
            return;
        }
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                System.err.println("❌ 정지 실패: " + e.getMessage());
            }
        });
    }
    
    /**
     * 오디오 재생 및 완료까지 대기
     */
    public void playAndWait() {
        if (!isInitialized || mediaPlayer == null) {
            System.err.println("❌ 오디오가 로드되지 않았습니다.");
            return;
        }
        
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.setOnEndOfMedia(() -> {
                    System.out.println("✅ 오디오 재생 완료");
                    latch.countDown();
                });
                
                mediaPlayer.setOnError(() -> {
                    System.err.println("❌ 오디오 재생 중 오류 발생: " + mediaPlayer.getError());
                    latch.countDown();
                });
                
                mediaPlayer.play();
            } catch (Exception e) {
                System.err.println("❌ 재생 시작 실패: " + e.getMessage());
                latch.countDown();
            }
        });
        
        try {
            // 최대 30초 대기
            boolean finished = latch.await(30, TimeUnit.SECONDS);
            if (!finished) {
                System.out.println("⚠️  재생 시간이 30초를 초과했습니다.");
                stop();
            }
        } catch (InterruptedException e) {
            System.err.println("⚠️  재생 대기 중 인터럽트: " + e.getMessage());
            stop();
        }
    }
    
    /**
     * 현재 재생 위치 설정
     * 
     * @param seconds 재생 시작할 위치 (초)
     */
    public void seek(double seconds) {
        if (!isInitialized || mediaPlayer == null) {
            return;
        }
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.seek(Duration.seconds(seconds));
            } catch (Exception e) {
                System.err.println("❌ 재생 위치 이동 실패: " + e.getMessage());
            }
        });
    }
    
    /**
     * 볼륨 설정
     * 
     * @param volume 볼륨 (0.0 ~ 1.0)
     */
    public void setVolume(double volume) {
        if (!isInitialized || mediaPlayer == null) {
            return;
        }
        
        Platform.runLater(() -> {
            try {
                mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
            } catch (Exception e) {
                System.err.println("❌ 볼륨 설정 실패: " + e.getMessage());
            }
        });
    }
    
    /**
     * 재생 상태 확인
     * 
     * @return 현재 재생 중인지 여부
     */
    public boolean isPlaying() {
        if (!isInitialized || mediaPlayer == null) {
            return false;
        }
        
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    /**
     * Duration을 사용자 친화적인 형식으로 변환
     */
    private String formatDuration(Duration duration) {
        if (duration == null || duration.isUnknown()) {
            return "알 수 없음";
        }
        
        int totalSeconds = (int) duration.toSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    /**
     * 리소스 정리
     */
    public void dispose() {
        if (mediaPlayer != null) {
            Platform.runLater(() -> {
                try {
                    mediaPlayer.dispose();
                    System.out.println("✅ MediaPlayer 리소스가 정리되었습니다.");
                } catch (Exception e) {
                    System.err.println("⚠️  MediaPlayer 정리 중 오류: " + e.getMessage());
                }
            });
            mediaPlayer = null;
        }
        isInitialized = false;
    }
} 