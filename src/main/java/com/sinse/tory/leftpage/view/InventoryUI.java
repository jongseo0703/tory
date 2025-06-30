package com.sinse.tory.leftpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.sinse.tory.common.view.Clock;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;
import com.sinse.tory.db.model.InventoryLog;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.rightpage.view.ProductShip;

/**
 * 창고 재고 현황을 시각적으로 표시하는 왼쪽 메인 UI 패널
 * 
 * 주요 구성:
 * - 상단: 로고 + 현재 시간
 * - 중단: "재고 현황" 제목 + 정렬 필터 콤보박스
 * - 하단: 상위 카테고리별 재고 셀(InventoryCell) 및 라벨을 그리드로 정렬
 * 
 * 기능:
 * - main/view의 데이터 로딩 및 관리 시스템 활용
 * - 상위 카테고리별 고유 색상 지정 및 하위 카테고리별 색상 연동
 * - 하위 카테고리별 재고 수량을 바탕으로 InventoryCell에 애니메이션 렌더링
 * - 정렬 기능을 통한 동적 재배치
 * 
 * 사용자는 각 셀에서 상품 분류별 수량을 직관적으로 파악할 수 있으며,
 * 셀에 마우스를 올리면 해당 하위 카테고리가 강조되도록 인터랙션 처리됨.
 */
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

	// main/view 기능 통합
	MainPageDataLoader mainPageDataLoader;
	CategoryManager categoryManager;
	
	// 기존 DAO들 (Clock 등에서 사용하므로 유지)
	TopCategoryDAO topCategoryDAO;
	List<TopCategory> topCategories;
	
	SubCategoryDAO subCategoryDAO;
	List<SubCategory> subCategories;

	ProductDAO productDAO;
	List<Product> products;

	// CategoryManager에서 관리되는 데이터들
	Map<String, Color> categoryColors;
	List<String> categoryOrder;
	Map<String, Integer> productCountPerCategory;
	Map<String, List<Product>> categoryProductMap;
	
	Map<JPanel, Product> boxProductMap = new LinkedHashMap<>();
	// 셀을 상위카테고리명으로 묶기
	Map<String, List<InventoryCell>> categoryPanelMap = new HashMap<>();
	
	Map<String, Color> subColorMap = new HashMap<>();
	
	// 정렬을 위한 재고 로그 데이터
	List<InventoryLog> inventoryLogs;
	
	// 오른쪽 페이지 연동을 위한 ProductShip 참조
	private ProductShip productShip;
	
	/**
	 * InventoryUI 생성자
	 * - 전체 창고 시각화 UI를 초기화하고 구성함
	 * - main/view의 데이터 관리 시스템 활용
	 * - 상단 영역(로고, 시계), 제목/필터 영역, 중앙 재고 격자 영역 등을 설정
	 * - DB에서 상위/하위 카테고리 및 상품 정보를 조회하여 카테고리별 셀과 색상을 구성
	 * - 하위카테고리별 재고 수량에 따라 애니메이션 기반으로 셀을 시각화함
	 */
	public InventoryUI() {
		// 전체 UI 영역 BorderLayout 기준 배치
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// main/view 데이터 관리 시스템 초기화
		initializeDataManagement();

		// UI 구성 요소들 초기화
		initializeUIComponents();
		
		// 재고 격자 생성
		createInventoryGrid();
		
		// 정렬 기능 연결
		setupSortingFeature();

		// 레이아웃 구성
		buildLayout();
	}
	
	/**
	 * main/view의 데이터 관리 시스템 초기화
	 */
	private void initializeDataManagement() {
		// 데이터 로더 및 카테고리 매니저 초기화
		mainPageDataLoader = new MainPageDataLoader();
		categoryManager = new CategoryManager();
		
		// 데이터 로드
		topCategories = mainPageDataLoader.loadTopCategories();
		products = mainPageDataLoader.loadProducts();
		
		// 카테고리 매니저 초기화
		categoryManager.init(topCategories, products);
		
		// 데이터 가져오기
		categoryOrder = categoryManager.getCategoryOrder();
		categoryColors = categoryManager.getCategoryColors();
		productCountPerCategory = categoryManager.getProductCountPerCategory();
		categoryProductMap = categoryManager.categorizeProducts(products);
		
		// 정렬을 위한 재고 로그 데이터 로드
		ProductDetailDAO productDetailDAO = new ProductDetailDAO();
		inventoryLogs = productDetailDAO.selectAllInventoryLogsWithProductInfo();
		
		// 하위카테고리 색상 매핑 (기존 로직 유지)
		subColorMap = new HashMap<>();
		for (Product p : products) {
			String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
			String subCategoryName = p.getLocation().getBrand().getSubCategory().getSubCategoryName().trim();

			if (!subColorMap.containsKey(subCategoryName)) {
				Color topColor = categoryColors.get(topCategoryName);
				subColorMap.put(subCategoryName, topColor);
			}
		}
	}
	
	/**
	 * UI 구성 요소들 초기화
	 */
	private void initializeUIComponents() {
		/* ---------- 로고 + 시계 (p_clockBar) ---------- */
		// 시계 + 로고를 담을 상단 패널 (고정 높이 60px)
		p_clockBar = new JPanel(new BorderLayout());
		p_clockBar.setPreferredSize(new Dimension(960, 60));
		p_clockBar.setMaximumSize(new Dimension(960, 60));
		p_clockBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30)); // 우측 여백만 설정

		// 로고 이미지 설정 (높이 100px로 스케일 조정)
		ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("images/Tory서비스 로고.png"));
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

		//재고현황 글씨를 감싸는 헤더영역 생성
		JPanel p_title = new JPanel(new BorderLayout());
		la_title = new JLabel("재고 현황", JLabel.CENTER);
		la_title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));

		// 필터 콤보박스 항목 추가 (main/view와 동일하게)
		cb_filter = new JComboBox<>();
		cb_filter.addItem("보기");
		cb_filter.addItem("재고량순");
		cb_filter.addItem("출고순");
		cb_filter.addItem("입고일순");
		
		// title 중앙으로 밀어줄 빈 패널
		JPanel leftSpacer = new JPanel();
		leftSpacer.setPreferredSize(new Dimension(180, 30));
		
		// 콤보박스를 감쌀 패널 생성 (FlowLayout 사용 시 크기 반영에 유리)
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10)); // 세로 padding 조정
		cb_filter.setPreferredSize(new Dimension(120, 30)); // 높이를 30으로
		
		filterPanel.add(cb_filter);
		p_title.add(la_title);
		p_titleBar.add(leftSpacer, BorderLayout.WEST);
		p_titleBar.add(p_title, BorderLayout.CENTER);
		p_titleBar.add(filterPanel, BorderLayout.EAST);
	}
	
	/**
	 * 재고 격자 생성
	 */
	private void createInventoryGrid() {
		/* ---------- 창고 시각화 격자 + 카테고리 라벨 (p_gridWrapper)---------- */

		// 가운데 정렬용 셀 격자를 감싸는 래퍼
		p_gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// 2행 N열 (1행: 각 열마다 재고 셀 (InventoryCell), 2행: 카테고리명 라벨)
		// 셀 사이간격 3px
		int cols = categoryOrder.size();
		p_grid = new JPanel(new GridLayout(2, cols, 3, 3));
		p_grid.setPreferredSize(new Dimension(600, 1200)); // 격자 전체 크기
		p_grid.setOpaque(false); // 배경 투명

		// 그리드 셀 생성
		createGridCells();
		
		p_gridWrapper.add(p_grid);
	}
	
	/**
	 * 그리드 셀들 생성
	 */
	private void createGridCells() {
		// grid 상단 (1행): 각 열에 InventoryCell 하나씩 생성 및 추가
		for (int col = 0; col < categoryOrder.size(); col++) {
			String category = categoryOrder.get(col);
			
			// 하나의 상위 카테고리(예: "상의") 안에 포함된 하위 카테고리(예: "반팔티")
			List<Product> productList = categoryProductMap.get(category);

			// 상위 카테고리 색상
			Color topCategoryColor = categoryColors.get(category);
			// 상위카테고리별로 색상이 다른 셀 생성
			InventoryCell cell = new InventoryCell(topCategoryColor); 
				
			// 하위카테고리별 누적 수량 및 상품 목록 (입력된 순서로 누적)
			Map<String, Integer> subCategoryCountMap = new LinkedHashMap<>();
			Map<String, List<Product>> subCategoryProductMap = new LinkedHashMap<>();
			
			for(Product p : productList) {
				String subCategoryName = p.getLocation().getBrand().getSubCategory().getSubCategoryName().trim();
				
				// 하위카테고리별 상품 목록 관리
				subCategoryProductMap.computeIfAbsent(subCategoryName, k -> new ArrayList<>()).add(p);
				
				for(ProductDetail detail : p.getProductDetails()) {
					int quantity = detail.getProductQuantity();
					// 기존값이 있으면 그걸 쓰고, 없으면 0 + 상품의 수량
					subCategoryCountMap.put(subCategoryName, subCategoryCountMap.getOrDefault(subCategoryName, 0) + quantity);
				}
			}
			
			// 하위 카테고리별 수량 및 상품 목록 기반으로 블록 추가
			for(Map.Entry<String, Integer> entry : subCategoryCountMap.entrySet()) {
				String subCategoryName = entry.getKey();
				int quantity = entry.getValue();
				
				// 하위카테고리별 색상 가져오기 (없으면 기본 색)
				Color subColor = subColorMap.getOrDefault(subCategoryName, Color.LIGHT_GRAY);
				
				// 해당 하위카테고리의 상품 목록 가져오기
				List<Product> subCategoryProducts = subCategoryProductMap.getOrDefault(subCategoryName, new ArrayList<>());

				// 상품 목록과 함께 블록 추가
				cell.addBlock(subCategoryName, quantity, subColor, subCategoryProducts);
			}
			
			// 부모 UI 참조 설정 (클릭 이벤트 전달용)
			cell.setParentUI(this);

			// 재고량 업데이트
			cell.renderBlocks(subColorMap);
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
	}
	
	/**
	 * 정렬 기능 설정 (main/view의 SortStrategy 패턴 활용)
	 */
	private void setupSortingFeature() {
		cb_filter.addActionListener(e -> {
			String selected = (String) cb_filter.getSelectedItem();
			
			if("재고량순".equals(selected)) {
				SortStrategy sortStrategy = new SortByQuantity();
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryOrder, products);
				refreshGrid(sortedCategoryOrder);
			}
			else if("출고순".equals(selected)) {
				SortStrategy sortStrategy = new SortByRecentShipment(products, inventoryLogs, InventoryLog.ChangeType.OUT, false);
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryOrder, products);
				refreshGrid(sortedCategoryOrder);
			}
			else if("입고일순".equals(selected)) {
				SortStrategy sortStrategy = new SortByRecentShipment(products, inventoryLogs, InventoryLog.ChangeType.IN, false);
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryOrder, products);
				refreshGrid(sortedCategoryOrder);
			}
		});
	}
	
	/**
	 * 정렬된 순서에 따라 그리드 새로고침
	 */
	private void refreshGrid(List<String> sortedCategoryOrder) {
		// 기존 그리드 내용 제거
		p_grid.removeAll();
		
		// 새로운 순서로 카테고리 데이터 재구성
		this.categoryOrder = sortedCategoryOrder;
		
		// 그리드 셀 재생성
		createGridCells();
		
		// UI 새로고침
		p_grid.revalidate();
		p_grid.repaint();
	}
	
	/**
	 * 레이아웃 구성
	 */
	private void buildLayout() {
		/* ------- 모든 구조 포함 Panel (p_left) : 로고/시계 , 제목/필터, 그리드 순------- */
		// 전체 영역을 수직으로 쌓기
		p_left = new JPanel();
		p_left.setLayout(new BoxLayout(p_left, BoxLayout.Y_AXIS)); // 수직정렬
		p_left.add(p_clockBar);
		p_left.add(p_titleBar);
		p_left.add(p_gridWrapper, BorderLayout.CENTER);
		add(p_left, BorderLayout.CENTER);
	}

	/**
	 * 현재 시간 라벨 컴포넌트를 외부에서 접근할 수 있도록 반환함
	 * - Clock 클래스에서 라벨 갱신 시 사용됨
	 * @return 현재 시간 표시용 JLabel
	 */
	public JLabel getLa_timeLabel() {
		return la_timeLabel;
	}

	/**
	 * 오른쪽 페이지 ProductShip 참조 설정
	 * @param productShip ProductShip 인스턴스
	 */
	public void setProductShipReference(ProductShip productShip) {
		this.productShip = productShip;
	}
	
	/**
	 * 선택된 상품 정보를 오른쪽 페이지로 전달
	 * @param selectedProduct 선택된 상품
	 */
	public void sendProductToRightPage(Product selectedProduct) {
		if (productShip != null) {
			productShip.fillProductInfo(selectedProduct);
			System.out.println("✅ 상품 정보 전달: " + selectedProduct.getProductName());
		}
	}
	
	/**
	 * 출고/입고 완료 후 실시간 데이터 새로고침 (애니메이션 포함)
	 */
	public void refreshInventoryData() {
		// 백그라운드에서 데이터 새로고침
		SwingUtilities.invokeLater(() -> {
			try {
				// DAO가 null인 경우 새로 생성
				if (topCategoryDAO == null) {
					topCategoryDAO = new TopCategoryDAO();
				}
				if (mainPageDataLoader == null) {
					mainPageDataLoader = new MainPageDataLoader();
				}
				if (categoryManager == null) {
					categoryManager = new CategoryManager();
				}
				
				// 모든 데이터 완전히 다시 로드
				topCategories = topCategoryDAO.selectAll();
				products = mainPageDataLoader.loadProducts();
				categoryManager.init(topCategories, products);
				categoryProductMap = categoryManager.categorizeProducts(products);
				
				// 카테고리 순서와 색상 다시 설정
				categoryOrder = categoryManager.getCategoryOrder();
				categoryColors = categoryManager.getCategoryColors();
				
				// 재고 로그 데이터도 새로고침
				try {
					ProductDetailDAO productDetailDAO = new ProductDetailDAO();
					inventoryLogs = productDetailDAO.selectAllInventoryLogsWithProductInfo();
				} catch (Exception logEx) {
					System.err.println("⚠️ 재고 로그 로딩 실패: " + logEx.getMessage());
					inventoryLogs = new ArrayList<>(); // 빈 리스트로 초기화
				}
				
				// 격자 새로고침 (애니메이션 포함)
				refreshGridWithAnimation();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * 애니메이션과 함께 격자 새로고침
	 */
	private void refreshGridWithAnimation() {
		// 기존 격자 페이드아웃 애니메이션
		Timer fadeOutTimer = new Timer(50, null);
		final float[] alpha = {1.0f};
		
		fadeOutTimer.addActionListener(e -> {
			alpha[0] -= 0.1f;
			if (alpha[0] <= 0.0f) {
				alpha[0] = 0.0f;
				fadeOutTimer.stop();
				
				// 격자 다시 생성
				p_grid.removeAll();
				createGridCells();
				
				// 페이드인 애니메이션
				startFadeInAnimation();
			}
			
			// 투명도 적용 (간단한 효과)
			p_grid.setOpaque(alpha[0] > 0.5f);
			p_grid.repaint();
		});
		
		fadeOutTimer.start();
	}
	
	/**
	 * 페이드인 애니메이션
	 */
	private void startFadeInAnimation() {
		Timer fadeInTimer = new Timer(50, null);
		final float[] alpha = {0.0f};
		
		fadeInTimer.addActionListener(e -> {
			alpha[0] += 0.1f;
			if (alpha[0] >= 1.0f) {
				alpha[0] = 1.0f;
				fadeInTimer.stop();
			}
			
			// 투명도 적용
			p_grid.setOpaque(alpha[0] > 0.5f);
			p_grid.repaint();
		});
		
		fadeInTimer.start();
	}
}
