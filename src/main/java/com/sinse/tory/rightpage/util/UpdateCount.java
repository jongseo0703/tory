package com.sinse.tory.rightpage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.db.repository.ProductImageDAO;

public class UpdateCount {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DBManager dbManager;
	ProductDetail productDetail;
	
	public void updateCount(String number, String id, ProductDetail productDetail) {
		this.productDetail = productDetail;
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("update productdetail"
				+ "set product_quantity = ?"
				+ "where product_id = ?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, number);
			pstmt.setString(2,id);
			int update = pstmt.executeUpdate();
			if(update>0) {
				sql.delete(0, sql.length());
				sql.append("select product_quantity fron productdetail where product_id =?"); //select문
				pstmt =con.prepareStatement(sql.toString());
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				productDetail.setProductQuantity(rs.getInt("product_quantity"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.release(pstmt, rs);
		}
		
	}

}
