package com.sinse.tory.common.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Timer;

import com.sinse.tory.leftpage.view.InventoryUI;

//오늘 날짜 + 현재 시각(초까지) 표시하는 객체
public class Clock {
	InventoryUI inventoryUI;

    public Clock(InventoryUI inventoryUI) {
    	this.inventoryUI=inventoryUI;
    	
    	// 타이머 설정 (1초마다 실행)
    	Timer timer = new Timer(1000, e -> updateTime());
    	timer.start();
	}
    
    // 현재 시간을 가져와 라벨에 표시하는 메서드
    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd(E) HH:mm:ss");
        String formattedTime = now.format(formatter);
        inventoryUI.getLa_timeLabel().setText(formattedTime);
    }
}
