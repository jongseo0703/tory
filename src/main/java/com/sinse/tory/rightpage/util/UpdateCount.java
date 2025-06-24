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
	
	public int selectCout(int id) {
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from productdetail where product_id =?");
		int itemcount =0;
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, Integer.toString(id));
			rs= pstmt.executeQuery();
			while(rs.next()) {
			itemcount = rs.getInt("product_quantity");
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}
		
		return itemcount;
	}

}
