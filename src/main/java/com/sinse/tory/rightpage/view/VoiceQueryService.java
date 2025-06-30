package com.sinse.tory.rightpage.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sinse.tory.db.common.util.DBManager;

/**
 * 음성 명령을 기반으로 데이터베이스를 조회하는 서비스 클래스
 */
public class VoiceQueryService {
    
    private DBManager dbManager;
    private SimpleDateFormat dateFormat;
    
    public VoiceQueryService() {
        this.dbManager = DBManager.getInstance();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    /**
     * 음성 텍스트를 분석하여 적절한 DB 조회 수행
     */
    public String processVoiceQuery(String voiceText) {
        String lowerText = voiceText.toLowerCase();
        StringBuilder result = new StringBuilder();
        
        result.append("음성 명령 분석 결과:\n\n");
        result.append("인식된 음성: ").append(voiceText).append("\n\n");
        
        try {
            boolean hasResult = false;
            
            // 전체 재고 조회
            if (lowerText.contains("재고") || lowerText.contains("수량") || lowerText.contains("개수")) {
                result.append(getInventoryStatus());
                hasResult = true;
            }
            
            // 카테고리별 조회
            if (lowerText.contains("카테고리") || lowerText.contains("분류") || 
                lowerText.contains("상의") || lowerText.contains("하의") || 
                lowerText.contains("신발") || lowerText.contains("가방")) {
                result.append(getCategoryInfo(lowerText));
                hasResult = true;
            }
            
            // 입고 내역 조회
            if (lowerText.contains("입고") || lowerText.contains("들어온")) {
                result.append(getInboundHistory());
                hasResult = true;
            }
            
            // 출고 내역 조회
            if (lowerText.contains("출고") || lowerText.contains("나간")) {
                result.append(getOutboundHistory());
                hasResult = true;
            }
            
            // 브랜드별 조회
            if (lowerText.contains("브랜드") || lowerText.contains("나이키") || 
                lowerText.contains("아디다스") || lowerText.contains("스투시")) {
                result.append(getBrandInfo(lowerText));
                hasResult = true;
            }
            
            // 위치 정보 조회
            if (lowerText.contains("위치") || lowerText.contains("어디")) {
                result.append(getLocationInfo());
                hasResult = true;
            }
            
            // 기본 응답 (키워드가 없을 경우)
            if (!hasResult) {
                result.append("💡 사용 가능한 명령어:\n");
                result.append("• '재고 조회' - 전체 재고 현황\n");
                result.append("• '카테고리 조회' - 카테고리별 상품 현황\n");
                result.append("• '입고 내역' - 최근 입고 현황\n");
                result.append("• '출고 내역' - 최근 출고 현황\n");
                result.append("• '브랜드 조회' - 브랜드별 상품 현황\n");
                result.append("• '위치 확인' - 창고 위치 정보\n\n");
            }
            
        } catch (Exception e) {
            result.append("❌ 데이터베이스 조회 중 오류 발생: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }
    
    /**
     * 전체 재고 현황 조회
     */
    private String getInventoryStatus() {
        StringBuilder result = new StringBuilder();
        result.append("전체 재고 현황:\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            // 전체 상품 수와 총 재고량 조회
            String sql = "SELECT COUNT(DISTINCT p.product_id) as total_products, " +
                        "SUM(pd.product_quantity) as total_quantity " +
                        "FROM Product p " +
                        "JOIN ProductDetail pd ON p.product_id = pd.product_id";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int totalProducts = rs.getInt("total_products");
                int totalQuantity = rs.getInt("total_quantity");
                
                result.append("• 등록된 상품 종류: ").append(totalProducts).append("개\n");
                result.append("• 전체 재고 수량: ").append(totalQuantity).append("개\n");
            }
            
            dbManager.release(pstmt, rs);
            
            // 재고 부족 상품 조회 (재고 5개 이하)
            sql = "SELECT p.product_name, pd.product_size_name, pd.product_quantity " +
                  "FROM Product p " +
                  "JOIN ProductDetail pd ON p.product_id = pd.product_id " +
                  "WHERE pd.product_quantity <= 5 " +
                  "ORDER BY pd.product_quantity ASC " +
                  "LIMIT 5";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            result.append("• 재고 부족 상품 (5개 이하):\n");
            boolean hasLowStock = false;
            while (rs.next()) {
                hasLowStock = true;
                result.append("  - ").append(rs.getString("product_name"))
                      .append(" (").append(rs.getString("product_size_name")).append("): ")
                      .append(rs.getInt("product_quantity")).append("개\n");
            }
            
            if (!hasLowStock) {
                result.append("  - 재고 부족 상품 없음\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 재고 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
    
    /**
     * 카테고리별 정보 조회
     */
    private String getCategoryInfo(String lowerText) {
        StringBuilder result = new StringBuilder();
        result.append("🏷️ 카테고리별 상품 현황:\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            String sql = "SELECT tc.top_category_name, " +
                        "COUNT(DISTINCT p.product_id) as product_count, " +
                        "SUM(pd.product_quantity) as total_quantity " +
                        "FROM TopCategory tc " +
                        "JOIN SubCategory sc ON tc.top_category_id = sc.top_category_id " +
                        "JOIN Brand b ON sc.sub_category_id = b.sub_category_id " +
                        "JOIN Location l ON b.brand_id = l.brand_id " +
                        "JOIN Product p ON l.location_id = p.location_id " +
                        "JOIN ProductDetail pd ON p.product_id = pd.product_id " +
                        "GROUP BY tc.top_category_id, tc.top_category_name " +
                        "ORDER BY total_quantity DESC";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String categoryName = rs.getString("top_category_name");
                int productCount = rs.getInt("product_count");
                int totalQuantity = rs.getInt("total_quantity");
                
                result.append("• ").append(categoryName).append(": ")
                      .append(productCount).append("종류, ")
                      .append(totalQuantity).append("개\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 카테고리 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
    
    /**
     * 최근 입고 내역 조회
     */
    private String getInboundHistory() {
        StringBuilder result = new StringBuilder();
        result.append("최근 입고 내역 (최근 5건):\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            String sql = "SELECT p.product_name, pd.product_size_name, " +
                        "il.quantity, il.changed_at " +
                        "FROM InventoryLog il " +
                        "JOIN ProductDetail pd ON il.product_detail_id = pd.product_detail_id " +
                        "JOIN Product p ON pd.product_id = p.product_id " +
                        "WHERE il.change_type = 'IN' " +
                        "ORDER BY il.changed_at DESC " +
                        "LIMIT 5";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String productName = rs.getString("product_name");
                String sizeName = rs.getString("product_size_name");
                int quantity = rs.getInt("quantity");
                String date = dateFormat.format(rs.getTimestamp("changed_at"));
                
                result.append("• ").append(productName)
                      .append(" (").append(sizeName).append("): ")
                      .append(quantity).append("개 (").append(date).append(")\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 입고 내역 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
    
    /**
     * 최근 출고 내역 조회
     */
    private String getOutboundHistory() {
        StringBuilder result = new StringBuilder();
        result.append("최근 출고 내역 (최근 5건):\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            String sql = "SELECT p.product_name, pd.product_size_name, " +
                        "il.quantity, il.changed_at " +
                        "FROM InventoryLog il " +
                        "JOIN ProductDetail pd ON il.product_detail_id = pd.product_detail_id " +
                        "JOIN Product p ON pd.product_id = p.product_id " +
                        "WHERE il.change_type = 'OUT' " +
                        "ORDER BY il.changed_at DESC " +
                        "LIMIT 5";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String productName = rs.getString("product_name");
                String sizeName = rs.getString("product_size_name");
                int quantity = rs.getInt("quantity");
                String date = dateFormat.format(rs.getTimestamp("changed_at"));
                
                result.append("• ").append(productName)
                      .append(" (").append(sizeName).append("): ")
                      .append(quantity).append("개 (").append(date).append(")\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 출고 내역 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
    
    /**
     * 브랜드별 정보 조회
     */
    private String getBrandInfo(String lowerText) {
        StringBuilder result = new StringBuilder();
        result.append("🏪 브랜드별 상품 현황:\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            String sql = "SELECT b.brand_name, " +
                        "COUNT(DISTINCT p.product_id) as product_count, " +
                        "SUM(pd.product_quantity) as total_quantity " +
                        "FROM Brand b " +
                        "JOIN Location l ON b.brand_id = l.brand_id " +
                        "JOIN Product p ON l.location_id = p.location_id " +
                        "JOIN ProductDetail pd ON p.product_id = pd.product_id " +
                        "GROUP BY b.brand_id, b.brand_name " +
                        "ORDER BY total_quantity DESC " +
                        "LIMIT 5";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String brandName = rs.getString("brand_name");
                int productCount = rs.getInt("product_count");
                int totalQuantity = rs.getInt("total_quantity");
                
                result.append("• ").append(brandName).append(": ")
                      .append(productCount).append("종류, ")
                      .append(totalQuantity).append("개\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 브랜드 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
    
    /**
     * 창고 위치 정보 조회
     */
    private String getLocationInfo() {
        StringBuilder result = new StringBuilder();
        result.append("📍 창고 위치 정보:\n");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = dbManager.getConnection();
            
            String sql = "SELECT tc.top_category_name, l.location_name, " +
                        "COUNT(DISTINCT p.product_id) as product_count " +
                        "FROM TopCategory tc " +
                        "JOIN SubCategory sc ON tc.top_category_id = sc.top_category_id " +
                        "JOIN Brand b ON sc.sub_category_id = b.sub_category_id " +
                        "JOIN Location l ON b.brand_id = l.brand_id " +
                        "JOIN Product p ON l.location_id = p.location_id " +
                        "GROUP BY tc.top_category_name, l.location_name " +
                        "ORDER BY tc.top_category_name, l.location_name";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            String currentCategory = "";
            while (rs.next()) {
                String categoryName = rs.getString("top_category_name");
                String locationName = rs.getString("location_name");
                int productCount = rs.getInt("product_count");
                
                if (!categoryName.equals(currentCategory)) {
                    if (!currentCategory.isEmpty()) {
                        result.append("\n");
                    }
                    result.append("• ").append(categoryName).append(":\n");
                    currentCategory = categoryName;
                }
                
                result.append("  - 위치 ").append(locationName).append(": ")
                      .append(productCount).append("종류\n");
            }
            
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("❌ 위치 정보 조회 오류: ").append(e.getMessage()).append("\n");
        } finally {
            dbManager.release(pstmt, rs);
        }
        
        return result.toString();
    }
} 