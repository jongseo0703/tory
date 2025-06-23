package com.sinse.tory.db.repository;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//DB관련 패키지 임포트
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.common.exception.UpdatedQuantityException;
import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.InventoryLog;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.ProductImage;

//ProductDetail 모델을 처리하기 위한 DAO
public class ProductDetailDAO {
	//싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
	DBManager dbManager = DBManager.getInstance();
	
	public ProductDetail selectDetailInfo(int productDetailId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductDetail detail = null;

		con = dbManager.getConnection();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pd.product_size_name, pd.product_quantity, ");
		sql.append("       p.product_description, pi.image_url ");
		sql.append("FROM ProductDetail pd ");
		sql.append("JOIN Product p ON pd.product_id = p.product_id ");
		sql.append("LEFT JOIN ProductImage pi ON p.product_id = pi.product_id ");
		sql.append("WHERE pd.product_detail_id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, productDetailId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// ProductDetail 객체 생성
				detail = new ProductDetail();
				detail.setProductSizeName(rs.getString("product_size_name"));
				detail.setProductQuantity(rs.getInt("product_quantity"));

				// Product 객체 생성 및 정보 설정
				Product product = new Product();
				product.setDescription(rs.getString("product_description"));

				// ProductImage 객체 생성 및 URL 설정
				ProductImage productImage = new ProductImage();
				productImage.setImageURL(rs.getString("image_url"));
				List<ProductImage> imageList = new ArrayList<>();
				imageList.add(productImage);

				// Product에 이미지 리스트 설정
				product.setProductImages(imageList);

				// ProductDetail에 Product 설정
				detail.setProduct(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return detail;
	}


	
	//현재 수량을 조회하는 메서드
	public int selectCurrentQuantity(int productDetailId) {
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//select 절
		sql.append("select product_size_name, product_quantity");
		//from 절
		sql.append(" from ProductDetail");
		//where 절
		sql.append(" where product_detail_id = ?");
		
		//현재 수량 초기화
		int currentQuantity = 0;
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, productDetailId); //첫번째 바인딩 변수
			rs = pstmt.executeQuery();
			
			//조회할 데이터가 있으면
			if(rs.next()) {
				//현재 수량 저장
				currentQuantity = rs.getInt("product_quantity");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt, rs);
		}
		//현재 수량 반환
		return currentQuantity;
	}
	
	//입고와 출고에 따라 현재 수량을 업데이트 해주는 메서드
	public void updateQuantity(int productDetailId, InventoryLog inventoryLog) throws UpdatedQuantityException {
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//product_detail_id로 상품의 수량 업데이트 쿼리
		//update 절
		sql.append("update ProductDetail set product_quantity = ?");
		//where 절
		sql.append(" where product_detail_id = ?");
		
		//현재 수량은 위에 선언한 selectCurrentQuantity() 메서드로 조회해서 저장
		int currentQuantity = selectCurrentQuantity(productDetailId);
		
		//현재 수량에서 입출고에 따라 업데이트되는 updatedQuantity 선언
		//InventoryLog 모델의 changeType 컬럼의 값을 얻어와서 IN이면 입고이기 때문에 +, OUT이면 출고이기 때문에 -
		int updatedQuantity = inventoryLog.getChangeType() == InventoryLog.ChangeType.IN 
				? currentQuantity + inventoryLog.getQuantity() : currentQuantity - inventoryLog.getQuantity();
		
		//업데이트된 수량이 0보다 작으면 현재 수량보다 출고 수량이 많다는 의미이므로
		if(updatedQuantity < 0) {
			//직접 만든 예외인 UpdatedQuantityException 던지기
			throw new UpdatedQuantityException("출고 수량이 현재 재고보다 많습니다.");
		}
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, updatedQuantity); //첫번째 바인딩 변수
			pstmt.setInt(2, productDetailId); //두번째 바인딩 변수
			pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt);
		}
	}
}
