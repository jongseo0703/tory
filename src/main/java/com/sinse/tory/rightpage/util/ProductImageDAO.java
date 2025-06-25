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
		sql.append("select * from productimage where product_id =?");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}
		
		return productImage;
	}
}
