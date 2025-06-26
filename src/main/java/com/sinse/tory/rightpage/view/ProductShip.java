package com.sinse.tory.rightpage.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.ProductImage;
import com.sinse.tory.db.repository.ProductDetailDAO;

import com.sinse.tory.rightpage.identifier.IdentifierUpdateWithNameComboBox;
import com.sinse.tory.rightpage.util.PageUtil;
import com.sinse.tory.rightpage.util.Pages;
import com.sinse.tory.rightpage.util.ProductImageDAO;
import com.sinse.tory.rightpage.util.UpdateCount;
import com.sinse.tory.leftpage.view.InventoryUI;
/*
 * 입고와 출고 기능이 있는 페이지
 * */
public class ProductShip extends Pages{
	JButton[]bt = new JButton[4];//버튼 4개 생성
	@SuppressWarnings("rawtypes")
	JComboBox[] box = new JComboBox[3];//콤보박스 3개
	JLabel[]la = new JLabel[4];//라벨 4개
	JTextField t_count;
	JPanel p_img;
	JPanel p_form; // 상위카테고리,하위카테고리,상품명이 부착될 곳
	JPanel[]location = new JPanel[3]; 
	Product product;
	ProductDetail productDetail;
	ProductImage productImage;
	ProductDetailDAO productDetailDAO;
	ProductImageDAO imageDAO;
	int itemId =0;
  int num =0;//t_count 글자 초기화
  
  // 왼쪽 InventoryUI와의 실시간 연동을 위한 참조
  private InventoryUI inventoryUI;
  
	public ProductShip(Object testmain, Object dataModificationPage) {
		super(testmain);
		t_count = new JTextField();
		p_form = new JPanel();
		productDetail = new ProductDetail();
		productDetailDAO = new ProductDetailDAO();
		productImage = new ProductImage();
		imageDAO = new ProductImageDAO();
		
		/*
		 * 패널[0]= 상품정보 수정과 추가 버튼
		 * 패널[1]= 이미지 패널과 폼 패널의 들어갈곳
		 * 패널[2]= 수량 라벨,텍스트박스, 입고 및 출고 버튼 2개
		 * */
		for(int i=0;i<location.length;i++) {
			location[i] = new JPanel();
			location[i].setBackground(null);
		}
		
		// 각각 하나씩 자동 생성
		for(int i=0; i<bt.length;i++) {
			bt[i] = new JButton() {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(getBackground());
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
				    g2.dispose();
				    super.paintComponent(g);
				}
				@Override
				public void updateUI() {
					super.updateUI();
					setOpaque(false);
				}
			};
			bt[i].setContentAreaFilled(false);
			bt[i].setFocusPainted(false);
			bt[i].setBorderPainted(false);
		}
		for(int i=0; i<la.length;i++) {
			la[i] = new JLabel();
		}
		
