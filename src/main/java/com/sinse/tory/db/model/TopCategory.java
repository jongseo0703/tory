package com.sinse.tory.db.model;

//상의, 하의, 악세서리, 모자, 아우터 등을 구별하기 위한 최상위 모델(TopCategory 엔터티)
//Entity
public class TopCategory {
	//Id
	//Column(name = "topcategory_id") 실제 디비 컬럼명
	private int topCategoryId; //PK
	
	//Column(name = "topcategory_name") 실제 디비 컬럼명
	private String topCategoryName;
	
	//디폴트 생성자
	public TopCategory() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public TopCategory(int topCategoryId, String topCategoryName) {
		this.topCategoryId = topCategoryId;
		this.topCategoryName = topCategoryName;
	}
	
	//접근제한자가 private 인 topCategoryId를 반환해주기 위한 getter 메서드
	public int getTopCategoryId() {
		return topCategoryId;
	}
	
	//접근제한자가 private 인 topCategoryId를 바꿔주기 위한 setter 메서드
	public void setTopCategoryId(int topCategoryId) {
		this.topCategoryId = topCategoryId;
	}
	
	//접근제한자가 private 인 topCategoryName을 반환해주기 위한 getter 메서드
	public String getTopCategoryName() {
		return topCategoryName;
	}
	
	//접근제한자가 private 인 topCategoryName을 바꿔주기 위한 setter 메서드
	public void setTopCategoryName(String topCategoryName) {
		this.topCategoryName = topCategoryName;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return getTopCategoryName();
	}
	
}
