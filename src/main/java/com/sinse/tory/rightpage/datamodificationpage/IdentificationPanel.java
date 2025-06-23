package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.rightpage.identifier.IdentifierUpdateWithNameField;

final class IdentificationPanel extends JPanel
{
	// #region 상품 식별 정보 UI
	private JComboBox<TopCategory> topCategoryComboBox;
	private JComboBox<SubCategory> subCategoryComboBox;
	private JTextField nameField;
	// #endregion
	
	private LabelComponentSet topCategory;
	private LabelComponentSet subCategory;
	private LabelComponentSet name;

	// #region 선택한 카테고리들 저장
	private IdentifierUpdateWithNameField identifierUpdateWithNameField;
	// #endregion
	
	
	
	IdentificationPanel()
	{
		topCategoryComboBox = new JComboBox<TopCategory>();
		subCategoryComboBox = new JComboBox<SubCategory>();
		nameField = new JTextField();
		
		topCategory = new LabelComponentSet("상위 카테고리", topCategoryComboBox, true);
		subCategory = new LabelComponentSet("하위 카테고리", subCategoryComboBox, false);
		name = new LabelComponentSet("상품 명", nameField, false);
		
		identifierUpdateWithNameField = new IdentifierUpdateWithNameField(topCategoryComboBox, subCategoryComboBox, nameField);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(subCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(name);
	}
	
	
	
	// 매개변수 데이터들로 UI들을 초기화하는 함수.
	void insertProductIdentifier(TopCategory topCategory, SubCategory subCategory, String name)
	{
		topCategoryComboBox.setSelectedItem(topCategory);
		subCategoryComboBox.setSelectedItem(subCategory);
		nameField.setText(name);
	}
	
	
	
	// 현재 선택 상태 확인
	private void logSelectionState()
	{
		System.out.println("===============================================");
		System.out.println((TopCategory)topCategoryComboBox.getSelectedItem());
		System.out.println((SubCategory)subCategoryComboBox.getSelectedItem());
		System.out.println("Text : " + nameField.getText());
	}
}