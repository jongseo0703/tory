package com.sinse.tory.db.repository;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//디비 관련 패키지 임포트
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.List;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.Brand;

public class BrandDAO {
	// 싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 하위 카테고리에 해당되는 브랜드 목록을 조회
	 */
	public List<Brand> selectBySub(SubCategory subCategory) {
		Connection con = null; // 커넥션 객체 초기화
		PreparedStatement pstmt = null; // sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; // select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Brand> list = new ArrayList<>(); // Brand 타입의 객체를 담은 list 생성

		// db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();

		// String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		// 선택된 SubCategory 에 매칭되는 정보만을 가져오기 위한 쿼리문
		sql.append("select brand_id, brand_name, sub_category_id from Brand where sub_category_id = ?");

		try {
			// 만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			// 바인딩 변수인 ? = subCategory에 매칭되는 subcategory_id 즉, 하위 카테고리 선택
			pstmt.setInt(1, subCategory.getSubCategoryId());
			rs = pstmt.executeQuery();

			// rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 brand 인스턴스에 넣어서 list 에저장
			while (rs.next()) {
				Brand brand = new Brand(); // 레코드 한 건을 담기위한 객체
				// brand_id컬럼의 데이터를 가져와서 저장
				brand.setBrandId(rs.getInt("brand_id"));
				// brand_name컬럼의 데이터를 가져와서 저장
				brand.setBrandName(rs.getString("brand_name"));
				// brand 와 subCategory 연결
				brand.setSubCategory(subCategory);
				// 레코드 한 건 list 에저장
				list.add(brand);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db 자원 반납
			dbManager.release(pstmt, rs);
		}
		// 전체 레코드가 담긴 list 반환
		return list;
	}

	/**
	 * 전체 브랜드 목록 조회 (하위 카테고리 정보 포함)
	 * 
	 * - Brand와 SubCategory를 내부적으로 JOIN하여
	 * 각 브랜드에 해당하는 하위 카테고리 정보를 함께 매핑한다.
	 * - 예: '스투시' 브랜드가 '니트' 하위카테고리에 속해 있는 정보까지 포함됨
	 * 
	 * @return 하위 카테고리 정보까지 포함된 전체 브랜드 리스트
	 */
	public List<Brand> selectAllWithSubCategory() {
		Connection con = null; // 커넥션 객체 초기화
		PreparedStatement pstmt = null; // sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; // select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Brand> list = new ArrayList<>(); // Brand 타입의 객체를 담은 list 생성

		// db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();

		// Brand와 SubCategory를 조인하여 모든 브랜드와 그에 속한 하위카테고리를 가져오는 쿼리
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT b.brand_id, b.brand_name, b.sub_category_id, sc.sub_category_name ");
		sql.append("FROM Brand b ");
		sql.append("JOIN SubCategory sc ON b.sub_category_id = sc.sub_category_id");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// Brand 객체 생성 및 정보 설정
				Brand brand = new Brand();
				brand.setBrandId(rs.getInt("brand_id"));
				brand.setBrandName(rs.getString("brand_name"));

				// SubCategory 객체 생성 및 정보 설정
				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(rs.getInt("sub_category_id"));
				subCategory.setSubCategoryName(rs.getString("sub_category_name"));

				// 브랜드에 하위카테고리 연결
				brand.setSubCategory(subCategory);

				// 리스트에 추가
				list.add(brand);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs); // 자원 반납
		}
		return list;
	}
}
