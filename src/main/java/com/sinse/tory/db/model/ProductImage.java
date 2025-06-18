package com.sinse.tory.db.model;

//상품에 해당되는 이미지 모델(ProductImage 엔터티)
public class ProductImage {
	//Id
	//Column(name = "productimage_id") 실제 디비 컬럼명
	private int productImageId;
	
	//Column(name = "image_url") 실제 디비 컬럼명
	private String imageURL;
	
	//ProductImage 모델은 Product 모델의 자식 엔터티
	private Product product;
	
	//디폴트 생성자
	public ProductImage() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public ProductImage(int productImageId, String imageURL, Product product) {
		this.productImageId = productImageId;
		this.imageURL = imageURL;
		this.product = product;
	}

	//접근제한자가 private 인 productImageId를 반환해주기 위한 getter 메서드
	public int getProductImageId() {
		return productImageId;
	}

	//접근제한자가 private 인 productImageId를 바꿔주기 위한 setter 메서드
	public void setProductImageId(int productImageId) {
		this.productImageId = productImageId;
	}

	//접근제한자가 private 인 imageURL을 반환해주기 위한 getter 메서드
	public String getImageURL() {
		return imageURL;
	}

	//접근제한자가 private 인 imageURL을 바꿔주기 위한 setter 메서드
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	//접근제한자가 private 인 product 를 반환해주기 위한 getter 메서드
	public Product getProduct() {
		return product;
	}

	//접근제한자가 private 인 product 를 바꿔주기 위한 setter 메서드
	public void setProduct(Product product) {
		this.product = product;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "ProductImage{id=" + productImageId +
				", url='" + imageURL + "'" +
				", product=" + product.getProductName() + "}";
	}

}
