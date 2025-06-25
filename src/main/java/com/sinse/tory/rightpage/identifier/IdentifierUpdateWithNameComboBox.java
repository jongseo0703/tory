package com.sinse.tory.rightpage.identifier;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.lang.model.element.NestingKind;
import javax.swing.JComboBox;

import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;
import com.sinse.tory.db.repository.SubCategoryDAO;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;

public final class IdentifierUpdateWithNameComboBox extends IdentifierUpdate<JComboBox<ProductDetail>>
{
	private JComboBox<SubCategory> subCategoryComboBox;
	private String itemName;
	private int itemID;
	
	public IdentifierUpdateWithNameComboBox(JComboBox<TopCategory> topCategoryComboBox, JComboBox<SubCategory> subCategoryComboBox, JComboBox<ProductDetail> name)
	{
		super(topCategoryComboBox, subCategoryComboBox, name);
		this.subCategoryComboBox = subCategoryComboBox;
		
	}
	
	
	
	@Override
	protected void clearNameComponent(JComboBox<ProductDetail> name)
	{
		name.removeAllItems();
	}
	@Override
	protected void updateNameComponent(JComboBox<ProductDetail> name)
	{
		List<ProductDetail> products = RightPageProductDetailDAO.selectProductDetail((SubCategory)subCategoryComboBox.getSelectedItem());
		
		ProductDetail dummy = new ProductDetail();
		Product product = new Product();
		
		product.setProductName("선택 하세요.");
		dummy.setProduct(product);
		dummy.setProductSizeName("");
		
		name.addItem(dummy);
		for(int i = 0; i < products.size(); i++)
		{
			name.addItem(products.get(i));
		}
	}
	public ProductDetail getName(){	
		return (ProductDetail)name.getSelectedItem(); 
	}
	public String getItemName() {
		itemName = getName().getProduct().getProductName();
		return itemName;
	}
	public int getItemID() {
		itemID = getName().getProduct().getProductId();
		return itemID;
	}
}