		for(int i=0; i<box.length;i++) {
			box[i] = 
			new JComboBox() {
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(getBackground());
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				    g2.dispose();
				    super.paintComponent(g);
				}
				@Override
				public void updateUI() {
					super.updateUI();
					setBorder(BorderFactory.createEmptyBorder());
					setOpaque(false);
				}
			};
		}
		
		//상품이미지 출력
		p_img = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				URL url = null;
				
				if(productImage !=null && productImage.getImageURL() != null) {
					String path = productImage.getImageURL();
					//이미지경로에서 맨 앞에 있는 '/'제거
					path = path.replaceFirst("^/", "");
					url = this.getClass().getClassLoader().getResource(path); // 상품이미지 위치
				}
				if(url == null) {					
					url =this.getClass().getClassLoader().getResource("images/not-found.png");//이미지가 없을 경우 
				}
				
				if(url != null) {
					try {
						BufferedImage buffer = ImageIO.read(url);
						// 이미지 크기 계산 (패널 크기에 맞춰서 비율 유지)
						int panelWidth = getWidth();
						int panelHeight = getHeight();
						int imgWidth = buffer.getWidth();
						int imgHeight = buffer.getHeight();
						
						// 비율 계산
						double scaleX = (double) panelWidth / imgWidth;
						double scaleY = (double) panelHeight / imgHeight;
						double scale = Math.min(scaleX, scaleY) * 0.8; // 80% 크기로 여백 확보
						
						int scaledWidth = (int) (imgWidth * scale);
						int scaledHeight = (int) (imgHeight * scale);
						
						// 중앙 정렬 계산
						int x = (panelWidth - scaledWidth) / 2;
						int y = (panelHeight - scaledHeight) / 2;
						
						Image scaledImage = buffer.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
						g2.drawImage(scaledImage, x, y, this);
						
						// 이미지 테두리 그리기
						g2.setColor(new Color(220, 220, 220));
						g2.setStroke(new BasicStroke(2));
						g2.drawRoundRect(x-2, y-2, scaledWidth+4, scaledHeight+4, 10, 10);
						repaint();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		// 이미지 패널 스타일링
		p_img.setBackground(Color.WHITE);
		p_img.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
			BorderFactory.createEmptyBorder(15, 15, 15, 15)
		));
		p_img.setPreferredSize(new Dimension(300, 280));
		
		// 폰트설정
		Font labelFont = new Font("맑은 고딕", Font.PLAIN, 14);
		Font buttonFont = new Font("맑은 고딕", Font.BOLD, 13);
		
		// 라벨 스타일링
		for(int i=0;i<la.length-1;i++) { // 마지막 수량 라벨 제외
			la[i].setFont(labelFont);
			la[i].setForeground(new Color(70, 70, 70));
		}
		la[3].setFont(labelFont); // 수량 라벨
		la[3].setForeground(new Color(70, 70, 70));
		
		// 콤보박스 스타일링
		for(int i=0;i<box.length;i++) {
			box[i].setFont(labelFont);
			box[i].setBackground(Color.WHITE);
		}
		
		// 버튼 스타일링
		for(int i=0;i<bt.length;i++) {
			bt[i].setFont(buttonFont);
			bt[i].setFocusPainted(false);
			bt[i].setBorderPainted(false);
			bt[i].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		}
		
		// 텍스트 필드 스타일링
		t_count.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		t_count.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
			BorderFactory.createEmptyBorder(10, 15, 10, 15)
		));
		t_count.setBackground(Color.WHITE);
		
		bt[0].setText("입출고 내역조회"); 
		bt[1].setText("상품추가");
		bt[2].setText("출고");
		bt[3].setText("입고");
		
		la[0].setText("상위카테고리");
		la[1].setText("하위카테고리");
		la[2].setText("상품명");
		la[3].setText("수량");
		
		// 패널들의 크기 (넉넉하게 조정)
		location[0].setPreferredSize(new Dimension(PageUtil.InputOutput_Width, 60));
		location[1].setPreferredSize(new Dimension(PageUtil.InputOutput_Width, 350));
		location[2].setPreferredSize(new Dimension(PageUtil.InputOutput_Width, 80));
		
		// 콤보박스와 라벨의 크기 (적절하게 조정)
		Dimension comboSize = new Dimension(280, 40);
		Dimension labelSize = new Dimension(120, 30);
		for(int i=0; i<box.length;i++) {
			la[i].setPreferredSize(labelSize);
			la[i].setMaximumSize(labelSize);
			box[i].setPreferredSize(comboSize);
			box[i].setMaximumSize(comboSize);
		}
		
		// 수량 라벨과 텍스트박스 크기 설정
		la[3].setPreferredSize(new Dimension(80, 40));
		t_count.setPreferredSize(new Dimension(120, 40));
		t_count.setMaximumSize(new Dimension(120, 40));
		
		// 버튼들의 크기 설정 (더 현대적인 크기)		
		Dimension actionButtonSize = new Dimension(80, 40);
		bt[2].setPreferredSize(actionButtonSize);
		bt[2].setMaximumSize(actionButtonSize);
		bt[3].setPreferredSize(actionButtonSize);
		bt[3].setMaximumSize(actionButtonSize);
		
		// 배경색 설정
		Color lightGray = new Color(248, 249, 250);
		Color primaryBlue = new Color(52, 144, 220);
		Color successGreen = new Color(40, 167, 69);
		Color warningOrange = new Color(255, 149, 0);
		
		bt[0].setBackground(lightGray);
		bt[0].setForeground(new Color(70, 70, 70));
		
		bt[1].setBackground(primaryBlue);
		bt[1].setForeground(Color.WHITE);
		
		bt[2].setBackground(warningOrange);
		bt[2].setForeground(Color.WHITE);
		
		bt[3].setBackground(successGreen);
		bt[3].setForeground(Color.WHITE);
		
		// 폼 패널 배경 설정
		p_form.setBackground(Color.WHITE);
		p_form.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)
		));
		
		//비활성화 기능
		//상위카테고리의 값이 정해지기 전까지만 비활성화
		//값이 차례대로 입력될 수 있도록 
		IdentifierUpdateWithNameComboBox identifierUpdateWithNameComboBox = new IdentifierUpdateWithNameComboBox(box[0], box[1], box[2]);
		box[2].addItemListener((ItemEvent e)->{
			if(e.getStateChange() == ItemEvent.SELECTED && box[2].getSelectedIndex()>0) {				
				t_count.setEnabled(true);//수량을 적을 텍스트박스			 
				itemId = identifierUpdateWithNameComboBox.getItemID(); // product_id 받기
				productDetail = productDetailDAO.selectDetailInfo(itemId);
				productImage = imageDAO.selectAll(itemId);
				t_count.setText(Integer.toString(productDetailDAO.selectCurrentQuantity(itemId)));
        bt[2].setEnabled(true);
				bt[3].setEnabled(true);
			}

		});
		
		bt[2].setEnabled(false);
		bt[3].setEnabled(false);
		t_count.setEnabled(false);//수량을 적을 텍스트박스			 
		
		// 조립
		// 상단 버튼 영역 (location[0])
		location[0].setLayout(new BoxLayout(location[0], BoxLayout.X_AXIS));
		location[0].setBackground(new Color(250, 251, 252));
		location[0].setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
			BorderFactory.createEmptyBorder(15, 20, 15, 20)
		));
		location[0].add(Box.createHorizontalGlue());
		location[0].add(bt[0]);
		location[0].add(Box.createRigidArea(new Dimension(15, 0)));
		location[0].add(bt[1]);

		// 메인 컨텐츠 영역 (location[1]) - 이미지와 폼을 나란히 배치
		location[1].setLayout(new GridLayout(1, 2, 20, 0));
		location[1].setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		location[1].setBackground(Color.WHITE);
		
		// 폼 패널 레이아웃 개선
		p_form.setLayout(new BoxLayout(p_form, BoxLayout.Y_AXIS));
		
		// 각 입력 필드를 위한 개별 패널 생성
		for(int i=0; i<box.length; i++) {
			JPanel fieldPanel = new JPanel();
			fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
			fieldPanel.setBackground(Color.WHITE);
			fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			la[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			box[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			
			fieldPanel.add(la[i]);
			fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
			fieldPanel.add(box[i]);
			
			p_form.add(fieldPanel);
			if(i < box.length - 1) {
				p_form.add(Box.createRigidArea(new Dimension(0, 20)));
			}
		}
		
		location[1].add(p_img);
		location[1].add(p_form);

		// 하단 액션 영역 (location[2]) - 수량과 버튼들
		location[2].setLayout(new BoxLayout(location[2], BoxLayout.X_AXIS));
		location[2].setBackground(new Color(250, 251, 252));
		location[2].setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)
		));
		
		// 수량 입력 패널
		JPanel quantityPanel = new JPanel();
		quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
		quantityPanel.setBackground(new Color(250, 251, 252));
		quantityPanel.add(la[3]);
		quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		quantityPanel.add(t_count);
		
		// 액션 버튼 패널
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
		actionPanel.setBackground(new Color(250, 251, 252));
		actionPanel.add(bt[2]);
		actionPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		actionPanel.add(bt[3]);
		
		location[2].add(quantityPanel);
		location[2].add(Box.createHorizontalGlue());
		location[2].add(actionPanel);
		
		bt[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 바로 입출고 내역 페이지로 이동
				showInventoryLogHistoryPage();
			}
		});
		bt[1].addActionListener(new ActionListener() {
			 // 상품추가로 가는 버튼
			@Override
			public void actionPerformed(ActionEvent e) {
				// MainPage의 public 메서드를 통해 ProductAddPage로 전환
				if (testmain != null) {
					try {
						testmain.getClass().getMethod("showProductAddPage").invoke(testmain);
						System.out.println("✅ ProductAddPage로 전환됨");
					} catch (Exception ex) {
						ex.printStackTrace();
						System.err.println("❌ 페이지 전환 실패: " + ex.getMessage());
					}
				}
			}
		});

		//t_count에 숫자만 입력했는지 확인
		t_count.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() !=KeyEvent.VK_ENTER && e.getKeyCode() !=KeyEvent.VK_BACK_SPACE) {
					if(t_count.getText() != null) {
						checkNumber(t_count.getText());	
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if(t_count.getText().length()>8) e.consume();
			}
		});
		UpdateCount updateCount = new UpdateCount();
		//t_count 안에 원하는 입출고 수량을 입력
			bt[2].addActionListener(e->{
				//출고버튼
				boolean resutle = ShowMessage.showConfirm(ProductShip.this,"출고하기","출고 하시겠습니까?");
				int count =0;
				// 확인 눌렀을때
				if(resutle) {
					//change_type 중 OUT
					String inOut = "OUT";
					//보유한 수량보다 출고수량이 크거나 출고가 0이 아닐때만 수행하도록 조건부여
					if(productDetail.getProductQuantity() != 0
							&&productDetail.getProductQuantity()>=Integer.parseInt(t_count.getText())
							&&num!=0 ) {
											count = productDetail.getProductQuantity()-num;						 							 
					updateCount.update(count, itemId);
					updateCount.dateInsert(inOut, num, itemId);
					
					// 출고 음성 재생
					playAudioFile("출고.wav");
					
					// 왼쪽 InventoryUI 실시간 업데이트
					String productName = box[2].getSelectedItem().toString();
					notifyInventoryUpdate("출고", num, productName);
					
					//상위 카테고리 초기화하면서 입출고 버튼 비활성화
					resetCombo();

					} else if (Integer.parseInt(t_count.getText())==0) {
						JOptionPane.showMessageDialog(ProductShip.this, "출고 수량을 다시 입력해 주세요");
					}
					else {
						JOptionPane.showMessageDialog(ProductShip.this, "재고부족\n 현재 재고량:" + productDetail.getProductQuantity());
					}
					System.out.println(itemId);
				}
			 });
			 bt[3].addActionListener(e->{
				boolean resutle = ShowMessage.showConfirm(ProductShip.this,"입고하기","입고 하시겠습니까?");
				// 확인 눌렀을때
				if(resutle) {
					//change_type 중 IN
					String inOut = "IN";
					// 입고수량이 0이 아닐때만 수행하도록 조건부여
					if(num !=0) {
					 					 	int count = productDetail.getProductQuantity()+ num;
					updateCount.update(count, itemId);
					updateCount.dateInsert(inOut, num, itemId);
					
					// 입고 음성 재생
					playAudioFile("입고.wav");
					
					// 왼쪽 InventoryUI 실시간 업데이트
					String productName = box[2].getSelectedItem().toString();
					notifyInventoryUpdate("입고", num, productName);
					
					resetCombo();
					} else {
						JOptionPane.showMessageDialog(ProductShip.this, "입고 수량을 다시 입력해 주세요");						
					}
					System.out.println(itemId);
				}
			});

		add(Box.createRigidArea(new Dimension(0,40)));
		for(int i =0;i<location.length;i++) {
			add(location[i]);
			location[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			if(i==0) add(Box.createRigidArea(new Dimension(0,10)));
			if(i==1) {
				add(Box.createRigidArea(new Dimension(0,20)));
			}
		}
		add(Box.createRigidArea(new Dimension(0,40)));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
	}
	
	/*
	 * 상위 콤보박스의 index를 0으로 돌아게 하고 다른 기능들 비활성화
	 * 이미지 경로도 초기화
	 * */
	public void resetCombo() {
		box[0].setSelectedIndex(0);
		t_count.setText("");
		if(box[0].getSelectedIndex()==0) {
			bt[2].setEnabled(false);
			bt[3].setEnabled(false);
			t_count.setEnabled(false);
			productImage = null;
			p_img.repaint();
		}
	}
	/*
	 * t_count에 숫자만 입력할 수 있도록 제안을 둔다
	 * */
	public boolean checkNumber(String text) {
		try {
			num = Integer.parseInt(text);
			return true;
		}catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(ProductShip.this,"숫자를 입력해 주세요");
			t_count.setText("");
			return false;
		}
		
	}
	
	/**
	 * 왼쪽 InventoryCell에서 클릭한 상품 정보로 오른쪽 폼을 자동 채우는 메서드
	 * @param selectedProduct 선택된 상품
	 */
	public void fillProductInfo(Product selectedProduct) {
		if (selectedProduct == null) return;
		
		try {
			// 상품 정보 추출
			String topCategoryName = selectedProduct.getLocation().getBrand().getSubCategory().getTopCategory().getTopCategoryName();
			String subCategoryName = selectedProduct.getLocation().getBrand().getSubCategory().getSubCategoryName();
			String productName = selectedProduct.getProductName();

			// 상위 카테고리 설정
			for (int i = 0; i < box[0].getItemCount(); i++) {
				if (box[0].getItemAt(i).toString().equals(topCategoryName)) {
					box[0].setSelectedIndex(i);
					break;
				}
			}
			
			// 잠시 대기 후 하위 카테고리 설정 (콤보박스 업데이트 시간 확보)
			javax.swing.SwingUtilities.invokeLater(() -> {
				for (int i = 0; i < box[1].getItemCount(); i++) {
					if (box[1].getItemAt(i).toString().equals(subCategoryName)) {
						box[1].setSelectedIndex(i);
						break;
					}
				}
				
				// 상품명 설정
				javax.swing.SwingUtilities.invokeLater(() -> {
					for (int i = 0; i < box[2].getItemCount(); i++) {
						if (box[2].getItemAt(i).toString().contains(productName)) {
							box[2].setSelectedIndex(i);
							break;
						}
					}
					
					// 상품 상세 정보 로드
					if (!selectedProduct.getProductDetails().isEmpty()) {
						ProductDetail detail = selectedProduct.getProductDetails().get(0);
						itemId = detail.getProductDetailId();
						
						// 상품 정보 업데이트
						productDetail = productDetailDAO.selectDetailInfo(itemId);
						productImage = imageDAO.selectAll(itemId);
						
						// 현재 수량 표시
						int currentQuantity = productDetailDAO.selectCurrentQuantity(itemId);
						t_count.setText(String.valueOf(currentQuantity));
						
						// 버튼들 활성화
						t_count.setEnabled(true);
						bt[2].setEnabled(true); // 출고 버튼
						bt[3].setEnabled(true); // 입고 버튼
						
						// 이미지 패널 새로고침
						p_img.repaint();
						
						System.out.println("✅ 상품 정보 자동 채우기 완료: " + productName);
					}
				});
			});
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("❌ 상품 정보 자동 채우기 실패: " + e.getMessage());
		}
	}
	
	/**
	 * InventoryUI 참조 설정 (실시간 업데이트용)
	 * @param inventoryUI 왼쪽 InventoryUI 참조
	 */
	public void setInventoryUIReference(InventoryUI inventoryUI) {
		this.inventoryUI = inventoryUI;
		System.out.println("✅ ProductShip에 InventoryUI 참조 연결됨");
	}
	
	/**
	 * 출고/입고 완료 후 왼쪽 InventoryUI 실시간 업데이트
	 * @param action 수행된 작업 ("출고" 또는 "입고")
	 * @param quantity 수량
	 * @param productName 상품명
	 */
	private void notifyInventoryUpdate(String action, int quantity, String productName) {
		if (inventoryUI != null) {
			// 성공 메시지와 함께 애니메이션 업데이트
			SwingUtilities.invokeLater(() -> {
				System.out.println("🔄 " + action + " 완료: " + productName + " " + quantity + "개");
				
				// InventoryUI 실시간 새로고침 (애니메이션 포함)
				inventoryUI.refreshInventoryData();
				
				// 성공 알림 (선택사항)
				showSuccessNotification(action, quantity, productName);
			});
		}
	}
	
	/**
	 * 성공 알림 표시 (토스트 스타일)
	 */
	private void showSuccessNotification(String action, int quantity, String productName) {
		JWindow notification = new JWindow();
		notification.setAlwaysOnTop(true);
		
		// 메인 패널 생성
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(40, 167, 69));
		mainPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(34, 139, 58), 3),
			BorderFactory.createEmptyBorder(20, 30, 20, 30)
		));
		
		// 성공 아이콘과 제목
		JLabel titleLabel = new JLabel("✅ " + action + " 성공!");
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// 상품 정보
		JLabel productLabel = new JLabel(productName);
		productLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		productLabel.setForeground(new Color(240, 255, 240));
		productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// 수량 정보
		JLabel quantityLabel = new JLabel("수량: " + quantity + "개");
		quantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		quantityLabel.setForeground(new Color(200, 255, 200));
		quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// 구성 요소 추가
		mainPanel.add(titleLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPanel.add(productLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(quantityLabel);
		
		notification.add(mainPanel);
		notification.pack();
		
		// 화면 중앙 상단에 표시
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - notification.getWidth()) / 2;
		int y = 100; // 상단에서 100px 아래
		notification.setLocation(x, y);
		
		// 슬라이드 다운 애니메이션
		slideDownAnimation(notification, y);
		
		// 4초 후 슬라이드 업 애니메이션과 함께 사라짐
		Timer timer = new Timer(4000, e -> slideUpAnimation(notification, y));
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * 슬라이드 다운 애니메이션
	 */
	private void slideDownAnimation(JWindow notification, int targetY) {
		notification.setVisible(true);
		int startY = targetY - 100; // 위에서 시작
		notification.setLocation(notification.getX(), startY);
		
		Timer slideTimer = new Timer(20, null);
		final int[] currentY = {startY};
		
		slideTimer.addActionListener(e -> {
			currentY[0] += 5;
			if (currentY[0] >= targetY) {
				currentY[0] = targetY;
				slideTimer.stop();
			}
			notification.setLocation(notification.getX(), currentY[0]);
		});
		
		slideTimer.start();
	}
	
	/**
	 * 슬라이드 업 애니메이션
	 */
	private void slideUpAnimation(JWindow notification, int startY) {
		Timer slideTimer = new Timer(20, null);
		final int[] currentY = {startY};
		int targetY = startY - 100; // 위로 사라짐
		
		slideTimer.addActionListener(e -> {
			currentY[0] -= 5;
			if (currentY[0] <= targetY) {
				slideTimer.stop();
				notification.dispose();
			} else {
				notification.setLocation(notification.getX(), currentY[0]);
			}
		});
		
		slideTimer.start();
	}
	
	/**
	 * 입출고 내역 페이지로 이동
	 */
	private void showInventoryLogHistoryPage() {
		if (testmain != null) {
			try {
				// MainPage의 showInventoryLogHistoryPage 메서드 호출
				testmain.getClass().getMethod("showInventoryLogHistoryPage").invoke(testmain);
				System.out.println("✅ 입출고 내역 페이지로 전환됨");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("❌ 입출고 내역 페이지 전환 실패: " + e.getMessage());
				ShowMessage.showAlert(this, "오류", "❌ 입출고 내역 페이지로 이동하는 중 오류가 발생했습니다.");
			}
		}
	}
	
	/**
	 * 음성 파일 재생
	 * @param audioFileName 재생할 음성 파일명 (예: "입고.wav", "출고.wav")
	 */
	private void playAudioFile(String audioFileName) {
		try {
			// 음성 파일 경로 설정
			String audioPath = "voice/" + audioFileName;
			URL audioUrl = getClass().getClassLoader().getResource(audioPath);
			
			if (audioUrl == null) {
				System.err.println("❌ 음성 파일을 찾을 수 없음: " + audioPath);
				return;
			}
			
			// 오디오 스트림 열기
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			
			// 음성 재생
			clip.start();
			
			System.out.println("🔊 음성 재생: " + audioFileName);
			
			// 재생 완료 후 자원 해제 (별도 스레드에서)
			new Thread(() -> {
				try {
					// 재생 완료까지 대기
					while (clip.isRunning()) {
						Thread.sleep(100);
					}
					// 자원 해제
					clip.close();
					audioInputStream.close();
				} catch (Exception e) {
					System.err.println("❌ 음성 파일 자원 해제 중 오류: " + e.getMessage());
				}
			}).start();
			
		} catch (UnsupportedAudioFileException e) {
			System.err.println("❌ 지원되지 않는 오디오 형식: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("❌ 오디오 파일 읽기 오류: " + e.getMessage());
		} catch (LineUnavailableException e) {
			System.err.println("❌ 오디오 라인 사용 불가: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("❌ 음성 재생 중 예상치 못한 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
