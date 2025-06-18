package com.sinse.tory.db.model;

//각 카테고리별 브랜드가 창고의 어디 위치에 있는지를 위한 위치 모델(Location 엔터티)
public class Location {
	//Id
	//Column(name = "location_id") 실제 디비 컬럼명
	private int locationId; //PK
	
	//Column(name = "location_name") 실제 디비 컬럼명
	private String locationName;
	
	//Location 모델은 Brand 모델의 자식 엔터티
	private Brand brand;
	
	//디폴트 생성자
	public Location() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public Location(int locationId, String locationName, Brand brand) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.brand = brand;
	}

	//접근제한자가 private 인 locationId를 반환해주기 위한 getter 메서드
	public int getLocationId() {
		return locationId;
	}

	//접근제한자가 private 인 locationId를 바꿔주기 위한 setter 메서드
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	//접근제한자가 private 인 locationName을 반환해주기 위한 getter 메서드
	public String getLocationName() {
		return locationName;
	}

	//접근제한자가 private 인 locationName을 바꿔주기 위한 setter 메서드
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	//접근제한자가 private 인 brand 를 반환해주기 위한 getter 메서드
	public Brand getBrand() {
		return brand;
	}

	//접근제한자가 private 인 brand 를 바꿔주기 위한 setter 메서드
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "Location{id=" + locationId +
				", name='" + locationName + "'" +
				", brand=" + brand.getBrandName() +
				", subCategory=" + brand.getSubCategory().getSubCategoryName() +
				", topCategory=" + brand.getSubCategory().getTopCategory().getTopCategoryName() + "}";
	}
	
}
