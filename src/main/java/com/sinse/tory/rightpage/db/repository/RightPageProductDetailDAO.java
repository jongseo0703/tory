package com.sinse.tory.rightpage.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;

public final class RightPageProductDetailDAO
{
	/**
	 * 
	 * @param subCategoryToGetID
	 * @return id, name만 가지고 있는 product
	 */
	public static List<ProductDetail> selectProductDetail(SubCategory subCategoryToGetID) {
		DBManager dbManager = DBManager.getInstance();
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null; //sql 문을 실행하기 위한 PreparedStatement 객체 초기화
		ResultSet rs = null; //select 문을 위한 표반환 객체인 ResultSet 객체 초기화
		List<ProductDetail> list = new ArrayList<>(); //Product 타입의 객체를 담은 list 생성
		
		//db 접속하기 위한 dbManager인스턴스의 getConnection 메서드 호출.
		con = dbManager.getConnection();
		
		//String 으로 sql 을 만들면 객체의 낭비가 생기기때문에 StringBuilder 객체 이용.
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pd.product_detail_id, pd.product_size_name, pd.product_quantity, ");
	    sql.append("p.product_id, p.product_name, p.product_price, p.product_description, ");
	    sql.append("l.location_id, l.location_name, ");
	    sql.append("b.brand_id, b.brand_name ");
	    sql.append("FROM ProductDetail pd ");
	    sql.append("JOIN Product p ON pd.product_id = p.product_id ");
	    sql.append("JOIN Location l ON p.location_id = l.location_id ");
	    sql.append("JOIN Brand b ON l.brand_id = b.brand_id ");
	    sql.append("JOIN SubCategory s ON b.sub_category_id = s.sub_category_id ");
	    sql.append("WHERE s.sub_category_id = ?");
	    
		try {
			//만든 쿼리를 실행.
			pstmt = con.prepareStatement(sql.toString());
			//첫번째 바인딩 변수 = select 메서드의 매개변수인 product_id(즉, 사용자가 선택한 상자)
			pstmt.setInt(1, subCategoryToGetID.getSubCategoryId());
			rs = pstmt.executeQuery();
			
			// rs 인스턴스(테이블)에서 한 줄씩 데이터를 꺼내서 product 인스턴스에 넣어서 list 에저장
			while (rs.next()) {
				 // ProductDetail
	            ProductDetail pd = new ProductDetail();
	            pd.setProductDetailId(rs.getInt("product_detail_id"));
	            pd.setProductSizeName(rs.getString("product_size_name"));
	            pd.setProductQuantity(rs.getInt("product_quantity"));

	            // Product
	            Product p = new Product();
	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));
	            p.setProductPrice(rs.getInt("product_price"));
	            p.setDescription(rs.getString("product_description"));

	            // Location
	            Location loc = new Location();
	            loc.setLocationId(rs.getInt("location_id"));
	            loc.setLocationName(rs.getString("location_name"));

	            // Brand
	            Brand brand = new Brand();
	            brand.setBrandId(rs.getInt("brand_id"));
	            brand.setBrandName(rs.getString("brand_name"));
	            brand.setSubCategory(subCategoryToGetID);

	            loc.setBrand(brand);
	            p.setLocation(loc);
	            pd.setProduct(p);

	            list.add(pd);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
	public static void insert(ProductDetail productDetail) {
		DBManager dbManager = DBManager.getInstance();
		Connection con = null; //커넥션 객체 초기화
		PreparedStatement pstmt = null;
		con = dbManager.getConnection();
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		
		try {
	        con.setAutoCommit(false); // 트랜잭션 시작

	        Product product = productDetail.getProduct();
	        Location location = product.getLocation();

	        if (location == null || location.getLocationId() == 0) {
	            throw new IllegalArgumentException("Product에 유효한 Location이 필요합니다.");
	        }

	        // 1. insert into product
	        String productSql = "INSERT INTO Product (product_name, product_price, product_description, location_id) VALUES (?, ?, ?, ?)";
	        pstmt = con.prepareStatement(productSql, Statement.RETURN_GENERATED_KEYS);
	        pstmt.setString(1, product.getProductName());
	        pstmt.setInt(2, product.getProductPrice());
	        pstmt.setString(3, product.getDescription());
	        pstmt.setInt(4, location.getLocationId());
	        pstmt.executeUpdate();

	        rs = pstmt.getGeneratedKeys();
	        if (rs.next()) {
	            int productId = rs.getInt(1);
	            product.setProductId(productId); // 자바 객체에도 반영
	        }
	        else {
	            throw new SQLException("Product insert 실패: 생성된 key 없음");
	        }

	        pstmt.close();

	        // 2. insert into productdetail
	        String detailSql = "INSERT INTO ProductDetail (product_size_name, product_quantity, product_id) VALUES (?, ?, ?)";
	        pstmt = con.prepareStatement(detailSql);
	        pstmt.setString(1, productDetail.getProductSizeName());
	        pstmt.setInt(2, productDetail.getProductQuantity());
	        pstmt.setInt(3, product.getProductId());
	        pstmt.executeUpdate();

	        con.commit(); // 트랜잭션 성공
	        System.out.println("✅ Product와 ProductDetail 저장 성공!");
	        System.out.println("   - Product ID: " + product.getProductId());
	        System.out.println("   - Product Name: " + product.getProductName());

	    }
		catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (con != null) {
	            	con.rollback();
	            }
	        }
	        catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	    }
		finally {
	        dbManager.release(pstmt, rs);
	    }
	}
	public static List<ProductDetail> selectProductDetailsByProductName(String productName) {
	    DBManager dbManager = DBManager.getInstance();
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<ProductDetail> list = new ArrayList<>();

	    // SQL 쿼리 작성: 제품 이름을 기준으로 조인
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT pd.product_detail_id, pd.product_size_name, pd.product_quantity, ");
	    sql.append("p.product_id, p.product_name, p.product_price, p.product_description, ");
	    sql.append("l.location_id, l.location_name, ");
	    sql.append("b.brand_id, b.brand_name, ");
	    sql.append("s.sub_category_id, s.sub_category_name ");
	    sql.append("FROM ProductDetail pd ");
	    sql.append("JOIN Product p ON pd.product_id = p.product_id ");
	    sql.append("JOIN Location l ON p.location_id = l.location_id ");
	    sql.append("JOIN Brand b ON l.brand_id = b.brand_id ");
	    sql.append("JOIN SubCategory s ON b.sub_category_id = s.sub_category_id ");
	    sql.append("WHERE p.product_name = ?");

	    try {
	        con = dbManager.getConnection();
	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setString(1, productName);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            // ProductDetail 객체 생성
	            ProductDetail pd = new ProductDetail();
	            pd.setProductDetailId(rs.getInt("product_detail_id"));
	            pd.setProductSizeName(rs.getString("product_size_name"));
	            pd.setProductQuantity(rs.getInt("product_quantity"));

	            // Product 객체 생성 및 설정
	            Product p = new Product();
	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));
	            p.setProductPrice(rs.getInt("product_price"));
	            p.setDescription(rs.getString("product_description"));

	            // Location 객체 생성 및 설정
	            Location l = new Location();
	            l.setLocationId(rs.getInt("location_id"));
	            l.setLocationName(rs.getString("location_name"));

	            // Brand 객체 생성 및 설정
	            Brand b = new Brand();
	            b.setBrandId(rs.getInt("brand_id"));
	            b.setBrandName(rs.getString("brand_name"));

	            // SubCategory 객체 생성 및 설정
	            SubCategory s = new SubCategory();
	            s.setSubCategoryId(rs.getInt("sub_category_id"));
	            s.setSubCategoryName(rs.getString("sub_category_name"));

	            // 계층 관계 연결: Brand → SubCategory, Location → Brand, Product → Location, ProductDetail → Product
	            b.setSubCategory(s);
	            l.setBrand(b);
	            p.setLocation(l);
	            pd.setProduct(p);

	            list.add(pd);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }
	    
	    return list;
	}
	public static ProductDetail select(Product product, String sizeName) {
		DBManager dbManager = DBManager.getInstance();
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ProductDetail pd = null;

	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT ");
	    sql.append("pd.product_detail_id, pd.product_size_name, pd.product_quantity, ");
	    sql.append("p.product_id, p.product_name, p.product_price, p.product_description, ");
	    sql.append("l.location_id, l.location_name, ");
	    sql.append("b.brand_id, b.brand_name, ");
	    sql.append("s.sub_category_id, s.sub_category_name, ");
	    sql.append("t.top_category_id, t.top_category_name ");
	    sql.append("FROM ProductDetail pd ");
	    sql.append("JOIN Product p ON pd.product_id = p.product_id ");
	    sql.append("JOIN Location l ON p.location_id = l.location_id ");
	    sql.append("JOIN Brand b ON l.brand_id = b.brand_id ");
	    sql.append("JOIN SubCategory s ON b.sub_category_id = s.sub_category_id ");
	    sql.append("JOIN TopCategory t ON s.top_category_id = t.top_category_id ");
	    sql.append("WHERE p.product_name = ? AND pd.product_size_name = ?;");

	    try {
	        con = dbManager.getConnection();
	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setString(1, product.getProductName());
	        pstmt.setString(2, sizeName);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            pd = new ProductDetail();
	            pd.setProductDetailId(rs.getInt("product_detail_id"));
	            pd.setProductSizeName(rs.getString("product_size_name"));
	            pd.setProductQuantity(rs.getInt("product_quantity"));

	            Product p = new Product();
	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));
	            p.setProductPrice(rs.getInt("product_price"));
	            p.setDescription(rs.getString("product_description"));

	            Location l = new Location();
	            l.setLocationId(rs.getInt("location_id"));
	            l.setLocationName(rs.getString("location_name"));

	            Brand b = new Brand();
	            b.setBrandId(rs.getInt("brand_id"));
	            b.setBrandName(rs.getString("brand_name"));

	            SubCategory s = new SubCategory();
	            s.setSubCategoryId(rs.getInt("sub_category_id"));
	            s.setSubCategoryName(rs.getString("sub_category_name"));

	            TopCategory t = new TopCategory();
	            t.setTopCategoryId(rs.getInt("top_category_id"));
	            t.setTopCategoryName(rs.getString("top_category_name"));

	            // 계층 관계 설정
	            s.setTopCategory(t);
	            b.setSubCategory(s);
	            l.setBrand(b);
	            p.setLocation(l);
	            pd.setProduct(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return pd;
	}
}
