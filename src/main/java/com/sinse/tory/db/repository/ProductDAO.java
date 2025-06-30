package com.sinse.tory.db.repository;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//디비 관련 패키지 임포트
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductImage;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;

public class ProductDAO {
	//싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
	DBManager dbManager = DBManager.getInstance();
	
	//전체 정보를 조회하는 메서드
	public List<Product> selectAll() {
		System.out.println("🔍 ProductDAO.selectAll() 시작");
		
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Product> list = new ArrayList<>(); //Product 타입의 객체를 담은 list 생성
		//중복을 방지하기 위한 Map 컬렉션
		//ex) 상품 A가 사이즈를 3개, 이미지를 3개 가지고 있다면 조인결과는 상품 A가 여러 줄로
		//반복되면서 나옴.
		Map<Integer, Product> productMap = new HashMap<>(); 
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		System.out.println("📋 DB 연결 성공");
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//모든 상품을 조회하되 이미지는 LEFT JOIN으로 처리 (이미지 없는 상품도 포함)
		//select 절
		sql.append("SELECT ");
		sql.append("p.product_id, p.product_name, ");
		sql.append("l.location_id, l.location_name, ");
		sql.append("b.brand_id, b.brand_name, ");
		sql.append("s.sub_category_id, s.sub_category_name, ");
		sql.append("t.top_category_id, t.top_category_name, ");
		sql.append("pd.product_detail_id, pd.product_size_name, pd.product_quantity, ");
		sql.append("pi.product_image_id, pi.image_url ");
		//from 절 (INNER JOIN + LEFT JOIN)
		sql.append("FROM Product p ");
		sql.append("INNER JOIN Location l ON p.location_id = l.location_id ");
		sql.append("INNER JOIN Brand b ON l.brand_id = b.brand_id ");
		sql.append("INNER JOIN SubCategory s ON b.sub_category_id = s.sub_category_id ");
		sql.append("INNER JOIN TopCategory t ON s.top_category_id = t.top_category_id ");
		sql.append("INNER JOIN ProductDetail pd ON p.product_id = pd.product_id ");
		sql.append("LEFT JOIN ProductImage pi ON p.product_id = pi.product_id");
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while(rs.next()) {
				int productId = rs.getInt("product_id"); //이미 만들어진 Product 인지 확인하기 위한 productId
				Product product = productMap.get(productId); //해당 productId의 레코드 한 건을 담기위한 객체
				
				//빈 레코드 라면
				if(product == null) {
					product = new Product();
					//product_id컬럼의 데이터를 가져와서 저장
					product.setProductId(rs.getInt("product_id"));
					//product_name컬럼의 데이터를 가져와서 저장
					product.setProductName(rs.getString("product_name"));
					
					Location location = new Location();
					//location_id컬럼의 데이터를 가져와서 저장
					location.setLocationId(rs.getInt("location_id"));
					//location_name컬럼의 데이터를 가져와서 저장
					location.setLocationName(rs.getString("location_name"));
					
					Brand brand = new Brand();
					//brand_id컬럼의 데이터를 가져와서 저장
					brand.setBrandId(rs.getInt("brand_id"));
					//brand_name컬럼의 데이터를 가져와서 저장
					brand.setBrandName(rs.getString("brand_name"));
					
					SubCategory subCategory = new SubCategory();
					//subcategory_id컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryId(rs.getInt("sub_category_id"));
					//subcategory_name컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryName(rs.getString("sub_category_name"));
					
					TopCategory topCategory = new TopCategory();
					//topcategory_id컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryId(rs.getInt("top_category_id"));
					//topcategory_name컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryName(rs.getString("top_category_name"));
					
					//연결
					subCategory.setTopCategory(topCategory);
					brand.setSubCategory(subCategory);
					location.setBrand(brand);
					product.setLocation(location);
					
					//Map 에 productId와 해당되는 레코드 저장
					productMap.put(productId, product);
					//리스트에 레코드 저장
					list.add(product);
				}
				
				ProductDetail productDetail = new ProductDetail();
				//product_detail_id컬럼의 데이터를 가져와서 저장
				productDetail.setProductDetailId(rs.getInt("product_detail_id"));
				//product_size_name컬럼의 데이터를 가져와서 저장
				productDetail.setProductSizeName(rs.getString("product_size_name"));
				//product_quantity컬럼의 데이터를 가져와서 저장
				productDetail.setProductQuantity(rs.getInt("product_quantity"));
				
				// 이미지 정보가 있는 경우에만 추가 (LEFT JOIN이므로 null 체크 필요)
				if (rs.getObject("product_image_id") != null) {
					ProductImage productImage = new ProductImage();
					//productimage_id컬럼의 데이터를 가져와서 저장
					productImage.setProductImageId(rs.getInt("product_image_id"));
					//image_url컬럼의 데이터를 가져와서 저장
					productImage.setImageURL(rs.getString("image_url"));
					
					//이미지 연결
					product.getProductImages().add(productImage);
				}
				
				//ProductDetail 연결
				product.getProductDetails().add(productDetail);
				
			}
			
			System.out.println("🔄 조회 결과: " + list.size() + "개의 상품 로드됨");
			
		} catch(SQLException e) {
			System.err.println("❌ SQL 오류 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt, rs);
		}
		//전체 레코드가 담긴 list 반환
		return list;
	}
	
