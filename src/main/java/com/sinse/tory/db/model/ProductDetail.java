package com.sinse.tory.db.model;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.List;

//상품에 해당되는 사이즈 모델(ProductSize 엔터티)
public class ProductDetail {
	//Id
	//Column(name = "product_detail_id") 실제 디비 컬럼명
	private int productDetailId;
	
	//Column(name = "product_size_name") 실제 디비 컬럼명
	private String productSizeName;
	
	//Column(name = "product_quantity") 실제 디비 컬럼명
	private int productQuantity;
	
	//ProductSize 모델은 Product 모델의 자식 엔터티
	private Product product;
	
	//연관된 상품의 입출고리스트
	private List<InventoryLog> inventoryLogs = new ArrayList<>();

	//디폴트 생성자
	public ProductDetail() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public ProductDetail(int productDetailId, String productSizeName, int productQuantity, Product product) {
		this.productDetailId = productDetailId;
		this.productSizeName = productSizeName;
		this.productQuantity = productQuantity;
		this.product = product;
	}

	//접근제한자가 private 인 productDetailId를 반환해주기 위한 getter 메서드
	public int getProductDetailId() {
		return productDetailId;
	}

	//접근제한자가 private 인 productDetailId를 바꿔주기 위한 setter 메서드
	public void setProductDetailId(int productDetailId) {
		this.productDetailId = productDetailId;
	}

	//접근제한자가 private 인 productSizeName을 반환해주기 위한 getter 메서드
	public String getProductSizeName() {
		return productSizeName;
	}

	//접근제한자가 private 인 productSizeName을 바꿔주기 위한 setter 메서드
	public void setProductSizeName(String productSizeName) {
		this.productSizeName = productSizeName;
	}
	
	//접근제한자가 private 인 productQuantity 를 반환해주기 위한 getter 메서드
	public int getProductQuantity() {
		return productQuantity;
	}

	//접근제한자가 private 인 productQuantity 를 바꿔주기 위한 setter 메서드
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	//접근제한자가 private 인 product 를 반환해주기 위한 getter 메서드
	public Product getProduct() {
		return product;
	}

	//접근제한자가 private 인 product 를 바꿔주기 위한 setter 메서드
	public void setProduct(Product product) {
		this.product = product;
	}
	
	//접근제한자가 private 인 stores 를 반환해주기 위한 getter 메서드
	public List<InventoryLog> getInventoryLogs() {
		return inventoryLogs;
	}

	//접근제한자가 private 인 stores 를 바꿔주기 위한 setter 메서드
	public void setInventoryLogs(List<InventoryLog> inventoryLogs) {
		this.inventoryLogs = inventoryLogs;
	}

	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "ProductDetail{id=" + productDetailId +
				", sizeName='" + productSizeName + "'" +
				", productQuantity='" + productQuantity + "'" +
				", product=" + product.getProductName() + "}";
	}
	
}
