package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.sinse.tory.rightpage.datamodificationpage.DataModificationPage;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;
import com.sinse.tory.rightpage.identifier.IdentifierUpdateWithNameComboBox;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.ProductImage;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.db.repository.ProductImageDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.util.PageUtil;
import com.sinse.tory.rightpage.util.Pages;
import com.sinse.tory.rightpage.util.UpdateCount;

public class ProductShip extends Pages{
	JButton[]bt = new JButton[4];//버튼 4개 생성
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
	
	int itemId =0;
	public ProductShip(Testmain testmain, DataModificationPage dataModificationPage) {
		super(testmain);
		t_count = new JTextField();
		p_form = new JPanel();
		productDetail = new ProductDetail();
		int width = getPreferredSize().getSize().width;//현 패널의 가로넓비
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
			bt[i] = new JButton();
		}
		for(int i=0; i<la.length;i++) {
			la[i] = new JLabel();
			la[i].setFont(new Font(null, 3, 15)); // 라벨은 폰트 설정
			
		}
		
		for(int i=0; i<box.length;i++) {
			box[i] = new JComboBox();
		}
		
		//상품이미지 출력
		p_img = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				URL url = null;
				
				if(productImage !=null && productImage.getImageURL() != null) {
				url = this.getClass().getClassLoader().getResource(productImage.getImageURL()); // 상품이미지 위치
				}else {					
					url =this.getClass().getClassLoader().getResource("images/torylogo.png");//이미지가 없을 경우 
				}
				Image image = null;
				try {
					
					BufferedImage buffer = ImageIO.read(url);
					image = buffer.getScaledInstance(PageUtil.InputOutput_Width/2,260, Image.SCALE_SMOOTH);					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g.drawImage(image, 0, 0,PageUtil.InputOutput_Width/2,260, this);
			}
		};
		
		
		
		bt[0].setText("상품정보 수정"); 
		bt[1].setText("상품추가");
		bt[2].setText("출고");
		bt[3].setText("입고");
		
		la[0].setText("상위카테고리");
		la[1].setText("하위카테고리");
		la[2].setText("상품명");
		la[3].setText("수량 ");
		
		// 패널들의 크기
		location[0].setPreferredSize(new Dimension(PageUtil.InputOutput_Width,40));
		location[1].setPreferredSize(new Dimension(PageUtil.InputOutput_Width,260));
		location[2].setPreferredSize(new Dimension(PageUtil.InputOutput_Width,270));
		
		//상위 카테고리, 하위 카테고리, 상품명인  3개의 콤보박스와 라벨의 크기
		Dimension d = new Dimension(600,40);
		for(int i=0; i<box.length;i++) {
			la[i].setPreferredSize(d); //라벨의 위치
			la[i].setMaximumSize(d); //라벨의 위치
			box[i].setPreferredSize(d); //콤보박스의 위치
			box[i].setMaximumSize(d);
		}
	
		
		//수량 라벨과 텍스트박스 크기 설정
		la[3].setPreferredSize(new Dimension(800,40));
		t_count.setPreferredSize(new Dimension(800,80));
		t_count.setMaximumSize(new Dimension(800,80));
		
