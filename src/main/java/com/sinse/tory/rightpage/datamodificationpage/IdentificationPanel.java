package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.EventListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

final class IdentificationPanel extends JPanel
{
	// TODO : 나중에 제네릭 타입 model 객체로 변경해야 함
	private LabelComponentSet topCategory;
	private LabelComponentSet subCategory;
	private LabelComponentSet name;
	//
	
	private TopCategory selectedTopCategory;
	private SubCategory selectedSubCategory;
	
	private JComboBox<TopCategory> topCategoryComboBox;
	private JComboBox<SubCategory> subCategoryComboBox;
	
	
	
	IdentificationPanel()
	{
		topCategoryComboBox = new JComboBox<TopCategory>();
		subCategoryComboBox = new JComboBox<SubCategory>();
		
		topCategory = new LabelComponentSet("상위 카테고리", topCategoryComboBox, true);
		subCategory = new LabelComponentSet("하위 카테고리", new JComboBox<String>(), true);
		name = new LabelComponentSet("상품 명", new JTextField(), false);
		
		selectedTopCategory = null;
		selectedSubCategory = null;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(subCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(name);
	}
	
	
	
	private void updateTopCategoryComboBox()
	{
		TopCategoryDAO topCategoryDAO = new TopCategoryDAO();
		List<TopCategory> topCategorys = topCategoryDAO.selectAll();
		
		// 다른 컴포넌트들 비활성화, 활성화
		topCategoryComboBox.addItemListener(new ComboBoxSelectionAction(subCategoryComboBox));
		
		// combo box item 초기화
		TopCategory dummy = new TopCategory();
		dummy.setTopCategoryId(0);
		dummy.setTopCategoryName("상위 카테고리를 선택 하세요.");
		
		topCategoryComboBox.addItem(dummy);
		for(int i = 0; i < topCategorys.size(); i++)
		{
			topCategoryComboBox.addItem(topCategorys.get(i));
		}
		//
	}
	// TODO : top category 선택할 때마다 최신화
	private JComboBox<SubCategory> getSubCategoryComboBox()
	{
		JComboBox<SubCategory> comboBox = new JComboBox<SubCategory>();
		SubCategoryDAO subCategoryDAO = new SubCategoryDAO();
		List<SubCategory> subCategorys = subCategoryDAO.selectByTop(selectedTopCategory);
		
		SubCategory dummy = new SubCategory();
		dummy.setSubCategoryId(0);
		dummy.setSubCategoryName("상위 카테고리를 선택 하세요.");
		
		comboBox.addItem(dummy);
		for(int i = 0; i < subCategorys.size(); i++)
		{
			comboBox.addItem(subCategorys.get(i));
		}
		
		return comboBox;
	}
	private void updateIdentifierState()
	{
		
	}
}