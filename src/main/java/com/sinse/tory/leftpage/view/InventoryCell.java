package com.sinse.tory.leftpage.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

// 셀 하나를 둥근 테두리 + 색상 있는 사각형으로 표현
public class InventoryCell extends JPanel{
	private Color fillColor; // 셀 안을 채울 배경 색상 (카테고리별로 다르게 지정 가능)

	public InventoryCell(Color fillColor) {
		this.fillColor = fillColor;
		setOpaque(false); // JPanel의 기본 배경색 칠하지 않음 (직접 그릴 것)
		setPreferredSize(new Dimension(50, 50)); // 이 셀의 크기 고정
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // 더 정밀한 그래픽 처리를 위해 

		// 안티앨리어싱: 모서리를 부드럽게 그리기 위한 설정
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(fillColor);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 사각형

		// 흰색 테두리 그리기
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2)); // 선 두께 2px
		g2.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 흰색 테두리
	}

}
