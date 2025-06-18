package com.sinse.tory.db.model;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.List;

//각 위치별 어떤 상품이 있는지를 위한 상품 모델(Product 엔터티)
public class Product {
	//Id
	//Column(name = "product_id") 실제 디비 컬럼명
	private int productId; //PK
	
	//Column(name = "product_name") 실제 디비 컬럼명
	private String productName;
	
	//Column(name = "product_price") 실제 디비 컬럼명
	private int productPrice;
	
	//Column(name = "description") 실제 디비 컬럼명
	private String description;
	
	//Product 모델은 Location 모델의 자식 엔터티
	private Location location;
	
	//연관된 사이즈의 리스트
	private List<ProductDetail> productDetails = new ArrayList<>();
	
	//연관된 이미지의 리스트
	private List<ProductImage> productImages = new ArrayList<>();
	
	//디폴트 생성자
	public Product() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public Product(int productId, String productName, Location location) {
		this.productId = productId;
		this.productName = productName;
		this.location = location;
	}

	//접근제한자가 private 인 productId를 반환해주기 위한 getter 메서드
	public int getProductId() {
		return productId;
	}

	//접근제한자가 private 인 productId를 바꿔주기 위한 setter 메서드
	public void setProductId(int productId) {
		this.productId = productId;
	}

	//접근제한자가 private 인 productName을 반환해주기 위한 getter 메서드
	public String getProductName() {
		return productName;
	}

	//접근제한자가 private 인 productName을 바꿔주기 위한 setter 메서드
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	//접근제한자가 private 인 productPrice를 반환해주기 위한 getter 메서드
	public int getProductPrice() {
		return productPrice;
	}

	//접근제한자가 private 인 productPrice를 바꿔주기 위한 setter 메서드
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	//접근제한자가 private 인 description 을 반환해주기 위한 getter 메서드
	public String getDescription() {
		return description;
	}

	//접근제한자가 private 인 description 을 바꿔주기 위한 setter 메서드
	public void setDescription(String description) {
		this.description = description;
	}

	//접근제한자가 private 인 location 을 반환해주기 위한 getter 메서드
	public Location getLocation() {
		return location;
	}

	//접근제한자가 private 인 location 을 바꿔주기 위한 setter 메서드
	public void setLocation(Location location) {
		this.location = location;
	}
	
	//접근제한자가 private 인 productDetail을 반환해주기 위한 getter 메서드
	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	//접근제한자가 private 인 productDetail을 바꿔주기 위한 setter 메서드
	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

	//접근제한자가 private 인 productImages를 반환해주기 위한 getter 메서드
	public List<ProductImage> getProductImages() {
		return productImages;
	}

	//접근제한자가 private 인 productImages를 바꿔주기 위한 setter 메서드
	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "Product{id=" + productId +
				", name='" + productName + "'" +
				", location=" + location.getLocationName() +
				", brand=" + location.getBrand().getBrandName() +
				", subCategory=" + location.getBrand().getSubCategory().getSubCategoryName() +
				", topCategory=" + location.getBrand().getSubCategory().getTopCategory().getTopCategoryName() + "}";
	}
	
}
