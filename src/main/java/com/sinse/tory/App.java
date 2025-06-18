package com.sinse.tory;

import java.awt.BorderLayout;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//awt 관련 패키지 임포트
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//스윙 관련 패키지 임포트
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class App extends JFrame {
	/**
	 * 상수 영역
	 */
	private static final int ROWS = 10; //행
	private static final int COLS = 10; //열
	private static final int BOX_SIZE = 80; //박스의 크기
	private static final int DELAY = 30; //박스가 채워지는 속도
	
	JPanel p_wrapper; //내부를 중앙 아래에 가깝게 배치하기 위한 패널
	JPanel p_box; //박스들이 담길 패널
	
	GridBagConstraints gbc; //각 컴포넌트들을 정밀하게 조절하기 위함
	
	JPanel[] boxes; //박스들의 배열
	Color[] colors; //각 박스들의 색 배열
	
	int currentIndex = 0; //현재 인덱스
	
	public App() {
		//생성
		//전체 레이아웃 중앙 정렬 및 상단 여(100px) 설정
		p_wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 100));
		//개별 박스들을 균등하게 배치하기 위한 GridLayout
		p_box = new JPanel(new GridLayout(ROWS, COLS, 2, 2)); //박스 간격 2px
		
		gbc = new GridBagConstraints();
		
		int totalBoxes = ROWS * COLS; //행 X 열 = 전체 박스 개수
		boxes = new JPanel[totalBoxes]; //전체 박스 배열
		colors = new Color[totalBoxes]; //전체 색상 배열
		
		for(int i = 0; i < totalBoxes; i++) {
			//각 박스들 생성 및 스타일 부여
			boxes[i] = new JPanel();
			boxes[i].setPreferredSize(new Dimension(BOX_SIZE, BOX_SIZE));
			boxes[i].setBackground(Color.GRAY);
			p_box.add(boxes[i]);
			
			//랜덤으로 색상 저장
			colors[i] = new Color(
					(int)(Math.random() * 256),
					(int)(Math.random() * 256),
					(int)(Math.random() * 256)
			);
		}
		
		//스타일
		p_wrapper.setBackground(Color.WHITE);
		
		gbc.gridx = 0;
		gbc.gridy = 1; //중앙보다 약간 아래
		gbc.anchor = GridBagConstraints.NORTH; //위 기준으로 정렬
		gbc.insets = new Insets(100, 0, 50, 0); //top, left, bottom, right
		
		p_box.setPreferredSize(new Dimension(ROWS * BOX_SIZE, COLS * BOX_SIZE));
		p_box.setOpaque(false); //배경 투명
		
		Timer timer = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentIndex < boxes.length) {
					boxes[currentIndex].setBackground(colors[currentIndex]);
					currentIndex++;
				}
				else {
					((Timer) e.getSource()).stop();
				}
			}
		});
		
		timer.start();
		
		//부착
		p_wrapper.add(p_box, gbc);
		add(p_wrapper, BorderLayout.CENTER);
		
		setSize(1440, 1024); //화면 크기는 1440 X 1024
		setVisible(true); //화면 보여주기
		setLocationRelativeTo(null); //화면 중앙에 화면 띄우기
		setDefaultCloseOperation(EXIT_ON_CLOSE); //화면을 끄면 자동으로 종료됨
	}
	
	public static void main(String[] args) {
		new App();
	}
	
}
