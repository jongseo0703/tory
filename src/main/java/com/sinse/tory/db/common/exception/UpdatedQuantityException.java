package com.sinse.tory.db.common.exception;

//수량 업데이트 예외 클래스
public class UpdatedQuantityException extends RuntimeException {
	public UpdatedQuantityException(String msg) {
		super(msg);
	}
	
	public UpdatedQuantityException(Throwable e) {
		super(e);
	}
	
	public UpdatedQuantityException(String msg, Throwable e) {
		super(msg, e);
	}
}
