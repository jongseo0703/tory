package com.sinse.tory.rightpage.datamodification.view;

import java.awt.Dimension;
import java.awt.GridLayout;
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
import com.sinse.tory.rightpage.db.repository.RightPageBrandDAO;
import com.sinse.tory.rightpage.db.repository.RightPageLocationDAO;

/**
 * DetailDataTable
 * - 상품 상세 정보 입력/표시용 테이블 영역
 * - 브랜드, 위치, 사이즈, 가격, 수량, 설명 등의 필드를 포함
 */
final class DetailDataTable extends JPanel {

	private JComboBox<Location> locationComboBox; // 위치 선택
	private JComboBox<Brand> brandComboBox; // 브랜드 선택
	private JTextField sizeField; // 사이즈 입력
	private JFormattedTextField priceField; // 가격 입력 (숫자 전용)
	private JFormattedTextField quantityField; // 수량 입력 (숫자 전용)
	private JTextField descriptionField; // 설명 입력

	/**
	 * 생성자
	 * - 입력 필드 및 콤보박스 초기화
	 * - 각 필드를 TableData 컴포넌트로 감싸고 그리드 형태로 배치
	 */
	DetailDataTable() {
		// 필드 초기화
		locationComboBox = new JComboBox<>();
		brandComboBox = new JComboBox<>();
		sizeField = new JTextField();
		priceField = getFormattedTextField(true); // 가격은 사용 가능
		quantityField = getFormattedTextField(false); // 수량은 비활성화로 초기화
		descriptionField = new JTextField();

		// 전체 레이아웃: 세로 방향 BoxLayout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// 행(row) 단위로 테이블 구성
		addRow(
				new TableData[] {
						new TableData("상품 위치", locationComboBox),
						new TableData("브랜드", brandComboBox)
				},
				new TableData[] {
						new TableData("사이즈", sizeField),
						new TableData("가격", priceField),
						new TableData("수량", quantityField)
				},
				new TableData[] {
						new TableData("설명", descriptionField)
				});

		initializeTable();
	}

