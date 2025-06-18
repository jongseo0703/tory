package com.sinse.tory.db.model;

//상의 중에서도 티셔츠, 셔츠, 니트 등을 구별하기 위한 서브 모델(SubCategory 엔터티)
public class SubCategory {
	//Id
	//Column(name = "subcategory_id") 실제 디비 컬럼명
	private int subCategoryId; //PK
	
	//Column(name = "subcategory_name") 실제 디비 컬럼명
	private String subCategoryName;
	
	//SubCategory 모델은 TopCategory 모델의 자식 엔터티
	private TopCategory topCategory;
	
	//디폴트 생성자
	public SubCategory() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public SubCategory(int subCategoryId, String subCategoryName, TopCategory topCategory) {
		this.subCategoryId = subCategoryId;
		this.subCategoryName = subCategoryName;
		this.topCategory = topCategory;
	}
	
	//접근제한자가 private 인 subCategoryId를 반환해주기 위한 getter 메서드
	public int getSubCategoryId() {
		return subCategoryId;
	}
	
	//접근제한자가 private 인 subCategoryId를 바꿔주기 위한 setter 메서드
	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	
	//접근제한자가 private 인 subCategoryName을 반환해주기 위한 getter 메서드
	public String getSubCategoryName() {
		return subCategoryName;
	}
	
	//접근제한자가 private 인 subCategoryName을 바꿔주기 위한 setter 메서드
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	
	//접근제한자가 private 인 TopCategory를 반환해주기 위한 getter 메서드
	public TopCategory getTopCategory() {
		return topCategory;
	}

	//접근제한자가 private 인 TopCategory를 바꿔주기 위한 setter 메서드
	public void setTopCategory(TopCategory topCategory) {
		this.topCategory = topCategory;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "SubCategory{id=" + subCategoryId + 
				", name='" + subCategoryName + "'" +
				", topCategory=" + topCategory.getTopCategoryName() + "}";
	}
	
}
