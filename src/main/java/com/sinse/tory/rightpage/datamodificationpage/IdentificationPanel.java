package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.TopCategoryDAO;

final class IdentificationPanel extends JPanel
{
	// TODO : 나중에 제네릭 타입 model 객체로 변경해야 함
	private LabelComponentSet topCategory;
	private LabelComponentSet subCategory;
	private LabelComponentSet name;
	//
	
	
	
	IdentificationPanel()
	{
		topCategory = new LabelComponentSet("상위 카테고리", getTopCategoryComboBox());
		subCategory = new LabelComponentSet("하위 카테고리", new JComboBox<String>());
		name = new LabelComponentSet("상품 명", new JTextField());
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(topCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(subCategory);
		add(Box.createRigidArea(new Dimension(0, 12)));
		add(name);
	}
	
	
	
	private JComboBox<TopCategory> getTopCategoryComboBox()
	{
		JComboBox<TopCategory> comboBox = new JComboBox<TopCategory>();
		TopCategoryDAO topCategoryDAO = new TopCategoryDAO();
		List<TopCategory> topCategorys = topCategoryDAO.selectAll();
		
		TopCategory dummy = new TopCategory();
		dummy.setTopCategoryId(0);
		dummy.setTopCategoryName("상위 카테고리를 선택 하세요.");
		
		comboBox.addItem(dummy);
		for(int i = 0; i < topCategorys.size(); i++)
		{
			comboBox.addItem(topCategorys.get(i));
		}
		
		return comboBox;
	}
}