	//선택된 상자의 상세 정보를 가져오는 메서드
	public List<Product> select(int product_id) {
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Product> list = new ArrayList<>(); //Product 타입의 객체를 담은 list 생성
		//중복을 방지하기 위한 Map 컬렉션
		//ex) 상품 A가 사이즈를 3개, 이미지를 3개 가지고 있다면 조인결과는 상품 A가 여러 줄로
		//반복되면서 나옴.
		Map<Integer, Product> productMap = new HashMap<>(); 
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//선택된 location 에 매칭되는 정보만을 가져오기 위한 쿼리문
		//select 절
		sql.append("select");
		sql.append(" p.product_id, p.product_name, p.product_price,");
		sql.append(" l.location_id, l.location_name,");
		sql.append(" b.brand_id, b.brand_name,");
		sql.append(" s.sub_category_id, s.sub_category_name,");
		sql.append(" t.top_category_id, t.top_category_name,");
		sql.append(" pd.product_detail_id, pd.product_size_name,");
		sql.append(" pd.product_quantity,");
		sql.append(" pi.product_image_id, pi.image_url");
		//from 절(조인)
		sql.append(" from Product p, Location l, Brand b, SubCategory s, TopCategory t, ProductDetail pd, ProductImage pi");
		//where 절(각 아이디가 같아야 해당되는 아이템을 가져옴)
		sql.append(" where p.location_id = l.location_id and");
		sql.append(" l.brand_id = b.brand_id and");
		sql.append(" b.sub_category_id = s.sub_category_id and");
		sql.append(" s.top_category_id = t.top_category_id and");
		sql.append(" p.product_id = pd.product_id and");
		sql.append(" p.product_id = pi.product_id and");
		sql.append(" p.product_id = ?");
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//첫번째 바인딩 변수 = select 메서드의 매개변수인 product_id(즉, 사용자가 선택한 상자)
			pstmt.setInt(1, product_id);
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while(rs.next()) {
				int productId = rs.getInt("product_id"); //이미 만들어진 Product 인지 확인하기 위한 productId
				Product product = productMap.get(productId); //해당 productId의 레코드 한 건을 담기위한 객체
				
				//빈 레코드 라면
				if(product == null) {
					product = new Product();
					//product_id컬럼의 데이터를 가져와서 저장
					product.setProductId(rs.getInt("product_id"));
					//product_name컬럼의 데이터를 가져와서 저장
					product.setProductName(rs.getString("product_name"));
					//product_price컬럼의 데이터를 가져와서 저장
					product.setProductPrice(rs.getInt("product_price"));
					
					Location location = new Location();
					//location_id컬럼의 데이터를 가져와서 저장
					location.setLocationId(rs.getInt("location_id"));
					//location_name컬럼의 데이터를 가져와서 저장
					location.setLocationName(rs.getString("location_name"));
					
					Brand brand = new Brand();
					//brand_id컬럼의 데이터를 가져와서 저장
					brand.setBrandId(rs.getInt("brand_id"));
					//brand_name컬럼의 데이터를 가져와서 저장
					brand.setBrandName(rs.getString("brand_name"));
					
					SubCategory subCategory = new SubCategory();
					//subcategory_id컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryId(rs.getInt("sub_category_id"));
					//subcategory_name컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryName(rs.getString("sub_category_name"));
					
					TopCategory topCategory = new TopCategory();
					//topcategory_id컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryId(rs.getInt("top_category_id"));
					//topcategory_name컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryName(rs.getString("top_category_name"));
					
					//연결
					subCategory.setTopCategory(topCategory);
					brand.setSubCategory(subCategory);
					location.setBrand(brand);
					product.setLocation(location);
					
					//Map 에 productId와 해당되는 레코드 저장
					productMap.put(productId, product);
					//리스트에 레코드 저장
					list.add(product);
				}
				
				ProductDetail productDetail = new ProductDetail();
				//product_detail_id컬럼의 데이터를 가져와서 저장
				productDetail.setProductDetailId(rs.getInt("product_detail_id"));
				//product_size_name컬럼의 데이터를 가져와서 저장
				productDetail.setProductSizeName(rs.getString("product_size_name"));
				//product_quantity컬럼의 데이터를 가져와서 저장
				productDetail.setProductQuantity(rs.getInt("product_quantity"));
				
				ProductImage productImage = new ProductImage();
				//productimage_id컬럼의 데이터를 가져와서 저장
				productImage.setProductImageId(rs.getInt("product_image_id"));
				//image_url컬럼의 데이터를 가져와서 저장
				productImage.setImageURL(rs.getString("image_url"));
				
				//연결
				product.getProductDetails().add(productDetail);
				product.getProductImages().add(productImage);
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt, rs);
		}
		//전체 레코드가 담긴 list 반환
		return list;
	}
	
	//상위카테고리 별로 정렬하기위한 메서드
	public List<Product> selectCategorySummary() {
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<Product> list = new ArrayList<>(); //Product 타입의 객체를 담은 list 생성
		//중복을 방지하기 위한 Map 컬렉션
		//ex) 상품 A가 사이즈를 3개, 이미지를 3개 가지고 있다면 조인결과는 상품 A가 여러 줄로
		//반복되면서 나옴.
		Map<Integer, Product> productMap = new HashMap<>(); 
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		//선택된 location 에 매칭되는 정보만을 가져오기 위한 쿼리문
		//select 절
		sql.append("select");
		sql.append(" p.product_id, p.product_name,");
		sql.append(" l.location_id, l.location_name,");
		sql.append(" b.brand_id, b.brand_name,");
		sql.append(" s.sub_category_id, s.sub_category_name,");
		sql.append(" t.top_category_id, t.top_category_name,");
		sql.append(" pd.product_detail_id, pd.product_size_name,");
		sql.append(" pd.product_quantity,");
		sql.append(" pi.product_image_id, pi.image_url");
		//from 절(조인)
		sql.append(" from Product p, Location l, Brand b, SubCategory s, TopCategory t, ProductDetail pd, ProductImage pi");
		//where 절(각 아이디가 같아야 해당되는 아이템을 가져옴)
		sql.append(" where p.location_id = l.location_id and");
		sql.append(" l.brand_id = b.brand_id and");
		sql.append(" b.sub_category_id = s.sub_category_id and");
		sql.append(" s.top_category_id = t.top_category_id and");
		sql.append(" p.product_id = pd.product_id and");
		sql.append(" p.product_id = pi.product_id");
		//order by 절
		sql.append(" order by case t.top_category_name");
		sql.append(" when '상의' then 1");
		sql.append(" when '하의' then 2");
		sql.append(" when '신발' then 3");
		sql.append(" else 99");
		sql.append(" end");
		
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			//rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while(rs.next()) {
				int productId = rs.getInt("product_id"); //이미 만들어진 Product 인지 확인하기 위한 productId
				Product product = productMap.get(productId); //해당 productId의 레코드 한 건을 담기위한 객체
				
				//빈 레코드 라면
				if(product == null) {
					product = new Product();
					//product_id컬럼의 데이터를 가져와서 저장
					product.setProductId(rs.getInt("product_id"));
					//product_name컬럼의 데이터를 가져와서 저장
					product.setProductName(rs.getString("product_name"));
					
					Location location = new Location();
					//location_id컬럼의 데이터를 가져와서 저장
					location.setLocationId(rs.getInt("location_id"));
					//location_name컬럼의 데이터를 가져와서 저장
					location.setLocationName(rs.getString("location_name"));
					
					Brand brand = new Brand();
					//brand_id컬럼의 데이터를 가져와서 저장
					brand.setBrandId(rs.getInt("brand_id"));
					//brand_name컬럼의 데이터를 가져와서 저장
					brand.setBrandName(rs.getString("brand_name"));
					
					SubCategory subCategory = new SubCategory();
					//subcategory_id컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryId(rs.getInt("sub_category_id"));
					//subcategory_name컬럼의 데이터를 가져와서 저장
					subCategory.setSubCategoryName(rs.getString("sub_category_name"));
					
					TopCategory topCategory = new TopCategory();
					//topcategory_id컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryId(rs.getInt("top_category_id"));
					//topcategory_name컬럼의 데이터를 가져와서 저장
					topCategory.setTopCategoryName(rs.getString("top_category_name"));
					
					//연결
					subCategory.setTopCategory(topCategory);
					brand.setSubCategory(subCategory);
					location.setBrand(brand);
					product.setLocation(location);
					
					//Map 에 productId와 해당되는 레코드 저장
					productMap.put(productId, product);
					//리스트에 레코드 저장
					list.add(product);
				}
				
				ProductDetail productDetail = new ProductDetail();
				//product_detail_id컬럼의 데이터를 가져와서 저장
				productDetail.setProductDetailId(rs.getInt("product_detail_id"));
				//product_size_name컬럼의 데이터를 가져와서 저장
				productDetail.setProductSizeName(rs.getString("product_size_name"));
				//product_quantity컬럼의 데이터를 가져와서 저장
				productDetail.setProductQuantity(rs.getInt("product_quantity"));
				
				ProductImage productImage = new ProductImage();
				//productimage_id컬럼의 데이터를 가져와서 저장
				productImage.setProductImageId(rs.getInt("product_image_id"));
				//image_url컬럼의 데이터를 가져와서 저장
				productImage.setImageURL(rs.getString("image_url"));
				
				//연결
				product.getProductDetails().add(productDetail);
				product.getProductImages().add(productImage);
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//db 자원 반납
			dbManager.release(pstmt, rs);
		}
		//전체 레코드가 담긴 list 반환
		return list;
	}
}