//		t_count.setText(Integer.toString(productDetail.getProductQuantity())); // 상품의 사이지별 수량
		
		//이미지 패널의 예비용 배경색
		p_img.setBackground(Color.lightGray);
		
		//버튼의 폰트 설정
		for(int i=0; i<bt.length;i++) {
			bt[i].setFont(new Font(null, 3, 15));
		}
		
		//버튼들의 크기 설정
		bt[0].setPreferredSize(new Dimension(150,30));//상품정보 수정
		bt[1].setPreferredSize(new Dimension(150,30));//상품 추가
		
		Dimension d2 = new Dimension(350, 40);
		bt[2].setPreferredSize(d2);//출고
		bt[2].setMaximumSize(d2);
		bt[3].setPreferredSize(d2);//입고
		bt[3].setMaximumSize(d2);
		
		//배경색 설정
		Color EE = Color.decode("#E5E5E5");
		bt[0].setBackground(EE);//상품정보 수정 버튼
		for(int i =2; i<bt.length;i++) {
			bt[i].setBackground(EE); //출고, 입고 버튼
		}
		Color EF = Color.decode("#75A5FD");
		bt[1].setBackground(EF);//상품추가 버튼
		p_form.setBackground(null);
		
		//비활성화 기능
		//상위카테고리의 값이 정해지기 전까지만 비활성화
		//값이 차례대로 입력될 수 있도록 
		IdentifierUpdateWithNameComboBox identifierUpdateWithNameComboBox = new IdentifierUpdateWithNameComboBox(box[0], box[1], box[2]);
		box[2].addItemListener((ItemEvent e)->{
			if(e.getStateChange() == ItemEvent.SELECTED && box[2].getSelectedIndex()>0) {				
				t_count.setEnabled(true);//수량을 적을 텍스트박스			 
				productDetailDAO = new ProductDetailDAO();
				itemId = identifierUpdateWithNameComboBox.getItemID();
				productDetail = productDetailDAO.selectDetailInfo(itemId);
				t_count.setText(Integer.toString(productDetailDAO.selectCurrentQuantity(itemId)));
			}

		});
		
		String result = (productDetail == null) ? "비여있음" : "안 비여있음";
		System.out.println(result);
		System.out.println(productDetail.getProductQuantity());
		
		t_count.setEnabled(false);//수량을 적을 텍스트박스			 
		
		//조립
		//location[0]의 안의 상품정보 수정 버튼과 상품추가 버튼 위치 설정
		 location[0].setLayout(new BoxLayout(location[0], BoxLayout.X_AXIS));
		 location[0].add(Box.createHorizontalGlue());//오른쪽으로 정렬
		 location[0].add(bt[0]);
		 location[0].add(Box.createRigidArea(new Dimension(10,0)));
		 location[0].add(bt[1]);
		 location[0].add(Box.createRigidArea(new Dimension(20,0)));
		 
		 //location[1]의 이미지 패널의 위치와 폼 패널의 위치 설정
		 location[1].setLayout(new GridLayout(1,2));
		 location[1].setBorder(new EmptyBorder(0,10,0,0));
		 p_form.setLayout(new BoxLayout(p_form, BoxLayout.Y_AXIS));
		 p_form.setBorder(new EmptyBorder(10,10,10,10));
		 for(int i=0;i<box.length;i++) {
			 p_form.add(la[i]);
			 p_form.add(box[i]);
			 
			 la[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
			 box[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
		 }
		 		 
		 location[1].add(p_img);
		 location[1].add(p_form);
		 
		 //location[2]의 수량라벨, 텍스트박스,입고 및 출고 버튼 위치 정의
		 location[2].setBorder(new EmptyBorder(10,10,10,10));
		 location[2].setLayout(new BoxLayout(location[2], BoxLayout.Y_AXIS));
//		 location[2].add(la[3]);
		 location[2].add(t_count);
		 location[2].add(Box.createRigidArea(new Dimension(0,30)));
		 Box btSpace = Box.createHorizontalBox();
		 btSpace.add(bt[2]);
		 btSpace.add(Box.createHorizontalStrut(10));
		 btSpace.add(bt[3]);
		 
		 location[2].add(btSpace);
		 location[2].add(Box.createRigidArea(new Dimension(0,150)));
		 
		 bt[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (identifierUpdateWithNameComboBox.getName() == null) {
					ShowMessage.showAlert(null, "상품 정보 수정", "상품을 선택 해주세요.");
					return;
				}
				
				System.out.println(productDetail);
				dataModificationPage.fillIdentifier(identifierUpdateWithNameComboBox.getSelectedTopCategoryID(),
						identifierUpdateWithNameComboBox.getSelectedSubCategoryID(),
						identifierUpdateWithNameComboBox.getItemName());
				testmain.pageMove.showPage(1,0);
			}
		});
		 // 이벤트
		 bt[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testmain.pageMove.showPage(1,0);
			}
		});
		 if(itemId >0) {			 
			 bt[2].addActionListener(e->{
				 boolean resutle = ShowMessage.showConfirm(testmain,"출고하기","출고하기겠습니까?");
				 
				 if(resutle) {
					 // 확인 눌렀을때
					 // 업데이트 처리필요
					 
				 }else {
					 // 취소를 눌렀을 때
				 }
			 });
			 bt[3].addActionListener(e->{
				 boolean resutle = ShowMessage.showConfirm(testmain,"입고하기","입고하기겠습니까?");
				 
				 if(resutle) {
					 // 확인 눌렀을때
					 
				 }else {
					 // 취소를 눌렀을 때
				 }
			 });
		 }
		 
		 
		 
		add(Box.createRigidArea(new Dimension(0,40)));
		for(int i =0;i<location.length;i++) {
			add(location[i]);
			location[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			if(i==0) add(Box.createRigidArea(new Dimension(0,30)));
			if(i==1) add(la[3]);la[3].setAlignmentX(Component.LEFT_ALIGNMENT); 
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
	   	//setPreferredSize(new Dimension(PageUtil.InputOutput_Width,PageUtil.Tory_Hieght));
	}

}
