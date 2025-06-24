package com.sinse.tory.rightpage.identifier;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;

public final class IdentifierUpdateWithNameField extends IdentifierUpdate<JTextField>
{
	public IdentifierUpdateWithNameField(JComboBox<TopCategory> topCategoryComboBox, JComboBox<SubCategory> subCategoryComboBox, JTextField name)
	{
		super(topCategoryComboBox, subCategoryComboBox, name);
	}
	
	
	
	@Override
	protected void clearNameComponent(JTextField name)
	{
		name.setText(null);
	}
	@Override
	protected void updateNameComponent(JTextField name)
	{
		
	}
}
