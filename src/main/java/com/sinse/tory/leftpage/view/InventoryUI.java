package com.sinse.tory.leftpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sinse.tory.common.view.Clock;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

// 왼쪽 영역 UI (960x1080)
public class InventoryUI extends JPanel {

	// 전체 영역을 수직으로 쌓는 컨테이너 (시계 + 제목/필터 + 격자)
	JPanel p_left;
	// 상단 영역 구성 요소들
	JPanel p_clockBar; // (좌: 로고, 우: 현재 시간) 부착할 패널
	JPanel p_titleBar; // (중앙: 제목, 우측: 필터 콤보박스) 부착할 패널
	JLabel la_logo, la_timeLabel, la_title; // 로고, 현재 시간 표시 라벨, 제목 라벨
	JComboBox<String> cb_filter; // 정렬 필터 콤보박스

	// 중앙 영역 (재고 격자 + 카테고리명)
	JPanel p_gridWrapper; // 격자 중앙 정렬용 래퍼
	JPanel p_grid; // 11x10 재고 격자 패널
	JLabel[] categories; // 하단 카테고리 라벨 배열

	// TopCategory
	TopCategoryDAO topcategoryDAO;
	List<TopCategory> topCategories;

	// Product
	ProductDAO productDAO;
	List<Product> products;

	// 각 상위카테고리별 색상을 매핑하는 categoryColors
	Map<String, Color> categoryColors;
	// 각 상위카테고리 이름들을 가지고 있는 categoryOrder
	List<String> categoryOrder;
	// 각 상위카테고리별 상품 개수를 저장하는 맵
	Map<String, Integer> productCountPerCategory;
	Map<JPanel, Product> boxProductMap = new LinkedHashMap<>();

