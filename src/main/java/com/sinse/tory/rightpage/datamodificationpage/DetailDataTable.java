package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.BrandDAO;
import com.sinse.tory.db.repository.LocationDAO;
import com.sinse.tory.rightpage.db.repository.RightPageBrandDAO;
import com.sinse.tory.rightpage.db.repository.RightPageLocationDAO;

// Content 부분의 테이블 영역
final class DetailDataTable extends JPanel
{
	private JComboBox<Location> locationComboBox;
	private JComboBox<Brand> brandComboBox;
	private JTextField sizeField;
	private JFormattedTextField priceField;
	private JFormattedTextField quantityField;
	
	private JTextField descriptionField;
	
	
	
	DetailDataTable(ProductDetail productDetail)
	{
		locationComboBox = new JComboBox<Location>();
		brandComboBox = new JComboBox<Brand>();
		sizeField = new JTextField();
		priceField = getFormattedTextField(true);
		quantityField = getFormattedTextField(false);
		descriptionField = new JTextField();
		
		
		
		// 열을 세로로 정렬하기 위한 레이아웃
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// 테이블에 열을 채우는 과정.
		addRow
		(
			new TableData[]
			{
				new TableData("상품 위치", locationComboBox),
				new TableData("브랜드", brandComboBox)
			},
			new TableData[]
			{
				new TableData("사이즈", sizeField),
				new TableData("가격", priceField),
				new TableData("수량", quantityField)
			},
			new TableData[]
			{
				new TableData("설명", descriptionField)
			}
		);
		
		if (productDetail != null)
		{
			insertDetailData(productDetail);
		}
		else
		{
			initializeTable();
		}
	}

	
	
	// 테이블에 TableData를 추가하는 함수
	// 한 열에 n개의 TableData를 삽입할 수 있음
	private void addRow(TableData[]... tableDatas)
	{
		// 삽입할 열의 수만큼 반복
		for (int i = 0; i < tableDatas.length; i++)
		{
			// 열 패널
			JPanel row = new JPanel();
			for (int j = 0; j < tableDatas[i].length; j++)
			{
				// 현재 열에 TableData 삽입
				row.add(tableDatas[i][j]);
			}
			// 열 내의 TableData 수에 맞게 너비를 자동 조정
			row.setLayout(new GridLayout(1, tableDatas[i].length));
			add(row);
			
			// 높이는 TableData의 높이에 맞게 조정
			int rowPreferredHeight = row.getPreferredSize().height;
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPreferredHeight));
		}
	}
	private JFormattedTextField getFormattedTextField(boolean enabled)
	{
		NumberFormatter numberFormatter = new NumberFormatter();
		JFormattedTextField textField = new JFormattedTextField(numberFormatter);
		
		numberFormatter.setValueClass(Integer.class);
		textField.setEnabled(enabled);
		
		return textField;
	}
	// #endregion
	// 
	void insertDetailData(ProductDetail productDetail)
	{
		selectLocationById(productDetail.getProduct().getLocation().getLocationId());
		selectBrandById(productDetail.getProduct().getLocation().getBrand().getBrandId());
		sizeField.setText(productDetail.getProductSizeName());
		priceField.setValue(productDetail.getProduct().getProductPrice());
		quantityField.setValue(productDetail.getProductQuantity());
		descriptionField.setText(productDetail.getProduct().getDescription());
	}
	private void selectLocationById(int targetId) {
	    for (int i = 0; i < locationComboBox.getItemCount(); i++) {
	        Location item = locationComboBox.getItemAt(i);
	        if (item.getLocationId() == targetId) {
	        	locationComboBox.setSelectedIndex(i);
	            return;
	        }
	    }
	}
	private void selectBrandById(int targetId) {
	    for (int i = 0; i < brandComboBox.getItemCount(); i++) {
	        Brand item = brandComboBox.getItemAt(i);
	        if (item.getBrandId() == targetId) {
	        	brandComboBox.setSelectedIndex(i);
	            return;
	        }
	    }
	}
	private void initializeTable()
	{
		initializeLocationComboBox();
		initializeBrandComboBox();
		quantityField.setValue(0);
	}
	private void initializeLocationComboBox()
	{
		Location dummy = new Location();
		dummy.setLocationId(0);
		dummy.setLocationName("선택 하세요.");
		
		locationComboBox.addItem(dummy);
		for (Location location : RightPageLocationDAO.selectAllName())
		{
			locationComboBox.addItem(location);
		}
	}
	private void initializeBrandComboBox()
	{
		Brand dummy = new Brand();
		dummy.setBrandId(0);
		dummy.setBrandName("선택 하세요.");
		
		brandComboBox.addItem(dummy);
		for (Brand brand : RightPageBrandDAO.selectAllName())
		{
			brandComboBox.addItem(brand);
		}
	}
	boolean isInputAll()
	{
		return
			locationComboBox.getSelectedIndex() != 0 &&
			brandComboBox.getSelectedIndex() != 0 &&
			sizeField.getText().isEmpty() == false &&
			priceField.getValue() != null;
	}
	ProductDetail createProductDetailFromInputted(SubCategory subCategory, String productName)
	{
		Brand selectedBrand = (Brand)brandComboBox.getSelectedItem();
		Location selectedLocation = (Location)locationComboBox.getSelectedItem();
		
		Brand brand = new Brand();
		Location location = new Location();
		Product product = new Product();
		ProductDetail productDetail = new ProductDetail();
		
		brand.setBrandId(selectedBrand.getBrandId());
		brand.setSubCategory(subCategory);
		brand.setBrandName(selectedBrand.getBrandName());
		location.setLocationId(selectedLocation.getLocationId());
		location.setBrand(brand);
		location.setLocationName(selectedLocation.getLocationName());
		product.setLocation(location);
		product.setProductPrice(((Number) priceField.getValue()).intValue());
		product.setDescription(descriptionField.getText());
		product.setProductName(productName);
		productDetail.setProduct(product);
		productDetail.setProductSizeName(sizeField.getText());
		
		return productDetail;
	}
	void reset()
	{
		brandComboBox.setSelectedIndex(0);
		locationComboBox.setSelectedIndex(0);
		priceField.setValue((Number)0);
		sizeField.setText("");
		descriptionField.setText("");
	}
}