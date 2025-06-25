package com.sinse.tory.rightpage.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sinse.tory.db.common.util.DBManager;

public final class RightPageProductDAO {
	public static boolean exist(String productName) {
		DBManager dbManager = DBManager.getInstance();
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    boolean exists = false;

	    String sql = "SELECT 1 FROM product WHERE product_name = ? LIMIT 1";

	    try {
	        con = dbManager.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, productName);
	        rs = pstmt.executeQuery();

	        // 결과가 있으면 true
	        exists = rs.next();
	    }
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    finally {
	        dbManager.release(pstmt, rs);
	    }

	    return exists;
	}
}