	/**
	 * addRow
	 * - TableData 묶음을 그리드 형식으로 한 행(row)에 배치
	 * - 여러 행을 세로로 쌓아 전체 테이블 구성
	 */
	private void addRow(TableData[]... tableDatas) {
		for (TableData[] rowDatas : tableDatas) {
			JPanel row = new JPanel();
			row.setLayout(new GridLayout(1, rowDatas.length)); // 항목 수에 따라 열 자동 정렬

			for (TableData data : rowDatas) {
				row.add(data); // TableData를 패널에 추가
			}

			// 폭은 최대, 높이는 구성 요소에 맞춤
			int rowPreferredHeight = row.getPreferredSize().height;
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPreferredHeight));
			add(row);
		}
	}

	/**
	 * getFormattedTextField
	 * - 숫자 전용 텍스트필드 생성 (활성 여부 지정)
	 */
	private JFormattedTextField getFormattedTextField(boolean enabled) {
		NumberFormatter formatter = new NumberFormatter();
		formatter.setValueClass(Integer.class);

		JFormattedTextField textField = new JFormattedTextField(formatter);
		textField.setEnabled(enabled);

		return textField;
	}

	/**
	 * insertDetailData
	 * - DB에서 가져온 ProductDetail 데이터를 각 입력 필드에 주입
	 */
	void insertDetailData(ProductDetail productDetail) {
		selectLocationById(productDetail.getProduct().getLocation().getLocationId());
		selectBrandById(productDetail.getProduct().getLocation().getBrand().getBrandId());
		sizeField.setText(productDetail.getProductSizeName());
		priceField.setValue(productDetail.getProduct().getProductPrice());
		quantityField.setValue(productDetail.getProductQuantity());
		descriptionField.setText(productDetail.getProduct().getDescription());
	}

	/**
	 * selectLocationById
	 * - 콤보박스에서 해당 ID의 Location 항목을 선택
	 */
	private void selectLocationById(int targetId) {
		for (int i = 0; i < locationComboBox.getItemCount(); i++) {
			Location item = locationComboBox.getItemAt(i);
			if (item.getLocationId() == targetId) {
				locationComboBox.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * selectBrandById
	 * - 콤보박스에서 해당 ID의 Brand 항목을 선택
	 */
	private void selectBrandById(int targetId) {
		for (int i = 0; i < brandComboBox.getItemCount(); i++) {
			Brand item = brandComboBox.getItemAt(i);
			if (item.getBrandId() == targetId) {
				brandComboBox.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * initializeTable
	 * - 브랜드, 위치 콤보박스 초기화 및 수량 기본값 설정
	 */
	private void initializeTable() {
		initializeLocationComboBox();
		initializeBrandComboBox();
		quantityField.setValue(0); // 수량 기본값
	}

	/**
	 * initializeLocationComboBox
	 * - 위치 콤보박스에 "선택하세요" 더미와 DB 항목 추가
	 */
	private void initializeLocationComboBox() {
		Location dummy = new Location();
		dummy.setLocationId(0);
		dummy.setLocationName("선택 하세요.");
		locationComboBox.addItem(dummy);

		for (Location location : RightPageLocationDAO.selectAllName()) {
			locationComboBox.addItem(location);
		}
	}

	/**
	 * initializeBrandComboBox
	 * - 브랜드 콤보박스에 "선택하세요" 더미와 DB 항목 추가
	 */
	private void initializeBrandComboBox() {
		Brand dummy = new Brand();
		dummy.setBrandId(0);
		dummy.setBrandName("선택 하세요.");
		brandComboBox.addItem(dummy);

		for (Brand brand : RightPageBrandDAO.selectAllName()) {
			brandComboBox.addItem(brand);
		}
	}

	/**
	 * isInputAll
	 * - 필수 항목이 모두 입력되었는지 검증
	 */
	boolean isInputAll() {
		return locationComboBox.getSelectedIndex() != 0 &&
				brandComboBox.getSelectedIndex() != 0 &&
				!sizeField.getText().isEmpty() &&
				priceField.getValue() != null;
	}

	/**
	 * createProductDetailFromInputted
	 * - 사용자가 입력한 값을 바탕으로 ProductDetail 객체를 생성
	 *
	 * @param subCategory 하위 카테고리
	 * @param productName 상품 이름
	 * @return 입력 기반으로 구성된 ProductDetail 객체
	 */
	ProductDetail createProductDetailFromInputted(SubCategory subCategory, String productName) {
		Brand selectedBrand = (Brand) brandComboBox.getSelectedItem();
		Location selectedLocation = (Location) locationComboBox.getSelectedItem();

		// 새 객체 구성
		Brand brand = new Brand();
		brand.setBrandId(selectedBrand.getBrandId());
		brand.setSubCategory(subCategory);
		brand.setBrandName(selectedBrand.getBrandName());

		Location location = new Location();
		location.setLocationId(selectedLocation.getLocationId());
		location.setBrand(brand);
		location.setLocationName(selectedLocation.getLocationName());

		Product product = new Product();
		product.setLocation(location);
		product.setProductPrice(((Number) priceField.getValue()).intValue());
		product.setDescription(descriptionField.getText());
		product.setProductName(productName);

		ProductDetail productDetail = new ProductDetail();
		productDetail.setProduct(product);
		productDetail.setProductSizeName(sizeField.getText());

		return productDetail;
	}

	/**
	 * reset
	 * - 모든 입력 필드를 초기 상태로 되돌림
	 */
	void reset() {
		brandComboBox.setSelectedIndex(0);
		locationComboBox.setSelectedIndex(0);
		priceField.setValue(0);
		sizeField.setText("");
		descriptionField.setText("");
	}
}