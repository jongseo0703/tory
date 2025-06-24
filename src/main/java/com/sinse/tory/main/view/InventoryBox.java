package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//awt 관련 패키지 임포트
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

//스윙 관련 패키지 임포트
import javax.swing.JPanel;

public class InventoryBox extends JPanel {

    private Color fillColor = Color.decode("#39393B"); //기본 색상 (비어 있음)
    private static final int ARC = 20; //둥근 정도
    private static final int PADDING = 2;

    public InventoryBox() {
        setOpaque(false); //배경 투명
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
        repaint(); //다시 그리기
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //배경색
        g2.setColor(fillColor);
        g2.fillRoundRect(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING, ARC, ARC);

        //흰색 테두리
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING, ARC, ARC);

        g2.dispose();
    }
}