package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

//스윙 관련 패키지 임포트
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.InventoryLog;
import com.sinse.tory.leftpage.view.InventoryCell;

public class MainPageLayoutBuilder {
	
	//상수
	private static final int ROWS = 10; //행
	private static final int CELL_SIZE = 65; //셀의 크기 (약간 줄여서 여백 확보)
	private static final int GAP = 0; //박스 간의 간격
	
	JPanel p_left; //왼쪽 영역
	JPanel p_left_top; //왼쪽 영역의 상단
	JPanel p_left_logo; //로고영역
	JPanel p_left_clock; //현재 시각 영역
	JPanel p_box; //박스들이 담길 패널
	JPanel p_box_wrapper; //박스 패널을 감싸는 패널
	JPanel p_inventory_header; //타이틀을 감싸는 패널
	JLabel la_inventory_title; //타이틀(재고현황)
	JLabel la_logo; //tory 로고
	JLabel la_clock; //현재 시각
	
	JPanel p_right; //오른쪽 영역
	
	//드롭다운 메뉴 배열
	String[] sortOptions = {"보기", "재고량순", "출고순", "회전율순", "입고일순"};
	JComboBox cb_sort; //정렬 드롭다운 박스
	
	List<Product> products;
	List<InventoryLog> inventoryLogs;
	CategoryManager categoryManager;
	
	// InventoryBox[] 배열은 더 이상 사용하지 않음 - InventoryCell 리스트 방식으로 변경됨
	
	ClockUpdater clockUpdater; //현재 날짜 가져오는 객체
	
