package com.sinse.tory.rightpage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sinse.tory.db.common.util.DBManager;

public class UpdateCount {
	DBManager dbManager = DBManager.getInstance();
	
	public void update(int count ,int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("update ProductDetail set product_quantity = ? where product_detail_id = ?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, count);
			pstmt.setInt(2, id);
			
			con.setAutoCommit(false); //AutoCommit를 꺼두어야 자동으로 저장됨 
			int retult = pstmt.executeUpdate();
			if(retult>0) {
				con.commit(); //DB에 반영하기 위해서는 필요
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt);
		}
		
	}
	
	public void dateInsert(String inOut, int count, int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into InventoryLog(change_type,quantity,product_detail_id) values(?,?,?)");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, inOut);
			pstmt.setInt(2, count);
			pstmt.setInt(3, id);
			con.setAutoCommit(false); //AutoCommit를 꺼두어야 자동으로 저장됨 
			int result = pstmt.executeUpdate();
			if(result>0) {
				System.out.println("insert 성공");
				con.commit(); //DB에 반영하기 위해서는 필요
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt);
		}
		
	}

}
