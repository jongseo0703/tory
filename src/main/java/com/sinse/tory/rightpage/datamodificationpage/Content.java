package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;

// 헤더 구역 외 나머지. 
public final class Content extends JPanel
{
	// 이미지, 상품 식별 드롭 다운 패널
	private JPanel simpleDataPanel;	
	private ImageUploadPanel imageUploadPanel;
	private IdentificationPanel identificationPanel;
	//
	
	private DetailDataTable detailDataTable;
	
	private static final int HORIZONTAL_MARGIN = 12;
	
	
	
	public Content()
	{
		simpleDataPanel = new JPanel();
		imageUploadPanel = new ImageUploadPanel();
		identificationPanel = new IdentificationPanel();
		detailDataTable = new DetailDataTable();
		
		// 내부 동서남북 마진
		setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));
		// 레이아웃에 포함되는 요소들의 간격
		setLayout(new GridLayout(2, 1, 0, 16));
		
		add(simpleDataPanel);
		add(detailDataTable);

		// 레이아웃에 포함되는 요소들의 간격
		simpleDataPanel.setLayout(new GridLayout(1, 2, 16, 0));
		simpleDataPanel.add(imageUploadPanel);
		simpleDataPanel.add(identificationPanel);
	}
	
	
	
	public DataManagementFromTable createDataManagementFromTable()
	{
		return new DataManagementFromTable(imageUploadPanel, identificationPanel, detailDataTable);
	}
	void fillData(int topCategoryID, int subCategoryID, String name) {
		identificationPanel.insertProductIdentifier(topCategoryID, subCategoryID, name);
		List<ProductDetail> productDetails = RightPageProductDetailDAO.selectProductDetailsByProductName(name);
		detailDataTable.insertDetailData(productDetails.get(0));
	}
}