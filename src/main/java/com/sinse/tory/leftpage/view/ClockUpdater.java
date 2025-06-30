package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//스윙 관련 패키지 임포트
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Color;
//time 관련 패키지 임포트
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//현재 날짜를 가져오는 클래스
public class ClockUpdater {
	private JLabel la_clock;
	private Timer timerClock;
	private DateTimeFormatter formatter;
	
	//la_clock을 초기화해주는 생성자
	public ClockUpdater(JLabel la_clock) {
		this.la_clock = la_clock;
		//원하는 형식으로(MM -> 월, dd -> 일, HH -> 시, mm -> 분)
		this.formatter = DateTimeFormatter.ofPattern("MM:dd:HH:mm");
	}
	
	//현재 날짜를 가져오는 메서드
	public void start() {
		//1초(1000ms)마다 실행되는 타이머 생성
		timerClock = new Timer(1000, e -> {
			//현재 날짜와 시간 정보 가져오기
			LocalDateTime now = LocalDateTime.now();
			la_clock.setText("현재 날짜는 " + now.format(formatter));
			la_clock.setForeground(Color.decode("#393939"));
		});
		timerClock.start();
	}

}
