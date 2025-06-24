package com.sinse.tory.rightpage.util;

import java.util.ArrayList;
import java.util.List;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

public class KeywordText {
	String topName =null;
	int topId = 0;
	String subName =null;
	int subId = 0;
	TopCategoryDAO topCategoryDAO;
	SubCategoryDAO subCategoryDAO;
	List<TopCategory>topList;
	List<SubCategory>subList;
	public KeywordText() {
		String sentence = "상의 중에 백맥 몇 개 있어?";
		
		topCategoryDAO = new TopCategoryDAO();
		subCategoryDAO = new SubCategoryDAO();
		topList = new ArrayList<TopCategory>();
		subList = new ArrayList<SubCategory>();
		
		topList = topCategoryDAO.selectAll();
		for(TopCategory topCategory : topList) {
			if(sentence.contains(topCategory.getTopCategoryName())) {
				topName = topCategory.getTopCategoryName();	
				topId = topCategory.getTopCategoryId();
				this.subList = subCategoryDAO.selectByTop(topCategory);
			}
		
		}
	}	
	public static void main(String[] args) {
		new KeywordText();
	}
}
