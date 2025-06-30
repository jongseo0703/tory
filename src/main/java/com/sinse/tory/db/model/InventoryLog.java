package com.sinse.tory.db.model;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryLog {
	/**
	 * 상품 수량 변경 유형을 나타내는 열거형.
	 * IN - 입고 (수량 증가)
	 * OUT - 출고 (수량 감소)
	 */
	public enum ChangeType {
		IN, OUT
	}
	//Id
	//Column(name = "inventory_log_id") 실제 디비 컬럼명
	private int inventoryLogId;
	
	//Column(name = "change_type") 실제 디비 컬럼명
	//changeType를 enum 타입(열거형)으로 정의해서 오타방지함.
	private ChangeType changeType;
	
	//Column(name = "quantity") 실제 디비 컬럼명
	private int quantity;
	
	//Column(name = "changedAt") 실제 디비 컬럼명
	private LocalDateTime changedAt;
	
	//Store 모델은 ProductDetail 모델의 자식 엔터티
	private ProductDetail productDetail;
	
	//디폴트 생성자
	public InventoryLog() {};
	
	//한 줄로 객체를 초기화 가능하게하는 생성자
	public InventoryLog(int inventoryLogId, ChangeType changeType, int quantity, LocalDateTime changedAt, ProductDetail productDetail) {
		this.inventoryLogId = inventoryLogId;
		this.changeType = changeType;
		this.quantity = quantity;
		this.changedAt = changedAt;
		this.productDetail = productDetail;
	}

	//접근제한자가 private 인 inventoryLogId를 반환해주기 위한 getter 메서드
	public int getInventoryLogId() {
		return inventoryLogId;
	}

	//접근제한자가 private 인 inventoryLogId를 바꿔주기 위한 setter 메서드
	public void setInventoryLogId(int inventoryLogId) {
		this.inventoryLogId = inventoryLogId;
	}

	//접근제한자가 private 인 changeType을 반환해주기 위한 getter 메서드
	public ChangeType getChangeType() {
		return changeType;
	}

	//접근제한자가 private 인 changeType을 바꿔주기 위한 setter 메서드
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	//접근제한자가 private 인 quantity 를 반환해주기 위한 getter 메서드
	public int getQuantity() {
		return quantity;
	}

	//접근제한자가 private 인 quantity 를 바꿔주기 위한 setter 메서드
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	//접근제한자가 private 인 changedAt을 반환해주기 위한 getter 메서드
	public LocalDateTime getChangedAt() {
		return changedAt;
	}

	//접근제한자가 private 인 changedAt을 바꿔주기 위한 setter 메서드
	public void setChangedAt(LocalDateTime changedAt) {
		this.changedAt = changedAt;
	}

	//접근제한자가 private 인 productDetail을 반환해주기 위한 getter 메서드
	public ProductDetail getProductDetail() {
		return productDetail;
	}

	//접근제한자가 private 인 productDetail을 바꿔주기 위한 setter 메서드
	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}
	
	//디버깅용 toString() 메서드 오버라이딩
	@Override
	public String toString() {
		return "InventoryLog{id=" + inventoryLogId +
				", changeType='" + changeType + "'" +
				", quantity='" + quantity + "'" +
				", date='" + changedAt + "'" +
				", productDetail=" + productDetail.getProductSizeName() +
				", product=" + productDetail.getProduct().getProductName() + "}";
	}
	
}
