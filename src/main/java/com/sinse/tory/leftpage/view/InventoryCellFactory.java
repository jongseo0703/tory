package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//스윙 관련 패키지 임포트
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.ProductDetailDAO;

/**
 * InventoryCell을 만들어 주는 클래스
 */
public class InventoryCellFactory {
	
	List<InventoryCell> cells;
	Map<InventoryCell, Product> cellProductMap;
	int rows;
	int cols;
	int cellSize;
	
	/**
	 * 행, 열, 셀 사이즈를 초기화하기 위한 생성자
	 */
	public InventoryCellFactory(int rows, int cols, int cellSize) {
		this.rows = rows;
		this.cols = cols;
		this.cellSize = cellSize;
		cells = new ArrayList<>();
		cellProductMap = new LinkedHashMap<>();
	}
	
	/**
	 * InventoryCell들을 만드는 메서드
	 */
	public List<InventoryCell> createCells(List<String> categoryOrder, Map<String, List<Product>> categoryProductMap, Map<String, Color> categoryColors, JPanel container) {

		// 기존 컨테이너 정리
		container.removeAll();
		cells.clear();
		cellProductMap.clear();
		
		// 각 카테고리별로 InventoryCell 생성
		for(int col = 0; col < categoryOrder.size(); col++) {
			String categoryName = categoryOrder.get(col);
			List<Product> productList = categoryProductMap.get(categoryName);
			Color categoryColor = categoryColors.get(categoryName);
			
			// 상위 카테고리 색상으로 InventoryCell 생성
			InventoryCell cell = new InventoryCell(categoryColor);
			cell.setPreferredSize(new Dimension(cellSize, cellSize * rows)); // 높이는 rows만큼
			
			// 하위카테고리별 상품 분류 및 수량 계산
			Map<String, Integer> subCategoryCountMap = new LinkedHashMap<>();
			
			for(Product p : productList) {
				String subCategoryName = p.getLocation().getBrand().getSubCategory().getSubCategoryName().trim();
				for(ProductDetail detail : p.getProductDetails()) {
					int quantity = detail.getProductQuantity();
					subCategoryCountMap.put(subCategoryName, subCategoryCountMap.getOrDefault(subCategoryName, 0) + quantity);
				}
			}
			
			// 하위 카테고리별 블록 추가
			Map<String, Color> subColorMap = new LinkedHashMap<>();
			for(Map.Entry<String, Integer> entry : subCategoryCountMap.entrySet()) {
				String subCategoryName = entry.getKey();
				int quantity = entry.getValue();
				
				// 하위카테고리 색상은 상위카테고리 색상 기반으로 설정
				Color subColor = categoryColor;
				subColorMap.put(subCategoryName, subColor);
				
				cell.addBlock(subCategoryName, quantity, subColor);
			}
			
			// 상품 상세보기 클릭 이벤트 추가
			if (!productList.isEmpty()) {
				Product representativeProduct = productList.get(0); // 대표 상품
				cellProductMap.put(cell, representativeProduct);
				
				cell.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						showProductDetail(e, representativeProduct);
					}
				});
			}
			
			// 렌더링 및 컨테이너에 추가
			cell.renderBlocks(subColorMap);
			container.add(cell);
			cells.add(cell);
		}
		
		// 카테고리 라벨 추가 (하단)
		for(int col = 0; col < categoryOrder.size(); col++) {
			String categoryName = categoryOrder.get(col);
			JLabel label = new JLabel(categoryName, SwingConstants.CENTER);
			label.setFont(new Font("Gulim", Font.BOLD, 16));
			label.setForeground(categoryColors.get(categoryName));
			label.setPreferredSize(new Dimension(cellSize, 30)); // 라벨 높이는 30px
			container.add(label);
		}
		
		return cells;
	}
	
	/**
	 * 상품 상세 정보 팝업 표시
	 */
	private void showProductDetail(MouseEvent e, Product product) {
		if (product == null) return;
		
		try {
			int productDetailId = product.getProductDetails().get(0).getProductDetailId();
			ProductDetailDAO productDetailDAO = new ProductDetailDAO();
			ProductDetail detail = productDetailDAO.selectDetailInfo(productDetailId);
			
			ProductDAO productDAO = new ProductDAO();
			List<Product> products = productDAO.select(detail.getProduct().getProductId());
			
			Product detailedProduct = products.get(0);
			
			// 이미지 경로 및 아이콘
			String imgPath = detail.getProduct().getProductImages().get(0).getImageURL();
			ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
			
			// 원하는 크기로 리사이즈된 아이콘
			Image img = icon.getImage();
			Image scaledImg = img.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(scaledImg);
			
			// 이미지, 상품명, 사이즈, 수량, 가격, 설명 라벨
			JLabel imgLabel = new JLabel(resizedIcon);
			JLabel nameLabel = new JLabel("상품명 : " + detailedProduct.getProductName());
			JLabel sizeLabel = new JLabel("사이즈 : " + detail.getProductSizeName());
			JLabel quantityLabel = new JLabel("수량 : " + detail.getProductQuantity() + "개");
			JLabel priceLabel = new JLabel("가격 : " + detailedProduct.getProductPrice() + "원");
			JLabel descriptionLabel = new JLabel(detail.getProduct().getDescription());
			
			// 팝업창 패널
			JPanel panel = new JPanel();
			
			// 팝업창 스타일
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(Color.decode("#39393B"));
			panel.setPreferredSize(new Dimension(450, 500));
			
			// 요소들을 감싸는 패널
			JPanel contentPanel = new JPanel();
			
			// BoxLayout으로 요소들이 차곡차곡 쌓이게 배치
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			// 요소들이 중앙정렬하게
			contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPanel.setOpaque(false);
			
			// 라벨 스타일 설정
			Font labelFont = new Font("Gulim", Font.BOLD, 16);
			Color labelColor = Color.WHITE;
			
			nameLabel.setFont(labelFont);
			nameLabel.setForeground(labelColor);
			sizeLabel.setFont(labelFont);
			sizeLabel.setForeground(labelColor);
			quantityLabel.setFont(labelFont);
			quantityLabel.setForeground(labelColor);
			priceLabel.setFont(labelFont);
			priceLabel.setForeground(labelColor);
			descriptionLabel.setFont(labelFont);
			descriptionLabel.setForeground(labelColor);
			
			contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
			contentPanel.add(imgLabel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 37)));
			contentPanel.add(nameLabel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
			contentPanel.add(sizeLabel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
			contentPanel.add(quantityLabel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
			contentPanel.add(priceLabel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
			contentPanel.add(descriptionLabel);
			
			panel.add(contentPanel);
			
			JDialog dialog = new JDialog();
			dialog.setTitle("상품 상세 정보");
			dialog.setModal(true);
			dialog.getContentPane().add(panel);
			dialog.pack();
			dialog.setLocation(e.getXOnScreen() - dialog.getWidth() / 2, e.getYOnScreen() - dialog.getHeight());
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
} 