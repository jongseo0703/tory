package com.sinse.tory.main.view;
/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//time 관련 패키지 임포트
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//스윙 관련 패키지 임포트
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

//메인페이지
public class MainPage extends JFrame {
	
	//상수
	private static final int ROWS = 10; //행
	private static final int BOX_SIZE = 60; //박스의 크기 (약간 줄여서 여백 확보)
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
	
	JPanel[] boxes; //박스들의 배열
	
	//TopCategory
	TopCategoryDAO topcategoryDAO;
	List<TopCategory> topCategories;
	
	//Product
	ProductDAO productDAO;
	List<Product> products;
	
	//각 상위카테고리별 색상을 매핑하는 categoryColors
	Map<String, Color> categoryColors;
	//각 상위카테고리 이름들을 가지고 있는 categoryOrder
	List<String> categoryOrder;
	//각 상위카테고리별 상품 개수를 저장하는 맵
	Map<String, Integer> productCountPerCategory;
	
	Map<JPanel, Product> boxProductMap = new LinkedHashMap<>();
	
	public MainPage() {
		//생성
		p_left = new JPanel(new BorderLayout());
		p_left_top = new JPanel(new BorderLayout());
		p_left_logo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_left_clock = new JPanel(new BorderLayout());
		p_right = new JPanel(); //오른쪽 빈 패널 생성
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
		//TopCategory 관련 생성
		topcategoryDAO = new TopCategoryDAO();
		topCategories = topcategoryDAO.selectAll();
		//Product 관련 생성
		productDAO = new ProductDAO();
		products = productDAO.selectAll();
		//각 카테고리별 색상을 매핑하는 categoryColors
		categoryColors = new LinkedHashMap<>();
		//각 카테고리 이름들을 가지고 있는 categoryOrder
		categoryOrder = new ArrayList<>();
		productCountPerCategory = new LinkedHashMap<>();
		//TopCategoryDAO로 가져온 topCategories에서 TopCategoryName을 가져와서 list에 저장
		for(TopCategory tc : topCategories) {
			categoryOrder.add(tc.getTopCategoryName());
		}
		// 1. 카테고리별 상품 리스트 초기화
		Map<String, List<Product>> categoryProductMap = new LinkedHashMap<>();
		for (String category : categoryOrder) {
		    categoryProductMap.put(category, new ArrayList<>());
		}
		// 2. 상품을 카테고리별로 분류
		for (Product p : products) {
		    String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
		    if (categoryProductMap.containsKey(topCategoryName)) {
		        categoryProductMap.get(topCategoryName).add(p);
		    }
		}
		//각 색상을 가진 배열
		Color[] colors = {
			new Color(255, 99, 71), new Color(60, 179, 113), new Color(65, 105, 225),
			new Color(238, 130, 238), new Color(255, 215, 0), new Color(70, 130, 180),
			new Color(255, 140, 0), new Color(123, 104, 238), new Color(199, 21, 133), 
			new Color(46, 139, 87)
		};
		//각 카테고리에 고유 색상 할당
		for(int i = 0; i < categoryOrder.size(); i++) {
			categoryColors.put(categoryOrder.get(i), colors[i % colors.length]);
		}
		//각 카테고리별 상품 개수 초기화
		for(String category : categoryOrder) {
			productCountPerCategory.put(category, 0);
		}
		//상품 목록을 순회하며 각 상품의 상위카테고리별로 개수를 누적
		for(Product p : products) {
			String topCategoryName = p.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName().trim();
			if(productCountPerCategory.containsKey(topCategoryName)) {
				int current = productCountPerCategory.get(topCategoryName);
				productCountPerCategory.put(topCategoryName, current + 1);
			}
		}
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
		//박스 패널 크기를 정확히 계산하여 정사각형 유지
		int totalBoxWidth = cols * BOX_SIZE + (cols - 1) * GAP;
		int totalBoxHeight = ROWS * BOX_SIZE + (ROWS - 1) * GAP;
		p_box.setLayout(new	GridLayout(ROWS + 1, cols, GAP, GAP));
		p_box.setBackground(Color.WHITE);
		p_box.setPreferredSize(new Dimension(totalBoxWidth, totalBoxHeight));
		p_box.setOpaque(false); //배경 투명
		//래퍼 패널 스타일 설정
		p_box_wrapper.setBackground(Color.WHITE);
		p_box_wrapper.setBorder(new EmptyBorder(200, 50, 120, 50)); //여백 조절(top, left, bottom, right)
		p_inventory_header.setBackground(Color.WHITE);
		p_inventory_header.setPreferredSize(new Dimension(totalBoxWidth, 50));
		la_inventory_title.setFont(new Font("Arial", Font.BOLD, 30));
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
		cb_sort.setFont(new Font("Arial", Font.PLAIN, 14));
		cb_sort.setBackground(Color.WHITE);
		cb_sort.setBorder(new EmptyBorder(5, 10, 5, 10));
		//오른쪽 영역 스타일(현재 빈공간)
		p_right.setBackground(Color.WHITE);
		p_right.setPreferredSize(new Dimension(820, 1024));
		int totalBoxes = (ROWS + 1) * cols; //행 X 열 = 전체 박스 개수
		boxes = new JPanel[totalBoxes]; //전체 박스 배열
		int productIndex = 0;
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < cols; col++) {
				int index = row * cols + col;
				JPanel box = new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						Graphics2D g2 = (Graphics2D) g.create();
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						int arc = 20;
						g2.setColor(getBackground());
						g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
						g2.setColor(Color.WHITE);
						g2.setStroke(new BasicStroke(2));
						g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);
						g2.dispose();
					}
				};
				box.setPreferredSize(new Dimension(BOX_SIZE, BOX_SIZE));
				box.setOpaque(true);
				box.setBackground(Color.decode("#39393B"));
				// 상품 매핑
		        String categoryName = categoryOrder.get(col);
		        List<Product> productList = categoryProductMap.get(categoryName);
		        int boxRow = ROWS - 1 - row;  // 아래에서부터 위로 채우는 row 인덱스
		        if (boxRow < productList.size()) {
		            Product product = productList.get(boxRow);
		            boxProductMap.put(box, product);
		        } else {
		            boxProductMap.put(box, null);
		        }
				box.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						Product clickedProduct = boxProductMap.get(box);
						if (clickedProduct != null) {
						    int productDetailId = clickedProduct.getProductDetails().get(0).getProductDetailId(); // 예시: 첫 번째 디테일
						    ProductDetailDAO productDetailDAO = new ProductDetailDAO();
						    ProductDetail detail = productDetailDAO.selectDetailInfo(productDetailId);
						    if (detail != null) {
						        String message = "사이즈: " + detail.getProductSizeName() +
						                         "\n수량: " + detail.getProductQuantity() +
						                         "\n설명: " + detail.getProduct().getDescription() +
						                         "\n이미지 URL: " + detail.getProduct().getProductImages().get(0).getImageURL(); // 예시
						        JOptionPane.showMessageDialog(null, message, "상품 상세", JOptionPane.INFORMATION_MESSAGE);
						    } else {
						        JOptionPane.showMessageDialog(null, "상세 정보를 불러올 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
						    }
						}
					}
				});
				p_box.add(box);
				boxes[index] = box;
			}
		}
		for(int col = 0; col < cols; col++) {
			String categoryName = categoryOrder.get(col);
			JLabel label = new JLabel(categoryName, SwingConstants.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 16));
			label.setForeground(categoryColors.get(categoryName));
			label.setPreferredSize(new Dimension(BOX_SIZE, BOX_SIZE));
			p_box.add(label);
			boxes[ROWS * cols + col] = null;
		}
		//1초(1000ms)마다 실행되는 타이머 생성
		Timer timerClock = new Timer(1000, e -> {
			//현재 날짜와 시간 정보 가져오기
			LocalDateTime now = LocalDateTime.now();
			//원하는 형식으로 
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:HH:mm");
			la_clock.setText("현재 날짜는 " + now.format(formatter));
			la_clock.setForeground(Color.decode("#393939"));
		});
		timerClock.start();
		Timer animateTimer = new Timer(100, null);
		int[] filled = new int[cols];
		animateTimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean more = false;
				for(int col = 0; col < cols; col++) {
					int count = Math.min(productCountPerCategory.getOrDefault(categoryOrder.get(col), 0), ROWS);
					if(filled[col] < count) {
						int row = ROWS - filled[col] - 1;
						int index = row * cols + col;
						if(index >= 0 && index < boxes.length && boxes[index] != null) {
							boxes[index].setBackground(categoryColors.get(categoryOrder.get(col)));
							boxes[index].repaint();
						}
						filled[col]++;
						more = true;
					}
				}
				if(!more) animateTimer.stop();
			}
		});
		animateTimer.start();
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
		add(p_left, BorderLayout.WEST);
		add(p_right, BorderLayout.EAST);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height); // 화면 가득
		setVisible(true); //화면 보여주기
		setLocationRelativeTo(null); //화면 중앙에 화면 띄우기
		setDefaultCloseOperation(EXIT_ON_CLOSE); //화면을 끄면 자동으로 종료됨
	}
	public void showDetail() {
	}
}
