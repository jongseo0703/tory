package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.rightpage.identifier.IdentifierUpdateWithNameField;

final class IdentificationPanel extends JPanel {
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
	
	IdentificationPanel() {
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
	
	/**
	 * 매개변수 데이터들로 UI들을 초기화하는 함수.
	 * */ 
	void insertProductIdentifier(int topCategoryID, int subCategoryID, String name) {
		selectTopCategoryById(topCategoryID);
		selectSubCategoryById(subCategoryID);
		nameField.setText(name);
	}
	public static boolean comboBoxContains(JComboBox<?> comboBox, Object target) {
	    for (int i = 0; i < comboBox.getItemCount(); i++) {
	        Object item = comboBox.getItemAt(i);
	        if (item.equals(target)) { // equals 오버라이딩 되어 있어야 정확함
	            return true;
	        }
	    }
	    return false;
	}
	private void selectTopCategoryById(int targetId) {
	    for (int i = 0; i < topCategoryComboBox.getItemCount(); i++) {
	        TopCategory item = topCategoryComboBox.getItemAt(i);
	        if (item.getTopCategoryId() == targetId) {
	        	topCategoryComboBox.setSelectedIndex(i);
	            return;
	        }
	    }
	}
	private void selectSubCategoryById(int targetId) {
	    for (int i = 0; i < subCategoryComboBox.getItemCount(); i++) {
	        SubCategory item = subCategoryComboBox.getItemAt(i);
	        if (item.getSubCategoryId() == targetId) {
	        	subCategoryComboBox.setSelectedIndex(i);
	            return;
	        }
	    }
	}
	boolean isSelectAll() {
		return nameField.getText().isEmpty() == false;
	}
	SubCategory createSubCategoryFromInputted() {
		TopCategory selectedTopCategory = (TopCategory)topCategoryComboBox.getSelectedItem();
		SubCategory selectedSubCategory = (SubCategory)subCategoryComboBox.getSelectedItem();
		TopCategory topCategory = new TopCategory();
		SubCategory subCategory = new SubCategory();
		
		topCategory.setTopCategoryId(selectedTopCategory.getTopCategoryId());
		topCategory.setTopCategoryName(selectedTopCategory.getTopCategoryName());
		subCategory.setSubCategoryId(selectedSubCategory.getSubCategoryId());
		subCategory.setSubCategoryName(selectedSubCategory.getSubCategoryName());
		subCategory.setTopCategory(topCategory);
		
		return subCategory;
	}
	String getProductName() {
		return nameField.getText();
	}
	/**
	 * 현재 선택 상태 확인
	 */
	private void logSelectionState() {
		System.out.println("===============================================");
		System.out.println((TopCategory)topCategoryComboBox.getSelectedItem());
		System.out.println((SubCategory)subCategoryComboBox.getSelectedItem());
		System.out.println("Text : " + nameField.getText());
	}
	void reset() {
		identifierUpdateWithNameField.reset();
	}
}
