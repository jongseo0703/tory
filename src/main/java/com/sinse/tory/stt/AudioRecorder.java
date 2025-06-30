package com.sinse.tory.stt;

import javax.sound.sampled.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java Sound API를 사용한 오디오 녹음 클래스
 * 멀티스레딩으로 논블로킹 방식 구현
 */
public class AudioRecorder {
    
    // 오디오 포맷 설정 - Google STT 최적화
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,  // 인코딩
        16000.0f,              // 샘플 레이트 (16kHz - Google STT 권장)
        16,              // 비트 깊이 (16-bit)
        1,                       // 채널 수 (모노)
        2,                      // 프레임 크기 (16bit * 1채널 = 2바이트)
        16000.0f,               // 프레임 레이트 (샘플레이트와 동일)
        false                   // 빅 엔디안 여부
    );
    
    private TargetDataLine targetDataLine;
    private ByteArrayOutputStream audioDataStream;
    private final AtomicBoolean isRecording = new AtomicBoolean(false);
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    private ExecutorService executorService;
    
    /**
     * AudioRecorder 초기화
     */
    public AudioRecorder() {
        this.executorService = Executors.newFixedThreadPool(2);
        this.audioDataStream = new ByteArrayOutputStream();
    }
    
    /**
     * 녹음 시작
     */
    public synchronized boolean startRecording() {
        if (isRecording.get()) {
            System.out.println("이미 녹음 중입니다.");
            return false;
        }
        
        try {
            // 마이크 라인 설정
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
            
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("오디오 라인이 지원되지 않습니다.");
                return false;
            }
            
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(AUDIO_FORMAT);
            
            // 마이크 게인 설정 (가능한 경우)
            try {
                if (targetDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) targetDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                    // 게인을 약간 높여서 더 선명한 음성 녹음
                    gainControl.setValue(Math.min(gainControl.getMaximum(), gainControl.getValue() + 3.0f));
                    System.out.println("마이크 게인 설정됨: " + gainControl.getValue() + " dB");
                }
            } catch (Exception e) {
                System.out.println("마이크 게인 설정 실패 (무시됨): " + e.getMessage());
            }
            
            targetDataLine.start();
            
            isRecording.set(true);
            isPaused.set(false);
            audioDataStream.reset();
            
            // 녹음 스레드 시작
            executorService.submit(this::recordAudio);
            
            System.out.println("녹음을 시작했습니다. (형식: " + AUDIO_FORMAT + ")");
            return true;
            
        } catch (LineUnavailableException e) {
            System.err.println("오디오 라인을 사용할 수 없습니다: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 녹음 중지
     */
    public synchronized void stopRecording() {
        if (!isRecording.get()) {
            System.out.println("녹음 중이 아닙니다.");
            return;
        }
        
        isRecording.set(false);
        
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
        
        System.out.println("녹음을 중지했습니다.");
    }
    
    /**
     * 녹음 일시정지/재개
     */
    public void togglePause() {
        if (!isRecording.get()) {
            System.out.println("녹음 중이 아닙니다.");
            return;
        }
        
        isPaused.set(!isPaused.get());
        System.out.println(isPaused.get() ? "녹음 일시정지" : "녹음 재개");
    }
    
    /**
     * 녹음 상태 확인
     */
    public boolean isRecording() {
        return isRecording.get();
    }
    
    /**
     * 녹음 스레드 - 실제 오디오 데이터 수집
     */
    private void recordAudio() {
        byte[] buffer = new byte[1024]; // 적절한 버퍼 크기
        
        try {
            while (isRecording.get()) {
                if (!isPaused.get()) {
                    int bytesRead = targetDataLine.read(buffer, 0, buffer.length);
                    
                    if (bytesRead > 0) {
                        // 모든 오디오 데이터를 저장
                        audioDataStream.write(buffer, 0, bytesRead);
                    }
                }
                
                // CPU 사용률 최적화를 위한 짧은 대기
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("녹음 스레드가 중단되었습니다.");
        }
    }
    
    /**
     * WAV 파일로 저장
     */
    public boolean saveToWavFile(String filename) {
        if (audioDataStream.size() == 0) {
            System.out.println("저장할 오디오 데이터가 없습니다.");
            return false;
        }
        
        ByteArrayInputStream bais = null;
        AudioInputStream audioInputStream = null;
        
        try {
            byte[] audioData = audioDataStream.toByteArray();
            bais = new ByteArrayInputStream(audioData);
            audioInputStream = new AudioInputStream(bais, AUDIO_FORMAT, 
                audioData.length / AUDIO_FORMAT.getFrameSize());
            
            File wavFile = new File(filename);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
            
            System.out.println("\n오디오 파일이 저장되었습니다: " + wavFile.getAbsolutePath());
            System.out.println("파일 크기: " + (wavFile.length() / 1024) + " KB");
            System.out.println("녹음 시간: " + String.format("%.1f", 
                (double) audioData.length / AUDIO_FORMAT.getFrameSize() / AUDIO_FORMAT.getFrameRate()) + " 초");
            
            return true;
            
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
            return false;
        } finally {
            // 리소스 정리 - 재생 문제 해결
            try {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                System.err.println("오디오 스트림 정리 중 오류: " + e.getMessage());
            }
        }
    }
    
    /**
     * 리소스 정리
     */
    public void cleanup() {
        stopRecording();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        
        try {
            audioDataStream.close();
        } catch (IOException e) {
            System.err.println("AudioStream 정리 중 오류: " + e.getMessage());
        }
    }
} 