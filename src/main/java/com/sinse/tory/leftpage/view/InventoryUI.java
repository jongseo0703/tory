package com.sinse.tory.leftpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sinse.tory.common.view.Clock;

// 왼쪽 영역 UI
public class InventoryUI extends JPanel {

	JLabel la_logo, la_title; // 로고, 제목 라벨
	JLabel la_timeLabel; // 현재 시간 표시 라벨
	JComboBox<String> cb_filter; // 정렬 필터 콤보박스
	JPanel p_grid; // 10x10 재고 격자 패널
	JLabel[] categories; // 하단 카테고리 라벨 배열
	Color[] columnColors; // 컬럼별 색상 고정 배열
	JPanel p_clockBar;
	JPanel p_titleBar;
	JPanel p_top;
	JPanel p_gridWrapper;
	JPanel p_footer;

	public InventoryUI() {
		// 전체 패널을 BorderLayout으로 설정 (NORTH / CENTER / SOUTH 구조)
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// 컬럼별 고정 색상 (column index별로 고정 색상 부여)
		columnColors = new Color[] { Color.PINK, Color.BLUE, Color.RED, Color.MAGENTA, Color.ORANGE, Color.CYAN,
				Color.YELLOW, Color.GREEN, Color.LIGHT_GRAY, Color.GRAY };
		/* ---------- 상단 영역 (NORTH) ---------- */
		// 시계 영역 (좌: 로고, 우: 현재 시간)
		p_clockBar = new JPanel(new BorderLayout());
		la_logo = new JLabel("TORY 로고 이미지 넣기");
		la_timeLabel = new JLabel();
		la_timeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
		new Clock(this); // 1초마다 갱신되는 시계 객체

		p_clockBar.add(la_logo, BorderLayout.WEST);
		p_clockBar.add(la_timeLabel, BorderLayout.EAST);

		// 제목 + 필터 영역
		p_titleBar = new JPanel(new BorderLayout());
		la_title = new JLabel("재고 현황", JLabel.CENTER);
		la_title.setFont(new Font("Gulim", Font.BOLD, 36));

		// 필터 콤보박스 항목 추가
		cb_filter = new JComboBox<>();
		cb_filter.addItem("재고량 많은 순");
		cb_filter.addItem("재고량 적은 순");
		cb_filter.addItem("최근 입고순");
		cb_filter.addItem("입고 예정순");

		p_titleBar.add(la_title, BorderLayout.CENTER);
		p_titleBar.add(cb_filter, BorderLayout.EAST);

		// 상단 전체 영역을 수직으로 쌓기 (로고/시계 위, 제목/필터 아래)
		p_top = new JPanel();
		// BoxLayout.Y_AXIS : 패널 안에 있는 컴포넌트를 위에서 아래로 세로로 정렬
		p_top.setLayout(new BoxLayout(p_top, BoxLayout.Y_AXIS));
		p_top.add(p_clockBar);
		p_top.add(p_titleBar);

		add(p_top, BorderLayout.NORTH);

		/* ---------- 중앙 격자 영역 (CENTER) ---------- */

		// 가운데 정렬용 셀 격자를 감싸는 래퍼
		p_gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));

		p_grid = new JPanel(new GridLayout(10, 10, 3, 3)); // 10X10격자, 셀 사이간격 3px
		p_grid.setBackground(Color.WHITE);
		p_grid.setPreferredSize(new Dimension(600, 600)); // 격자 전체 크기

		// 셀 생성: row, col 반복 돌면서 색상 설정
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				InventoryCell cell = new InventoryCell(columnColors[col]);
				p_grid.add(cell);
			}
		}
		p_gridWrapper.add(p_grid);
		add(p_gridWrapper, BorderLayout.CENTER);

		/* ---------- 하단 카테고리 영역 (SOUTH) ---------- */

		// 1행 10열로 구성된 하단 카테고리 패널
		JPanel p_footer = new JPanel(new GridLayout(1, 10));
		String[] names = { "티셔츠", "셔츠", "청바지", "신발", "가방", "양말", "가디건", "점퍼", "목도리", "패딩" };
		categories = new JLabel[names.length];
		for (int i = 0; i < names.length; i++) {
			categories[i] = new JLabel(names[i], JLabel.CENTER); // 텍스트 중앙 정렬
			categories[i].setForeground(Color.DARK_GRAY); // 글자색
			categories[i].setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
			p_footer.add(categories[i]);
		}
		p_footer.setPreferredSize(new Dimension(960, 40));
		p_footer.setBackground(Color.LIGHT_GRAY); // 임시 확인용

		add(p_footer, BorderLayout.SOUTH);
	}

	// Clock이 la_timeLabel을 업데이트하기 위해 필요
	public JLabel getLa_timeLabel() {
		return la_timeLabel;
	}
}
