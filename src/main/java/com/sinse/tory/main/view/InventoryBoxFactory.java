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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//스윙 관련 패키지 임포트
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.repository.ProductDetailDAO;

public class InventoryBoxFactory {
	
	InventoryBox[] boxes;
	Map<InventoryBox, Product> boxProductMap;
	int rows;
	int cols;
	int boxSize;
	
	public InventoryBoxFactory(int rows, int cols, int boxSize) {
		this.rows = rows;
		this.cols = cols;
		this.boxSize = boxSize;
		boxes = new InventoryBox[(rows + 1) * cols];
		boxProductMap = new LinkedHashMap<>();
	}
	
	public InventoryBox[] createBoxes(List<String> categoryOrder, Map<String, List<Product>> categoryProductMap, Map<String, Color> categoryColors, JPanel container) {

		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				int index = row * cols + col;
				InventoryBox box = new InventoryBox();
				box.setPreferredSize(new Dimension(boxSize, boxSize));
				box.setOpaque(true);
				box.setBackground(Color.WHITE);
				// 상품 매핑
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
