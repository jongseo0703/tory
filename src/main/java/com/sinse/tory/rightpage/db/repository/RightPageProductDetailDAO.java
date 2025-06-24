package com.sinse.tory.rightpage.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.ProductImage;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;

public final class RightPageProductDetailDAO
{
	/**
	 * 
	 * @param subCategoryToGetID
	 * @return id, name만 가지고 있는 product
	 */
	public static List<ProductDetail> selectProductDetail(SubCategory subCategoryToGetID)
	{
		DBManager dbManager = DBManager.getInstance();
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<ProductDetail> list = new ArrayList<>(); //Product 타입의 객체를 담은 list 생성
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		
		sql.append("select 												");
		sql.append("p.product_id, p.product_name, pd.product_size_name 	");
		//from 절(조인)
		sql.append("from Product p, Location l, Brand b, SubCategory s, productdetail pd 	");
		sql.append("where s.sub_category_id = b.sub_category_id and 						");
		sql.append("b.brand_id = l.brand_id and 											");
		sql.append("l.location_id = p.location_id and 										");
		sql.append("s.sub_category_id = ? and 												");
		sql.append("pd.product_id = p.product_id;											");
		try
		{
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//첫번째 바인딩 변수 = select 메서드의 매개변수인 product_id(즉, 사용자가 선택한 상자)
			pstmt.setInt(1, subCategoryToGetID.getSubCategoryId());
			rs = pstmt.executeQuery();
			
			// rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while (rs.next())
			{
				ProductDetail productDetail = new ProductDetail();
				Product product = new Product();
				
				product.setProductId(rs.getInt("product_id"));
				product.setProductName(rs.getString("product_name"));
				
				productDetail.setProduct(product);
				productDetail.setProductSizeName(rs.getString("product_size_name"));
				
				list.add(productDetail);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
}
