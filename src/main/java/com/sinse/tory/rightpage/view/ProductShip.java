package com.sinse.tory.rightpage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.common.config.Config;

public class ProductShip extends JPanel{
	JButton[]bt = new JButton[4];//버튼 4개 생성
	JComboBox[] box = new JComboBox[3];//콤보박스 3개
	JLabel[]la = new JLabel[4];//라벨 4개
	JTextField t_count;
	JPanel p_img;
	JPanel p_center;
	public ProductShip() {
		t_count = new JTextField();
		p_img = new JPanel();
		p_center = new JPanel(); //라벨, 버튼, 텍스트박스, 콤보박스가 들어있는 패널
		
		// 각각 하나씩 자동 생성
		for(int i=0; i<bt.length;i++) {
			bt[i] = new JButton();
		}
		for(int i=0; i<la.length;i++) {
			la[i] = new JLabel();
		}
		for(int i=0; i<box.length;i++) {
			box[i] = new JComboBox();
		}
		
		
		bt[0].setText("상품정보 수정"); 
		bt[1].setText("상품추가");
		bt[2].setText("출고");
		bt[3].setText("입고");
		
		la[0].setText("상위카테고리");
		la[1].setText("하위카테고리");
		la[2].setText("상품명");
		la[3].setText("수량");
		
		setLayout(null);
		
		p_center.setLayout(null);
		p_center.setBounds(10,80,680,510); //위치조정
		
		//상위 카테고리, 하위 카테고리, 상품명인  3개의 콤보박스와 라벨의 위치 설정
		for(int i=0; i<box.length;i++) {
			la[i].setFont(new Font(null, 3, 15)); // 라벨은 폰트 설정
			la[i].setBounds(p_center.getPreferredSize().getSize().width-330,0+85*i,320,40); //라벨의 위치
			box[i].setBounds(p_center.getPreferredSize().getSize().width-330,35+i*85,320,40); //콤보박스의 위치
		}
		la[3].setFont(new Font(null, 3, 15));
		
		//수량 라벨과 텍스트박스 위치 설정
		la[3].setBounds(10, 260, 320, 40);
		t_count.setBounds(10, 290, 660, 40);
		
		//이미지 패널의 위치와 크기 설정
		p_img.setBounds(0,0,317,257);
		p_img.setBackground(Color.lightGray);
		
		//버튼의 폰트 설정
		for(int i=0; i<bt.length;i++) {
			bt[i].setFont(new Font(null, 3, 15));
		}
		
		//버튼들의 위치와 크기 설정
		bt[0].setBounds(380, 40, 150, 30); //상품정보 수정
		bt[1].setBounds(380+160, 40, 116, 30); //상품 추가
		bt[2].setBounds(10, 450, 322, 40); //출고
		bt[3].setBounds(10+330, 450, 322, 40);//입고
		
		//배경색 설정
		Color EE = Color.decode("#E5E5E5");
		bt[0].setBackground(EE);//상품정보 수정 버튼
		for(int i =2; i<bt.length;i++) {
			bt[i].setBackground(EE); //출고, 입고 버튼
		}
		Color EF = Color.decode("#75A5FD");
		bt[1].setBackground(EF);//상품추가 버튼
		p_center.setBackground(null);//색경색 제거
		
		//비활성화 기능
		//상위카테고리의 값이 정해지기 전까지만 비활성화
		//값이 차례대로 입력될 수 있도록 
		 box[1].setEnabled(false);//하위카테고리
		 box[2].setEnabled(false);//상품명
		 t_count.setEnabled(false);//수량을 적을 텍스트박스
		
		
		//조립
		add(bt[0]);//상품정보 수정 버튼
		add(bt[1]);//상품 추가 버튼
		p_center.add(p_img);//이미지 패널
		for(int i=0; i<box.length;i++) {
			p_center.add(la[i]);// 상위카테고리, 하위카테고리, 상품명 라벨
			p_center.add(box[i]);// 상위카테고리, 하위카테고리, 상품명 콤보박스
		}
		p_center.add(la[3]);//수량 라벨
		p_center.add(t_count);//수량 적을 텍스트박스
		p_center.add(bt[2]);//출고 버튼
		p_center.add(bt[3]);//입고 버튼
		add(p_center);
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(Config.Ship_Main_Width,Config.Ship_Main_Height));//config에 들어있는 사이즈
	}

}