	//왼쪽 페이지 생성하는 메서드
	public JPanel buildLeft(List<String> categoryOrder) {
		//생성
		p_left = new JPanel(new BorderLayout());
		p_left_top = new JPanel(new BorderLayout());
		p_left_logo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_left_clock = new JPanel(new BorderLayout());
		//GridLayout에 간격 설정으로 정사각형 모양 유지
		p_box = new JPanel();
		//박스 패널을 감싸는 래퍼 패널 생성 (FlowLayout 사용)
		p_box_wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		//로고 생성
		ImageIcon logo = new ImageIcon(getClass().getResource("/images/torylogo.png"));
		Image logoImage = logo.getImage();
		Image resizedImage = logoImage.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		ImageIcon resizedLogo = new ImageIcon(resizedImage);
		la_logo = new JLabel(resizedLogo);
		//현재 시각 생성
		la_clock = new JLabel();
		//재고현황 글씨를 감싸는 헤더영역 생성
		p_inventory_header = new JPanel(new BorderLayout());
		la_inventory_title = new JLabel("재고현황");
		//드롭다운 메뉴 배열로 드롭다운박스 생성
		cb_sort = new JComboBox<>(sortOptions);
		
		//현재 날짜 업데이트하는 메서드 호출
		clockUpdater = new ClockUpdater(la_clock);
		clockUpdater.start();
		
		//상위카테고리 이름의 개수
		int cols = categoryOrder.size();
		//스타일
		//왼쪽 영역 스타일
		p_left.setBackground(Color.WHITE);
		p_left.setPreferredSize(new Dimension(820, 1024));
		p_left_top.setPreferredSize(new Dimension(820, 100));
		//왼쪽 영역의 상단바 스타일
		p_left_top.setBackground(Color.WHITE);
		p_left_logo.setBackground(Color.WHITE);
		p_left_clock.setBackground(Color.WHITE);
		la_clock.setFont(new Font("Arial", Font.BOLD, 22));
		//셀 패널 크기를 정확히 계산
		int totalCellWidth = cols * CELL_SIZE + (cols - 1) * GAP;
		int totalCellHeight = ROWS * CELL_SIZE + (ROWS - 1) * GAP;
		p_box.setLayout(new	GridLayout(ROWS + 1, cols, GAP, GAP));
		p_box.setBackground(Color.WHITE);
		p_box.setPreferredSize(new Dimension(totalCellWidth, totalCellHeight));
		p_box.setOpaque(false); //배경 투명
		//래퍼 패널 스타일 설정
		p_box_wrapper.setBackground(Color.WHITE);
		p_box_wrapper.setBorder(new EmptyBorder(200, 50, 120, 50)); //여백 조절(top, left, bottom, right)
		p_inventory_header.setBackground(Color.WHITE);
		p_inventory_header.setPreferredSize(new Dimension(totalCellWidth, 50));
		la_inventory_title.setFont(new Font("Gulim", Font.BOLD, 30));
		la_inventory_title.setForeground(Color.decode("#393939"));
		la_inventory_title.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel leftSpacer = new JPanel();
		leftSpacer.setBackground(Color.WHITE);
		leftSpacer.setPreferredSize(new Dimension(120, 50));
		JPanel headerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerContainer.setBackground(Color.WHITE);
		headerContainer.setBorder(new EmptyBorder(50, 0, 0, 0));
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		cb_sort.setPreferredSize(new Dimension(120, 30));
		cb_sort.setFont(new Font("Gulim", Font.PLAIN, 14));
		cb_sort.setBackground(Color.WHITE);
		cb_sort.setBorder(new EmptyBorder(5, 10, 5, 10));
		
		cb_sort.addActionListener(e -> {
			String selected = (String) cb_sort.getSelectedItem();
			
			if("재고량순".equals(selected)) {
				SortStrategy sortStrategy = new SortByQuantity();
				
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryManager.getCategoryOrder(), products);
				
				Map<String, List<Product>> categorizedProducts = categoryManager.categorizeProducts(products);
				
				p_box.removeAll();
				
				InventoryCellFactory cellFactory = new InventoryCellFactory(ROWS, sortedCategoryOrder.size(), CELL_SIZE);
				List<InventoryCell> newCells = cellFactory.createCells(sortedCategoryOrder, categorizedProducts, categoryManager.getCategoryColors(), p_box);
				
				CellFillAnimator animator = new CellFillAnimator(newCells, sortedCategoryOrder, categoryManager.getCategoryColors(), categoryManager.getProductCountPerCategory(), ROWS, sortedCategoryOrder.size());
				animator.start();
				
				p_box.revalidate();
				p_box.repaint();
			}
			else if("출고순".equals(selected)) {
				SortStrategy sortStrategy = new SortByRecentShipment(products, inventoryLogs, InventoryLog.ChangeType.OUT, false);
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryOrder, products);
				Map<String, List<Product>> categorizedProducts = categoryManager.categorizeProducts(products);
				
				p_box.removeAll();
				
				InventoryCellFactory cellFactory = new InventoryCellFactory(ROWS, sortedCategoryOrder.size(), CELL_SIZE);
				List<InventoryCell> newCells = cellFactory.createCells(sortedCategoryOrder, categorizedProducts, categoryManager.getCategoryColors(), p_box);
				
				CellFillAnimator animator = new CellFillAnimator(newCells, sortedCategoryOrder, categoryManager.getCategoryColors(), categoryManager.getProductCountPerCategory(), ROWS, sortedCategoryOrder.size());
				animator.start();
				
				p_box.revalidate();
				p_box.repaint();
			}
			else if("입고일순".equals(selected)) {
				SortStrategy sortStrategy = new SortByRecentShipment(products, inventoryLogs, InventoryLog.ChangeType.IN, true);
				List<String> sortedCategoryOrder = sortStrategy.sort(categoryOrder, products);
				Map<String, List<Product>> categorizedProducts = categoryManager.categorizeProducts(products);
				
				p_box.removeAll();
				
				InventoryCellFactory cellFactory = new InventoryCellFactory(ROWS, sortedCategoryOrder.size(), CELL_SIZE);
				List<InventoryCell> newCells = cellFactory.createCells(sortedCategoryOrder, categorizedProducts, categoryManager.getCategoryColors(), p_box);
				
				CellFillAnimator animator = new CellFillAnimator(newCells, sortedCategoryOrder, categoryManager.getCategoryColors(), categoryManager.getProductCountPerCategory(), ROWS, sortedCategoryOrder.size());
				animator.start();
				
				p_box.revalidate();
				p_box.repaint();
			}
		});
		
		//부착
		p_left_logo.add(la_logo);
		p_left_clock.add(la_clock, BorderLayout.CENTER);
		p_left_top.add(p_left_logo, BorderLayout.WEST);
		p_left_top.add(p_left_clock, BorderLayout.EAST);
		p_left.add(p_left_top, BorderLayout.NORTH);
		p_inventory_header.add(leftSpacer, BorderLayout.WEST);
		p_inventory_header.add(la_inventory_title, BorderLayout.CENTER);
		p_inventory_header.add(cb_sort, BorderLayout.EAST);
		headerContainer.add(p_inventory_header);
		p_box_wrapper.add(p_box);
		centerPanel.add(headerContainer, BorderLayout.NORTH);
		centerPanel.add(p_box_wrapper, BorderLayout.CENTER);
		// 박스를 래퍼에 추가하고, 래퍼를 왼쪽 패널에 추가
		p_left.add(centerPanel, BorderLayout.CENTER);
		
		return p_left;
	}
	
	//오른쪽 페이지 생성하는 메서드
	public JPanel buildRight() {
		//생성
		p_right = new JPanel(); //오른쪽 빈 패널 생성
		
		//스타일
		//오른쪽 영역 스타일(현재 빈공간)
		p_right.setBackground(Color.WHITE);
		p_right.setPreferredSize(new Dimension(820, 1024));
		
		return p_right;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void setInventoryLogs(List<InventoryLog> inventoryLogs) {
		this.inventoryLogs = inventoryLogs;
	}
	
	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}
	
	public JPanel getBoxPanel() {
		return p_box;
	}
}
