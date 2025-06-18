package com.sinse.tory.db.common.util;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//디비 관련 파일 임포트
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//선언한 라이브러리 패키지 임포트
//공통 관련 파일 임포트
import com.sinse.tory.db.common.config.Config;

//디자인패턴 중 싱글톤패턴을 사용한(하나의 인스턴스만 보장) DBManager 클래스
public class DBManager {
	//객체 생성해서 접근하지 못하게 함.
	private static DBManager instance;
	
	//객체 생성해서 접근하지 못하게 함.
	private Connection con;
	
	//직접 인스턴스를 생성하지 못하게 생성자에 접근제한을 줌.
	private DBManager() {
		try {
			//pom.xml파일에서 의존성 주입한 jdbc 위치
			Class.forName("com.mysql.jdbc.Driver");
			
			//디비 접속
			con = DriverManager.getConnection(Config.url, Config.user, Config.pass);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DBManager getInstance() {
		//싱글톤 패턴의 핵심 만들어진 객체가 없으면 만들어지게 함(한번만).
		if(instance == null) { //첫번째 검사 (락 없이 빠르게 체크함)
			instance = new DBManager();
		}
		//만들어진 객체 반환.
		return instance;
	}
	
	//Connection 인스턴스를 반환하는 메서드
	public Connection getConnection() {
		return con;
	}
	
	//데이터베이스 관련 자원 해제하는 메서드(Connection)
	public void release(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//데이터베이스 관련 자원 해제하는 메서드(PreparedStatement)
	public void release(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//데이터베이스 관련 자원 해제하는 메서드(PreparedStatement, ResultSet) - (메서드 오버로딩)
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//데이터베이스 관련 자원 해제하는 메서드(PreparedStatement, ResultSet, Connection) - (메서드 오버로딩)
	public void release(PreparedStatement pstmt, ResultSet rs, Connection con) {
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(con != null) {
			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
