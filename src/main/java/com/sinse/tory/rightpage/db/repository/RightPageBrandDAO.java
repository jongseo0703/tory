package com.sinse.tory.rightpage.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.SubCategory;

public final class RightPageBrandDAO {
	public static List<Brand> selectAllName() {
		DBManager dBManager = DBManager.getInstance();
		Connection con = dBManager.getConnection();
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Brand> list = new ArrayList<>(); //Brand 타입의 객체를 담은 list 생성
				
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//선택된 SubCategory 에 매칭되는 정보만을 가져오기 위한 쿼리문
		sql.append("select * from Brand;");
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//바인딩 변수인 ? = subCategory에 매칭되는 subcategory_id 즉, 하위 카테고리 선택
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 brand 인스턴스에 넣어서 list 에저장
			while(rs.next()) {
				Brand brand = new Brand(); //레코드 한 건을 담기위한 객체
				//brand_id컬럼의 데이터를 가져와서 저장
				brand.setBrandId(rs.getInt("brand_id"));
				//brand_name컬럼의 데이터를 가져와서 저장
				brand.setBrandName(rs.getString("brand_name"));
				//레코드 한 건 list 에저장
				list.add(brand);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			//db 자원 반납
			dBManager.release(pstmt, rs);
		}
		
		//전체 레코드가 담긴 list 반환
		return list;
	}
	
	// 하위 카테고리에 해당되는 브랜드 목록을 조회하기 위한 메서드
	public static List<Brand> selectBySubCategory(SubCategory subCategory) {
		DBManager dBManager = DBManager.getInstance();
		Connection con = dBManager.getConnection();
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Brand> list = new ArrayList<>(); //Brand 타입의 객체를 담은 list 생성
				
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//선택된 SubCategory 에 매칭되는 정보만을 가져오기 위한 쿼리문
		sql.append("select brand_id, brand_name, sub_category_id from Brand where sub_category_id = ?");
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//바인딩 변수인 ? = subCategory에 매칭되는 subcategory_id 즉, 하위 카테고리 선택
			pstmt.setInt(1, subCategory.getSubCategoryId());
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 brand 인스턴스에 넣어서 list 에저장
			while(rs.next()) {
				Brand brand = new Brand(); //레코드 한 건을 담기위한 객체
				//brand_id컬럼의 데이터를 가져와서 저장
				brand.setBrandId(rs.getInt("brand_id"));
				//brand_name컬럼의 데이터를 가져와서 저장
				brand.setBrandName(rs.getString("brand_name"));
				//brand 와 subCategory 연결
				brand.setSubCategory(subCategory);
				//레코드 한 건 list 에저장
				list.add(brand);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			//db 자원 반납
			dBManager.release(pstmt, rs);
		}
		
		//전체 레코드가 담긴 list 반환
		return list;
	}
}