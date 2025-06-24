package com.sinse.tory.db.model;

//티셔츠 중에서도 어느 브랜드꺼인지 구별하기 위한 브랜드 모델(Brand 엔터티)
public class Brand {
	//Id
	//Column(name = "brand_id") 실제 디비 컬럼명
	private int brandId; //PK
	
	//Column(name = "brand_name") 실제 디비 컬럼명
	private String brandName;
	
	//Brand 모델은 SubCategory 모델의 자식 엔터티
	private SubCategory subCategory;
	
	//디폴트 생성자
	public Brand() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public Brand(int brandId, String brandName, SubCategory subCategory) {
		this.brandId = brandId;
		this.brandName = brandName;
		this.subCategory = subCategory;
	}

	//접근제한자가 private 인 brandId를 반환해주기 위한 getter 메서드
	public int getBrandId() {
		return brandId;
	}

	//접근제한자가 private 인 brandId를 바꿔주기 위한 setter 메서드
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	//접근제한자가 private 인 brandName을 반환해주기 위한 getter 메서드
	public String getBrandName() {
		return brandName;
	}

	//접근제한자가 private 인 brandName을 바꿔주기 위한 setter 메서드
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	//접근제한자가 private 인 subCategory를 반환해주기 위한 getter 메서드
	public SubCategory getSubCategory() {
		return subCategory;
	}

	//접근제한자가 private 인 subCategory를 바꿔주기 위한 setter 메서드
	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return getBrandName();
	}
	
}
