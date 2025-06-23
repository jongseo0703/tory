package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
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
	private LabelComponentSet topCategory;
	private LabelComponentSet subCategory;
	private LabelComponentSet name;
	
	private TopCategory selectedTopCategory;
	private SubCategory selectedSubCategory;
	private JComboBox<TopCategory> topCategoryComboBox;
	private JComboBox<SubCategory> subCategoryComboBox;
	private JTextField nameField;
	
	
	
	IdentificationPanel()
	{
		topCategoryComboBox = new JComboBox<TopCategory>();
		subCategoryComboBox = new JComboBox<SubCategory>();
		nameField = new JTextField();
		
		topCategory = new LabelComponentSet("상위 카테고리", topCategoryComboBox, true);
		subCategory = new LabelComponentSet("하위 카테고리", subCategoryComboBox, false);
		name = new LabelComponentSet("상품 명", nameField, false);
		
		selectedTopCategory = null;
		selectedSubCategory = null;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(subCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(name);
		
		updateTopCategoryComboBox();
	}
	
	
	
	private void updateTopCategoryComboBox()
	{
		TopCategoryDAO topCategoryDAO = new TopCategoryDAO();
		List<TopCategory> topCategorys = topCategoryDAO.selectAll();
		
		topCategoryComboBox.addItemListener((itemEvent)->
		{
			if (itemEvent.getStateChange() == ItemEvent.SELECTED)
			{
				onSelectTopCategory((TopCategory)itemEvent.getItem());
			}
			
		});
		subCategoryComboBox.addItemListener((itemEvent)->
		{
			if (itemEvent.getStateChange() == ItemEvent.SELECTED)
			{
				onSelectSubCategory((SubCategory)itemEvent.getItem());
			}
		});
		nameField.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				System.out.println("focus lost");
				logSelectionState();
			}
			
		});
		
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
	private void onSelectTopCategory(TopCategory selectedTopCategory)
	{
		this.selectedTopCategory = selectedTopCategory;
		subCategoryComboBox.removeAllItems();
		
		if (selectedTopCategory.getTopCategoryId() == 0)
		{
			disableSubComponentOfTopCategory();
			logSelectionState();
			return;
		}
		
		subCategoryComboBox.setEnabled(true);
		updateSubCategoryItem();
		logSelectionState();
	}
	private void disableSubComponentOfTopCategory()
	{
		selectedSubCategory = null;
		subCategoryComboBox.setEnabled(false);
		disableSubComponentOfSubCategory();
	}
	private void disableSubComponentOfSubCategory()
	{
		nameField.setEnabled(false);
	}
	private void updateSubCategoryItem()
	{
		SubCategoryDAO subCategoryDAO = new SubCategoryDAO();
		List<SubCategory> subCategorys = subCategoryDAO.selectByTop(selectedTopCategory);
		
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
	private void onSelectSubCategory(SubCategory selectedSubCategory)
	{
		this.selectedSubCategory = selectedSubCategory;
		nameField.setText(null);
		
		if (selectedSubCategory.getSubCategoryId() == 0)
		{
			disableSubComponentOfSubCategory();
			logSelectionState();
			return;
		}
		
		nameField.setEnabled(true);
		logSelectionState();
	}
	private void logSelectionState()
	{
		System.out.println("===============================================");
		System.out.println(selectedTopCategory);
		System.out.println(selectedSubCategory);
		System.out.println("Text : " + nameField.getText());
	}
}