package com.sinse.tory;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.common.util.DatabaseInitializer;
import com.sinse.tory.main.view.MainPage;

public class App {
	
	public static void main(String[] args) {
		//프로그램이 실행할때 필요한 테이블들을 만들어주고 임시 데이터들을 넣어줌.(CREATE TABLE + INSERT)
		//DatabaseInitializer.initializeSchema();
		new MainPage(); //메인페이지 실행.
	}
	
}
