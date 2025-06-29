package com.sinse.tory.stt;

/**
 * 음성을 텍스트로 변환하는 인터페이스
 * 실제 STT API 구현체로 교체 가능하도록 설계
 */
public interface SpeechToTextInterface {
    
    /**
     * 음성 파일을 텍스트로 변환
     * @param audioFilePath 음성 파일 경로
     * @return 변환된 텍스트
     * @throws Exception 변환 중 발생한 예외
     */
    String convertSpeechToText(String audioFilePath) throws Exception;
    
    /**
     * 음성 데이터(바이트 배열)를 텍스트로 변환
     * @param audioData 음성 데이터
     * @return 변환된 텍스트
     * @throws Exception 변환 중 발생한 예외
     */
    String convertSpeechToText(byte[] audioData) throws Exception;
    
    /**
     * STT 서비스 초기화
     * @return 초기화 성공 여부
     */
    boolean initialize();
    
    /**
     * STT 서비스 종료 및 리소스 정리
     */
    void cleanup();
    
    /**
     * STT 서비스 가용성 확인
     * @return 서비스 사용 가능 여부
     */
    boolean isAvailable();
} 
