package com.sinse.tory.common.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Timer;

import com.sinse.tory.leftpage.view.InventoryUI;

/**
 * 현재 날짜와 시각(초 단위까지)을 실시간으로 표시해주는 시계 클래스
 * 역할:
 * - 1초마다 현재 시간을 `InventoryUI`의 시계 라벨에 갱신해줌
 * 형식 예시:
 * - "2025-06-25(수) 14:23:45"
 */
public class Clock {
	InventoryUI inventoryUI;

	/**
	 * 생성자
	 * - Clock 객체 생성 시, UI에 시계 라벨을 즉시 표시하고 1초마다 자동 갱신
	 * @param inventoryUI 시계 라벨이 포함된 대상 UI
	 */
    public Clock(InventoryUI inventoryUI) {
    	this.inventoryUI=inventoryUI;
    	
    	updateTime(); // 프로그램이 실행되자마자 시계 표시 위해
    	
    	// 타이머 설정 (1초마다 실행)
    	Timer timer = new Timer(1000, e -> updateTime());
    	timer.start();
	}
    
	/**
	 * 현재 시간을 가져와 형식화한 뒤, 시계 라벨에 출력
	 * - 시간 형식: yyyy-MM-dd(E) HH:mm:ss
	 */
    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd(E) HH:mm:ss");
        String formattedTime = now.format(formatter);
        inventoryUI.getLa_timeLabel().setText(formattedTime);
    }
}
