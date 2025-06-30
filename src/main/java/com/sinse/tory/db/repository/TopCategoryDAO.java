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
import com.sinse.tory.db.model.TopCategory;

public class TopCategoryDAO {
	//싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
	DBManager dbManager = DBManager.getInstance();
	
	public List<TopCategory> selectAll() {
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<TopCategory> list = new ArrayList<>(); //TopCategory 타입의 객체를 담은 list 생성
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		sql.append("select top_category_id, top_category_name from TopCategory"); //TopCategory 테이블의 전체 데이터 조회
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 topCategory 인스턴스에 넣어서 list 에 저장
			while(rs.next()) {
				TopCategory topCategory = new TopCategory(); //레코드 한 건을 담기위한 객체
				//topcategory_id컬럼의 데이터를 가져와서 저장
				topCategory.setTopCategoryId(rs.getInt("top_category_id"));
				//topcategory_name컬럼의 데이터를 가져와서 저장
				topCategory.setTopCategoryName(rs.getString("top_category_name"));
				//레코드 한 건 list 에 저장
				list.add(topCategory);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt, rs);
		}
		//전체 레코드가 담긴 list 반환
		return list;
	}
}