	public InventoryUI() {
		// 전체 UI 영역 BorderLayout 기준 배치
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		/* ---------- 로고 + 시계 (p_clockBar) ---------- */

		// 시계 + 로고를 담을 상단 패널 (고정 높이 60px)
		p_clockBar = new JPanel(new BorderLayout());
		p_clockBar.setPreferredSize(new Dimension(960, 60));
		p_clockBar.setMaximumSize(new Dimension(960, 60));
		p_clockBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30)); // 우측 여백만 설정

		// 로고 이미지 설정 (높이 100px로 스케일 조정)
		ImageIcon logo = new ImageIcon("src/main/resources/images/Tory서비스 로고.png");
		Image scaledImage = logo.getImage().getScaledInstance(-1, 100, Image.SCALE_SMOOTH);
		la_logo = new JLabel(new ImageIcon(scaledImage));

		// 로고를 감싸는 패널에 FlowLayout(BOTTOM) 적용
		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		logoPanel.add(la_logo);

		// 시계 라벨 설정
		la_timeLabel = new JLabel();
		la_timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		new Clock(this); // 시계 갱신용 객체 (1초마다 라벨 업데이트)

		// 시계 패널 (오른쪽에 붙이고 아래 여백 15px로 살짝 띄움)
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
		timePanel.add(la_timeLabel);

		// 상단바에 로고와 시계 배치
		p_clockBar.add(logoPanel, BorderLayout.WEST);
		p_clockBar.add(timePanel, BorderLayout.EAST);

		/* ---------- 제목 + 정렬 필터 (p_titleBar) ---------- */
		p_titleBar = new JPanel(new BorderLayout());
		p_titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50)); // 상,하,좌,우 각각 여백주기

		la_title = new JLabel("재고 현황", JLabel.CENTER);
		la_title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));

		// 필터 콤보박스 항목 추가
		cb_filter = new JComboBox<>();
		cb_filter.addItem("재고량 많은 순");
		cb_filter.addItem("재고량 적은 순");
		cb_filter.addItem("최근 입고순");
		cb_filter.addItem("입고 예정순");

		// 콤보박스 크기 고정
		cb_filter.setPreferredSize(new Dimension(150, 40));

		p_titleBar.add(la_title, BorderLayout.CENTER);
		p_titleBar.add(cb_filter, BorderLayout.EAST);

		// [DB 연동]
		// TopCategory DAO 객체 생성 및 전체 조회
		topcategoryDAO = new TopCategoryDAO();
		topCategories = topcategoryDAO.selectAll();

		// ProductDAO: 상품 정보 + 연관 정보까지 모두 JOIN 조회
		productDAO = new ProductDAO();
		products = productDAO.selectAll();
		// Product에는 연관된 Location (브랜드/카테고리), ProductDetail(재고 수량), ProductImage 등이 모두
		// 포함됨

		// 카테고리 이름 + 색상 + 순서 매핑
		// 각 카테고리별 색상을 매핑하는 categoryColors
		categoryColors = new LinkedHashMap<>();
		// 각 카테고리 이름들을 가지고 있는 categoryOrder
		categoryOrder = new ArrayList<>();
		productCountPerCategory = new LinkedHashMap<>();

		// TopCategoryDAO로 가져온 topCategories에서 TopCategoryName을 가져와서 list에 저장
		for (TopCategory tc : topCategories) {
			categoryOrder.add(tc.getTopCategoryName());
		}
		// 카테고리별 상품 분류
		// 1. 카테고리별 상품 리스트 초기화
		Map<String, List<Product>> categoryProductMap = new LinkedHashMap<>();
		for (String category : categoryOrder) {
			categoryProductMap.put(category, new ArrayList<>());
		}
		// 2. 상품을 카테고리별로 분류
		for (Product p : products) {
			String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName()
					.trim();
			if (categoryProductMap.containsKey(topCategoryName)) {
				categoryProductMap.get(topCategoryName).add(p);
			}
		}
		// 카테고리별 색상 지정
		Color[] colors = { new Color(255, 99, 71), new Color(60, 179, 113), new Color(65, 105, 225),
				new Color(238, 130, 238), new Color(255, 215, 0), new Color(70, 130, 180), new Color(255, 140, 0),
				new Color(123, 104, 238), new Color(199, 21, 133), new Color(46, 139, 87) };
		// 각 카테고리에 고유 색상 할당
		for (int i = 0; i < categoryOrder.size(); i++) {
			categoryColors.put(categoryOrder.get(i), colors[i % colors.length]);
		}
		// 각 카테고리별 상품 개수 초기화
		for (String category : categoryOrder) {
			productCountPerCategory.put(category, 0);
		}
		// 상품 목록을 순회하며 각 상품의 상위카테고리별로 개수를 누적
		for (Product p : products) {
			String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName()
					.trim();
			if (productCountPerCategory.containsKey(topCategoryName)) {
				int current = productCountPerCategory.get(topCategoryName);
				productCountPerCategory.put(topCategoryName, current + 1);
			}
		}
		// 상위카테고리 이름의 개수
		int cols = categoryOrder.size();

		/* ---------- 창고 시각화 격자 + 카테고리 라벨 (p_gridWrapper)---------- */

		// 가운데 정렬용 셀 격자를 감싸는 래퍼
		p_gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// 2행 10열 (1행: 각 열마다 재고 셀 (InventoryCell), 2행: 카테고리명 라벨)
		// 셀 사이간격 3px
		p_grid = new JPanel(new GridLayout(2, 10, 3, 3));
		p_grid.setPreferredSize(new Dimension(600, 1200)); // 격자 전체 크기
		p_grid.setOpaque(false); // 배경 투명

		// grid 상단 (1행): 각 열에 InventoryCell 하나씩 생성 및 추가
		for (int col = 0; col < categoryOrder.size(); col++) {
			String category = categoryOrder.get(col);
			List<Product> productList = categoryProductMap.get(category);

			// 실제 재고량 계산 (ProductDetail.productQuantity 합계)
			int totalStock = 0;
			for (Product p : productList) {
				System.out.println("상품: " + p.getProductName() + " | 상세 개수: " + p.getProductDetails().size());
				for (ProductDetail d : p.getProductDetails()) {
					System.out.println("  → 수량: " + d.getProductQuantity());
					totalStock += d.getProductQuantity();
				}
			}

			// 셀 생성 + 재고량 업데이트
			InventoryCell cell = new InventoryCell(categoryColors.get(category));
			cell.stockUpdate(totalStock);
			p_grid.add(cell);

			// 셀과 상품을 매핑 (클릭이벤트 연결용)
			boxProductMap.put(cell, productList.isEmpty() ? null : productList.get(0));
		}

		// grid 하단 : 카테고리명 라벨
		categories = new JLabel[categoryOrder.size()];
		for (int i = 0; i < categoryOrder.size(); i++) {
			String categoryName = categoryOrder.get(i);
			categories[i] = new JLabel(categoryName, JLabel.CENTER); // 텍스트 중앙 정렬
			categories[i].setVerticalAlignment(JLabel.TOP);
			categories[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			categories[i].setForeground(Color.DARK_GRAY); // 글자색

			p_grid.add(categories[i]);
		}
		p_gridWrapper.add(p_grid);

		/* ------- 모든 구조 포함 Panel (p_left) : 로고/시계 , 제목/필터, 그리드 순------- */
		// 전체 영역을 수직으로 쌓기ㄴ
		p_left = new JPanel();
		p_left.setLayout(new BoxLayout(p_left, BoxLayout.Y_AXIS)); // 수직정렬
		p_left.add(p_clockBar);
		p_left.add(p_titleBar);
		p_left.add(p_gridWrapper);
		add(p_left, BorderLayout.CENTER);

	}

	// 시계 라벨에 접근할 수 있도록 getter 제공
	public JLabel getLa_timeLabel() {
		return la_timeLabel;
	}
}
