package com.sinse.tory.rightpage.util;

import java.util.ArrayList;
import java.util.List;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;
/**
 * 문장에 특정 단어를 뽑아내기
 */
public class KeywordText {
	//문장안에 있는 topCategory의 이름
	String topName =null; 
	//문장안에 있는 topCategory의 id
	int topId = 0;
	//문장안에 있는 subCategory의 이름
	String subName =null;
	//문장안에 있는 subCategory의 id
	int subId = 0;
	TopCategoryDAO topCategoryDAO;
	SubCategoryDAO subCategoryDAO;
	List<TopCategory>topList;
	List<SubCategory>subList;
	/**
	 * 문장안에 있는 원한는 단어들이 있는지 찾기
	 */
	public KeywordText() {
		//test용 문장
		String sentence = "상의 몇 개 있어?";
		
		topCategoryDAO = new TopCategoryDAO();
		subCategoryDAO = new SubCategoryDAO();
		topList = new ArrayList<TopCategory>();
		subList = new ArrayList<SubCategory>();
		
		topList = topCategoryDAO.selectAll();
		//문장안에 topCategory의 이름이 있는지 반복문으로 찾기
		for(TopCategory topCategory : topList) {
			//문장속 단어들과 topCategory의 이름 비교
			if(sentence.contains(topCategory.getTopCategoryName())) {
				//문장안에 있는 topCategory의 이름
				topName = topCategory.getTopCategoryName();	
				//문장안에 있는 topCategory의 id
				topId = topCategory.getTopCategoryId();
			}
		
		}
		System.out.println(topName);
	}
	
	public static void main(String[] args) {
		new KeywordText();
	}
}
