package com.sinse.tory.common.config;

public class Config {
	public static final String url ="jdbc:mysql://localhost:3306/tory"; 
	public static final String usr ="admin"; 
	public static final String pwd ="1234"; 
	
	// 상품 정보
	public static final String Product_table = "product"; // 상품정보가 들어있는 테이블
	public static final String Product_Pk = "product_id"; // 상품의 pk값
	public static final String Product_ID = "id"; // 상품의 고유아이디
	public static final String Product_Name = "name"; // 상품의 이름
	public static final String Product_Size = "size_id"; // 상품의 사이즈 id
	public static final String Product_Coclor = "color_id"; // 상품의 색상 id
	public static final String Product_Img = "img"; // 상품의 이미지
	public static final String Product_Price = "price"; // 상품 가격
	public static final String Product_InputDate = "inputDate"; // 상품의 입고날짜(추가할 떄 시간)
	public static final String Product_Space = "space_id"; // 상품의 창고위치 id
	public static final String Product_Brand = "brand_id"; // 상품의 브랜드 id
	public static final String Product_Detail = "dtail"; // 상품의 상세정보
	
	public static final String Product_Colum = 
			Product_ID+","+Product_Name+","+Product_Price+","+Product_Coclor+Product_Size+
			","+Product_Space+","+Product_Brand+Product_Img+","+Product_InputDate+","+Product_Detail;
	
}
