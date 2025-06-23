package com.sinse.tory.rightpage.identifier;

import java.util.List;

import javax.swing.JComboBox;

import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.rightpage.db.repository.RightPageProductDAO;

public final class IdentifierUpdateWithNameComboBox extends IdentifierUpdate<JComboBox<Product>>
{
	private JComboBox<SubCategory> subCategoryComboBox;
	
	
	
	public IdentifierUpdateWithNameComboBox(JComboBox<TopCategory> topCategoryComboBox, JComboBox<SubCategory> subCategoryComboBox, JComboBox<Product> name)
	{
		super(topCategoryComboBox, subCategoryComboBox, name);
		this.subCategoryComboBox = subCategoryComboBox;
	}
	
	
	
	@Override
	protected void clearNameComponent(JComboBox<Product> name)
	{
		name.removeAllItems();
	}
	@Override
	protected void updateNameComponent(JComboBox<Product> name)
	{
		List<Product> products = RightPageProductDAO.select((SubCategory)subCategoryComboBox.getSelectedItem());
		
		Product dummy = new Product();
		dummy.setProductName("선택 하세요.");
		
		name.addItem(dummy);
		System.out.println("======================================");
		for(int i = 0; i < products.size(); i++)
		{
			System.out.println(products.get(i));
			name.addItem(products.get(i));
		}
	}
}