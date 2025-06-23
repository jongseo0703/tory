package com.sinse.tory.rightpage.identifier;

import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.db.repository.TopCategoryDAO;

abstract class IdentifierUpdate<T extends JComponent>
{
	private JComboBox<TopCategory> topCategoryComboBox;
	private JComboBox<SubCategory> subCategoryComboBox;
	private T name;
	
	
	
	IdentifierUpdate(JComboBox<TopCategory> topCategoryComboBox, JComboBox<SubCategory> subCategoryComboBox, T name)
	{
		this.topCategoryComboBox = topCategoryComboBox;
		this.subCategoryComboBox = subCategoryComboBox;
		this.name = name;
		
		// #region 카테고리 선택 이벤트 추가
		topCategoryComboBox.addItemListener((itemEvent) ->
		{
			if (itemEvent.getStateChange() == ItemEvent.SELECTED)
			{
				onSelectTopCategory((TopCategory)itemEvent.getItem());
			}
			
		});
		subCategoryComboBox.addItemListener((itemEvent) ->
		{
			if (itemEvent.getStateChange() == ItemEvent.SELECTED)
			{
				onSelectSubCategory((SubCategory)itemEvent.getItem());
			}
		});
		// #endregion		
		
		// top category 이름 초기화
		initializeTopCategoryComboBoxItem();
	}
	
	
	
	private void initializeTopCategoryComboBoxItem()
	{
		TopCategoryDAO topCategoryDAO = new TopCategoryDAO();
		List<TopCategory> topCategorys = topCategoryDAO.selectAll();
		
		
		
		// combo box item 초기화
		TopCategory dummy = new TopCategory();
		dummy.setTopCategoryId(0);
		dummy.setTopCategoryName("선택 하세요.");
		
		topCategoryComboBox.addItem(dummy);
		for(int i = 0; i < topCategorys.size(); i++)
		{
			topCategoryComboBox.addItem(topCategorys.get(i));
		}
		//
	}
	// top category 선택 이벤트
	private void onSelectTopCategory(TopCategory selectedTopCategory)
	{
		subCategoryComboBox.removeAllItems();
		
		// 더미를 선택했을 때 하위 카테고리, 이름 입력 컴포넌트 비활성화
		if (selectedTopCategory.getTopCategoryId() == 0)
		{
			disableSubComponentOfTopCategory();
			return;
		}
		
		subCategoryComboBox.setEnabled(true);
		updateSubCategoryItem();
	}
	// 하위 카테고리, 이름 입력 컴포넌트 비활성화
	private void disableSubComponentOfTopCategory()
	{
		subCategoryComboBox.setEnabled(false);
		disableSubComponentOfSubCategory();
	}
	// 이름 입력 컴포넌트 비활성화
	private void disableSubComponentOfSubCategory()
	{
		name.setEnabled(false);
	}
	// 하위 카테고리 콤보 박스 요소 최신화
	private void updateSubCategoryItem()
	{
		SubCategoryDAO subCategoryDAO = new SubCategoryDAO();
		List<SubCategory> subCategorys = subCategoryDAO.selectByTop((TopCategory)topCategoryComboBox.getSelectedItem());
		
		SubCategory dummy = new SubCategory();
		dummy.setSubCategoryId(0);
		dummy.setSubCategoryName("선택 하세요.");
		dummy.setTopCategory(null);
		
		subCategoryComboBox.addItem(dummy);
		for(int i = 0; i < subCategorys.size(); i++)
		{
			subCategoryComboBox.addItem(subCategorys.get(i));
		}
	}
	// 하위 카테고리 콤보 박스 선택 이벤트
	private void onSelectSubCategory(SubCategory selectedSubCategory)
	{
		clear(name);
		
		if (selectedSubCategory.getSubCategoryId() == 0)
		{
			disableSubComponentOfSubCategory();
			return;
		}
		
		name.setEnabled(true);
	}
	protected abstract void clear(T name);
}