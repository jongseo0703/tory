package com.sinse.tory.db.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
	DBManager dbManager;
	
	// 테이블 전체보기
	public void selectAll(String table) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		con = dbManager.getConection();
		StringBuilder sql = new StringBuilder();
		sql.append("select *from "+ table); // 상품이 들어있는 테이블 전체 보기
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs= pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
	}
	// 특정 아이템 검색
		public void select(String table, String val1, String val2) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			con = dbManager.getConection();
			StringBuilder sql = new StringBuilder();
			sql.append("select *from "+table+" where "+val1+" ="+val2);
			try {
				pstmt = con.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				dbManager.release(pstmt, rs);
			}
			
		}
	
	// 아이팀 추가기능
	public void insert(String table,String colum, int n) {
		Connection con = null;
		PreparedStatement pstmt = null;
		con = dbManager.getConection();
		StringBuffer mark = new StringBuffer(); // ?들을 넣을 곳
		mark.append("?");
		for(int i=0;i<n-1;i++) { // n은 colum의 안에있는 값 갯수
			mark.append(",?");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("insert into "+table+"("+colum+") values("+mark+")");
		try {
			pstmt = con.prepareStatement(sql.toString());
			for(int i=1;i <=n; i++) {
				pstmt.setString(i, null); // ?안에 넣을 값들 
									      // null 자리에 list를 넣어 자동으로 들어가게 할 계획
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt);
		}
	}
	// 아이템 삭제기능
	public void delete(String table,String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		con = dbManager.getConection();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from "+table+"where id ="+ id);
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 특정 아이템정보 수정
	public void update(String table, String val, String reval, String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		con = dbManager.getConection();
		StringBuilder sql = new StringBuilder();
		sql.append("update "+table+" set " + val+ "="+reval +" where id =" +id);
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt);
		}
	}

}
