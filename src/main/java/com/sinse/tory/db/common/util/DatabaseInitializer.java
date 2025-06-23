package com.sinse.tory.db.common.util;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//입출력 관련 패키지 임포트
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//디비 관련 패키지 임포트
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//프로그램 실행 시 자동으로 테이블을 만들어주는 클래스(이미 존재하는 테이블은 생성해주지 않음. -> 충돌방지)
public class DatabaseInitializer {
	
	//이미 선언된 쿼리문 저장 경로 - maven 에서는 CLASSPATH가 기본적으로 잡혀있기 때문
	private static final String SCHEMA_FILE_PATH = "db/schema.sql";
	
	//싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
	static DBManager dbManager = DBManager.getInstance();
	
	//테이블 초기화해주는 메서드
	public static void initializeSchema() {
		
		Connection con = null; //커넥션 객체 초기화
		
		//sql 문을 실행하기 위한 Statement 객체 초기화(여러 개의 쿼리문을 사용하기 때문에 Statement 클래스이용)
		//단일 쿼리문은 PreparedStatement이용
		Statement st = null;
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		
		String line;
		
		try {
			st = con.createStatement(); //쿼리 실행을 위한 Statement 객체 생성
			
			//try-with-resources로 더 안전한 자원 반납
			try (BufferedReader br = new BufferedReader(new FileReader(SCHEMA_FILE_PATH))){
				
				//스키마 파일을 한 줄씩 읽어 sql 에 문자열로 누적
				while((line = br.readLine()) != null) {
					sql.append(line).append("\n");
				}
				
				//쿼리문은 ; 기준으로 실행되기 때문에, ; 기준으로 나누어서 문자열 배열에 저장
				String[] queries = sql.toString().split(";");
				for(String query : queries) {
					query = query.trim(); //쿼리문 앞뒤의 불필요한 공백 제거
					//쿼리가 비어있지 않다면(존재한다면)
					if(!query.isEmpty()) {
						//만든 쿼리를 실행.
						st.executeUpdate(query);
					}
				}
				
				System.out.println("DB 초기화 완료");
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch(SQLException e) {
			System.out.println("DB 초기화 실패");
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(st);
		}
		
	}

}
