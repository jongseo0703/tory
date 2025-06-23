package com.sinse.tory.rightpage.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.ProductImage;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDAO;

public final class RightPageProductDAO
{
	public static List<Product> select(SubCategory subCategoryToGetID)
	{
		DBManager dbManager = DBManager.getInstance();
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
		
		sql.append("select");
		sql.append(" p.product_id, p.product_name,					");
		sql.append(" l.location_id, l.location_name,				");
		sql.append(" b.brand_id, b.brand_name,						");
		sql.append(" s.sub_category_id, s.sub_category_name,		");
		sql.append(" t.top_category_id, t.top_category_name,		");
		sql.append(" pd.product_detail_id, pd.product_size_name,	");
		sql.append(" pd.product_quantity,							");
		sql.append(" pi.product_image_id, pi.image_url				");
		//from 절(조인)
		sql.append(" from Product p, Location l, Brand b, SubCategory s, TopCategory t, ProductDetail pd, ProductImage pi	");
		sql.append(" where p.location_id = l.location_id and																");
		sql.append(" l.brand_id = b.brand_id and																			");
		sql.append(" b.sub_category_id = ?;																					");
		
		try
		{
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//첫번째 바인딩 변수 = select 메서드의 매개변수인 product_id(즉, 사용자가 선택한 상자)
			pstmt.setInt(1, subCategoryToGetID.getSubCategoryId());
			System.out.println(subCategoryToGetID.getSubCategoryId());
			rs = pstmt.executeQuery();
			
			// rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while (rs.next())
			{
				int productId = rs.getInt("product_id");
				Product product = productMap.get(productId);
				
				//빈 레코드 라면
				if (product == null)
				{
					product = new Product();
					product.setProductId(rs.getInt("product_id"));
					product.setProductName(rs.getString("product_name"));
					
					Location location = new Location();
					location.setLocationId(rs.getInt("location_id"));
					location.setLocationName(rs.getString("location_name"));
					
					Brand brand = new Brand();
					brand.setBrandId(rs.getInt("brand_id"));
					brand.setBrandName(rs.getString("brand_name"));
					
					SubCategory subCategory = new SubCategory();
					subCategory.setSubCategoryId(rs.getInt("sub_category_id"));
					subCategory.setSubCategoryName(rs.getString("sub_category_name"));
					
					TopCategory topCategory = new TopCategory();
					topCategory.setTopCategoryId(rs.getInt("top_category_id"));
					topCategory.setTopCategoryName(rs.getString("top_category_name"));
					
					subCategory.setTopCategory(topCategory);
					brand.setSubCategory(subCategory);
					location.setBrand(brand);
					product.setLocation(location);
					
					productMap.put(productId, product);
					list.add(product);
				}
				
				ProductDetail productDetail = new ProductDetail();
				productDetail.setProductDetailId(rs.getInt("product_detail_id"));
				productDetail.setProductSizeName(rs.getString("product_size_name"));
				productDetail.setProductQuantity(rs.getInt("product_quantity"));
				
				ProductImage productImage = new ProductImage();
				productImage.setProductImageId(rs.getInt("product_image_id"));
				productImage.setImageURL(rs.getString("image_url"));
				
				//연결
				product.getProductDetails().add(productDetail);
				product.getProductImages().add(productImage);	
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
}
