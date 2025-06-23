package com.sinse.tory.leftpage.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 인벤토리(재고) 단일 셀 패널 - 수직으로 쌓이는 형태 (최대 10칸) - 카테고리별 색상을 지정하여 시각적으로 표현 - 재고 수량에 따라
 * 애니메이션 효과로 채워짐
 */
public class InventoryCell extends JPanel {

	private final Color categoryColor; // 셀 안을 채울 배경 색상 (카테고리별로 다르게 지정 가능)
	private final int maxStock = 10; // 최대 셀 개수 (세로 10칸)
	private int currentStock; // 현재 보이는 재고 수량 (0~10)

	// @param categoryColor - 해당 카테고리를 나타내는 색상
	public InventoryCell(Color categoryColor) {
		this.categoryColor = categoryColor;

		// 셀 내부를 수직(10행)으로 나누는 레이아웃 설정
		setLayout(new GridLayout(maxStock, 1, 2, 2)); // 행 간 간격 2px
		setOpaque(false); // 배경 투명 처리 (직접 그릴 것)
	}
	
	/**
	 * 색상 있는 단일 셀을 생성하는 헬퍼 메서드
	 * @param color - 셀 내부 색상
	 * @return JPanel - 둥근 테두리의 색상 셀
	 */
	private JPanel createColorCell(Color color) {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g; // 더 정밀한 그래픽 처리를 위해

				// 안티앨리어싱: 모서리를 부드럽게 그리기 위한 설정
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(color);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 사각형

				// 흰색 테두리 그리기
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2)); // 선 두께 2px
				g2.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 흰색 테두리
			}
		};
	}

	// 현재 재고 수량(currentStock)을 기반으로 셀 UI를 다시 그림
	private void renderCells() {
		removeAll(); // 기존 셀 다 지우기
		JPanel cell;

		for (int i = maxStock - 1; i >= 0; i--) {
			if (i < currentStock) {
				// 재고가 있는 셀 (카테고리별 색상)
				cell = createColorCell(categoryColor);
			} else {
				// 재고가 없는 셀 (회색)
				cell = createColorCell(Color.DARK_GRAY);
			}
			add(cell);
		}

		revalidate(); // 레이아웃 다시 계산/정렬
		repaint(); // 화면 다시 그리기
	}

	// 재고 수량에 따라 셀을 하나씩 채워주는 애니메이션 메서드
	// @param lastStock - 최종 재고 수량 (0~10)
	public void stockUpdate(int lastStock) {
		Thread aniThread = new Thread(() -> {
			for (int i = 0; i <= lastStock; i++) {
				currentStock = i; // 현재 재고 설정

				/*
				 * Swing은 UI를 다른 스레드에서 직접 바꾸면 오류가 나므로 
				 * Swing에게 renderCells() 호출을 "나중에 직접 해달라고" 위임함
				 */
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						renderCells(); // 실제 셀 그리기 메서드 (UI 변경 작업)
					}
				});

				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		aniThread.start(); // 비동기 애니메이션 실행
	}
}
