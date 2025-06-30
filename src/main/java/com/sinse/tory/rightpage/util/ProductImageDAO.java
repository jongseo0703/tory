package com.sinse.tory.rightpage.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductImage;

public class ProductImageDAO {
	DBManager dbManager = DBManager.getInstance();
	public ProductImage selectAll(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		ProductImage productImage = new ProductImage();
		sql.append("select * from ProductImage where product_id =?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Product product = new Product();
				productImage.setProductImageId(rs.getInt("product_image_id"));
				productImage.setImageURL(rs.getString("image_url"));
				productImage.setProduct(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}
		
		return productImage;
	}
	
	/**
	 * ProductImage 저장
	 * @param productImage 저장할 ProductImage 객체
	 */
	public void insert(ProductImage productImage) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		String sql = "INSERT INTO ProductImage (image_url, product_id) VALUES (?, ?)";
		
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, productImage.getImageURL());
			pstmt.setInt(2, productImage.getProduct().getProductId());
			int result = pstmt.executeUpdate();
			con.setAutoCommit(true);
			if (result > 0) {
				System.out.println("✅ ProductImage 저장 성공");
			} else {
				System.out.println("❌ ProductImage 저장 실패");
			}
			
		} catch (SQLException e) {
			System.err.println("❌ ProductImage 저장 중 오류: " + e.getMessage());
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, null);
		}
	}
}
