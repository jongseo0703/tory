package com.sinse.tory.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sinse.tory.db.common.util.DBManager;
import com.sinse.tory.db.model.InventoryLog;
import com.sinse.tory.db.model.InventoryLog.ChangeType;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;

public class InventoryLogDAO {
    
    private DBManager dbManager = DBManager.getInstance();
    
    /**
     * 특정 ProductDetail의 입출고 내역 조회
     * @param productDetail 조회할 상품 상세 정보
     * @return 입출고 내역 리스트
     */
    public List<InventoryLog> selectByProductDetail(ProductDetail productDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<InventoryLog> logList = new ArrayList<>();
        
        try {
            con = dbManager.getConnection();
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT inventory_log_id, change_type, quantity, changed_at, product_detail_id ");
            sql.append("FROM InventoryLog ");
            sql.append("WHERE product_detail_id = ? ");
            sql.append("ORDER BY changed_at DESC");
            
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, productDetail.getProductDetailId());
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                InventoryLog log = new InventoryLog();
                log.setInventoryLogId(rs.getInt("inventory_log_id"));
                log.setChangeType(ChangeType.valueOf(rs.getString("change_type")));
                log.setQuantity(rs.getInt("quantity"));
                log.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime().toLocalDate());
                log.setProductDetail(productDetail);
                
                logList.add(log);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return logList;
    }
    
    /**
     * 전체 입출고 내역 조회 (모든 상품)
     * @return 전체 입출고 내역 리스트
     */
    public List<InventoryLog> selectAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<InventoryLog> logList = new ArrayList<>();
        
        try {
            con = dbManager.getConnection();
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT il.inventory_log_id, il.change_type, il.quantity, il.changed_at, ");
            sql.append("       il.product_detail_id, pd.product_size_name, pd.product_quantity, ");
            sql.append("       p.product_id, p.product_name, p.product_price ");
            sql.append("FROM InventoryLog il ");
            sql.append("INNER JOIN ProductDetail pd ON il.product_detail_id = pd.product_detail_id ");
            sql.append("INNER JOIN Product p ON pd.product_id = p.product_id ");
            sql.append("ORDER BY il.changed_at DESC");
            
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Product 객체 생성
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setProductPrice(rs.getInt("product_price"));
                
                // ProductDetail 객체 생성
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(rs.getInt("product_detail_id"));
                productDetail.setProductSizeName(rs.getString("product_size_name"));
                productDetail.setProductQuantity(rs.getInt("product_quantity"));
                productDetail.setProduct(product);
                
                // InventoryLog 객체 생성
                InventoryLog log = new InventoryLog();
                log.setInventoryLogId(rs.getInt("inventory_log_id"));
                log.setChangeType(ChangeType.valueOf(rs.getString("change_type")));
                log.setQuantity(rs.getInt("quantity"));
                log.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime().toLocalDate());
                log.setProductDetail(productDetail);
                
                logList.add(log);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return logList;
    }
    
    /**
     * 입출고 내역 추가
     * @param inventoryLog 추가할 입출고 내역
     * @return 성공 여부
     */
    public boolean insert(InventoryLog inventoryLog) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            con = dbManager.getConnection();
            
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO InventoryLog (change_type, quantity, product_detail_id) ");
            sql.append("VALUES (?, ?, ?)");
            
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, inventoryLog.getChangeType().name());
            pstmt.setInt(2, inventoryLog.getQuantity());
            pstmt.setInt(3, inventoryLog.getProductDetail().getProductDetailId());
            
            int rowsAffected = pstmt.executeUpdate();
            result = rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, null);
        }
        
        return result;
    }
    
    /**
     * 특정 기간의 입출고 내역 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 입출고 내역 리스트
     */
    public List<InventoryLog> selectByDateRange(Timestamp startDate, Timestamp endDate) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<InventoryLog> logList = new ArrayList<>();
        
        try {
            con = dbManager.getConnection();
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT il.inventory_log_id, il.change_type, il.quantity, il.changed_at, ");
            sql.append("       il.product_detail_id, pd.product_size_name, pd.product_quantity, ");
            sql.append("       p.product_id, p.product_name, p.product_price ");
            sql.append("FROM InventoryLog il ");
            sql.append("INNER JOIN ProductDetail pd ON il.product_detail_id = pd.product_detail_id ");
            sql.append("INNER JOIN Product p ON pd.product_id = p.product_id ");
            sql.append("WHERE il.changed_at BETWEEN ? AND ? ");
            sql.append("ORDER BY il.changed_at DESC");
            
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Product 객체 생성
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setProductPrice(rs.getInt("product_price"));
                
                // ProductDetail 객체 생성
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(rs.getInt("product_detail_id"));
                productDetail.setProductSizeName(rs.getString("product_size_name"));
                productDetail.setProductQuantity(rs.getInt("product_quantity"));
                productDetail.setProduct(product);
                
                // InventoryLog 객체 생성
                InventoryLog log = new InventoryLog();
                log.setInventoryLogId(rs.getInt("inventory_log_id"));
                log.setChangeType(ChangeType.valueOf(rs.getString("change_type")));
                log.setQuantity(rs.getInt("quantity"));
                log.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime().toLocalDate());
                log.setProductDetail(productDetail);
                
                logList.add(log);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return logList;
    }
}
