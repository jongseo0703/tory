package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
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
import javax.swing.JOptionPane;
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
 * 박스를 만들어 주는 클래스
 */
public class InventoryBoxFactory {
	
	InventoryBox[] boxes;
	Map<InventoryBox, Product> boxProductMap;
	int rows;
	int cols;
	int boxSize;
	
	/**
	 * 행, 열, 박스사이즈를 초기화하기 위한 생성자
	 */
	public InventoryBoxFactory(int rows, int cols, int boxSize) {
		this.rows = rows;
		this.cols = cols;
		this.boxSize = boxSize;
		boxes = new InventoryBox[(rows + 1) * cols];
		boxProductMap = new LinkedHashMap<>();
	}
	
	/**
	 * 박스를 만드는 메서드
	 */
	public InventoryBox[] createBoxes(List<String> categoryOrder, Map<String, List<Product>> categoryProductMap, Map<String, Color> categoryColors, JPanel container) {

		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				int index = row * cols + col;
				InventoryBox box = new InventoryBox();
				box.setPreferredSize(new Dimension(boxSize, boxSize));
				box.setOpaque(true);
				box.setBackground(Color.WHITE);
				//상품 매핑
		        String categoryName = categoryOrder.get(col);
		        List<Product> productList = categoryProductMap.get(categoryName);
		        int boxRow = rows - 1 - row;  // 아래에서부터 위로 채우는 row 인덱스
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
						    int productDetailId = clickedProduct.getProductDetails().get(0).getProductDetailId();
						    ProductDetailDAO productDetailDAO = new ProductDetailDAO();
						    ProductDetail detail = productDetailDAO.selectDetailInfo(productDetailId);
						    
						    ProductDAO productDAO = new ProductDAO();
						    List<Product> products = productDAO.select(detail.getProduct().getProductId());
						    
						    Product product = products.get(0);
						    
						    //이미지 경로 및 아이콘
						    String imgPath = detail.getProduct().getProductImages().get(0).getImageURL();
							ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
							
							//원하는 크기로 리사이즈된 아이콘
							Image img = icon.getImage();
							Image scaledImg = img.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
							ImageIcon resizedIcon = new ImageIcon(scaledImg);
							
							//이미지, 상품명, 사이즈, 수량, 가격, 설명 라벨
							JLabel imgLabel = new JLabel(resizedIcon);
							JLabel nameLabel = new JLabel("상품명 : " + product.getProductName());
							JLabel sizeLabel = new JLabel("사이즈 : " + detail.getProductSizeName());
							JLabel quantityLabel = new JLabel("수량 : " + detail.getProductQuantity() + "개");
							JLabel priceLabel = new JLabel("가격 : " + product.getProductPrice() + "원");
							JLabel descriptionLabel = new JLabel(detail.getProduct().getDescription());
						    
							//팝업창 패널
							JPanel panel = new JPanel();
							
							//팝업창 스타일
							panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
							panel.setBackground(Color.decode("#39393B"));
							panel.setPreferredSize(new Dimension(450, 500));
							
							//요소들을 감싸는 패널
							JPanel contentPanel = new JPanel();
							
							//BoxLayout으로 요소들이 차곡차곡 쌓이게 배치
							contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
							//요소들이 중앙정렬하게
							contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
							contentPanel.setOpaque(false);
							
							//nameLabel 스타일
							nameLabel.setFont(new Font("Gulim", Font.BOLD, 16));
							nameLabel.setForeground(Color.WHITE);
							//sizeLabel 스타일
							sizeLabel.setFont(new Font("Gulim", Font.BOLD, 16));
							sizeLabel.setForeground(Color.WHITE);
							//quantityLabel 스타일
							quantityLabel.setFont(new Font("Gulim", Font.BOLD, 16));
							quantityLabel.setForeground(Color.WHITE);
							//priceLabel 스타일
							priceLabel.setFont(new Font("Gulim", Font.BOLD, 16));
							priceLabel.setForeground(Color.WHITE);
							//descriptionLabel 스타일
							descriptionLabel.setFont(new Font("Gulim", Font.BOLD, 16));
							descriptionLabel.setForeground(Color.WHITE);
							
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
						}
					}
				});
				container.add(box);
				boxes[index] = box;
			}
		}
		for(int col = 0; col < cols; col++) {
			String categoryName = categoryOrder.get(col);
			JLabel label = new JLabel(categoryName, SwingConstants.CENTER);
			label.setFont(new Font("Gulim", Font.BOLD, 16));
			label.setForeground(categoryColors.get(categoryName));
			label.setPreferredSize(new Dimension(boxSize, boxSize));
			container.add(label);
			boxes[rows * cols + col] = null;
		}
		
		return boxes;
		
	}

}
