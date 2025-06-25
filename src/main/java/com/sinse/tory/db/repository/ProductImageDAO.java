package com.sinse.tory.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sinse.tory.db.common.util.DBManager;

public class ProductImageDAO {
    // 싱글톤 패턴인 DBManager 클래스를 통해 하나의 인스턴스인 dbManger생성
    DBManager dbManager = DBManager.getInstance();

    /**
     * 이미지 경로 DB에 저장
     */
    public void insertImagePath(String imagePath, int productId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        con = dbManager.getConnection();
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ProductImage (image_url, product_id) VALUES (?, ?)");

        try {
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
            pstmt.executeUpdate();
            System.out.println("✅ 이미지 경로 저장 완료: " + imagePath);
        } catch (SQLException e) {
            System.out.println("이미지 경로 저장 실패");
            e.printStackTrace();
        }

    }
}