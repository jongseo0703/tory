package com.sinse.tory.rightpage.identifier;

import javax.swing.JComboBox;

import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;

public final class IdentifierUpdateWithNameComboBox extends IdentifierUpdate<JComboBox<Product>>
{
	public IdentifierUpdateWithNameComboBox(JComboBox<TopCategory> topCategoryComboBox, JComboBox<SubCategory> subCategoryComboBox, JComboBox<Product> name)
	{
		super(topCategoryComboBox, subCategoryComboBox, name);
	}
	
	
	
	@Override
	protected void clear(JComboBox<Product> name)
	{
		name.removeAllItems();
	}
}