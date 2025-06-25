package com.sinse.tory.rightpage.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.sinse.tory.db.model.InventoryLog;
import com.sinse.tory.db.model.Product;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.repository.InventoryLogDAO;
import com.sinse.tory.rightpage.util.Pages;

/**
 * 입출고 내역 조회 페이지
 */
public class InventoryLogHistoryPage extends Pages {
    
    private JPanel headerPanel;
    private JPanel filterPanel;
    private JPanel contentPanel;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton searchButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel infoLabel;
    
    // 필터링 컴포넌트들
    private JComboBox<String> changeTypeFilter;
    private JTextField productNameFilter;
    private JTextField startDateFilter;
    private JTextField endDateFilter;
    
    private InventoryLogDAO inventoryLogDAO;
    private ProductDetail selectedProductDetail;
    private String selectedProductName;
    
    // 색상 상수
    private static final Color PRIMARY_COLOR = new Color(52, 144, 220);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 149, 0);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    
    public InventoryLogHistoryPage(Object testmain) {
        super(testmain);
        this.inventoryLogDAO = new InventoryLogDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        styleComponents();
        
        // 페이지 로드 시 전체 입출고 내역 자동 로드
        loadAllInventoryHistory();
    }
    
    /**
     * 컴포넌트 초기화
     */
    private void initializeComponents() {
        // 헤더 패널
        headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // 뒤로가기 버튼
        backButton = createStyledButton("← 뒤로", LIGHT_GRAY, new Color(70, 70, 70));
        
        // 제목 라벨
        titleLabel = new JLabel("📋 입출고 내역 조회");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // 필터 패널
        filterPanel = new JPanel();
        filterPanel.setBackground(LIGHT_GRAY);
        filterPanel.setPreferredSize(new Dimension(0, 80));
        
        // 필터링 컴포넌트들
        changeTypeFilter = new JComboBox<>(new String[]{"전체", "📥 입고", "📤 출고"});
        changeTypeFilter.setPreferredSize(new Dimension(100, 30));
        
        productNameFilter = new JTextField();
        productNameFilter.setPreferredSize(new Dimension(150, 30));
        addPlaceholder(productNameFilter, "상품명 검색...");
        
        startDateFilter = new JTextField();
        startDateFilter.setPreferredSize(new Dimension(120, 30));
        addPlaceholder(startDateFilter, "YYYY-MM-DD");
        
        endDateFilter = new JTextField();
        endDateFilter.setPreferredSize(new Dimension(120, 30));
        addPlaceholder(endDateFilter, "YYYY-MM-DD");
        
        searchButton = createStyledButton("🔍 검색", PRIMARY_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(80, 30));
        
        resetButton = createStyledButton("🔄 초기화", WARNING_COLOR, Color.WHITE);
        resetButton.setPreferredSize(new Dimension(80, 30));
        
        // 컨텐츠 패널
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());
        
        // 테이블 모델 설정
        String[] columnNames = {"날짜", "구분", "수량", "상품명", "사이즈", "비고"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 편집 불가
            }
        };
        
        // 테이블 생성
        logTable = new JTable(tableModel);
        logTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        logTable.setRowHeight(35);
        logTable.setBackground(Color.WHITE);
        logTable.setSelectionBackground(new Color(220, 240, 255));
        logTable.setSelectionForeground(Color.BLACK);
        logTable.setGridColor(new Color(240, 240, 240));
        
        // 정보 라벨
        infoLabel = new JLabel("전체 입출고 내역을 불러오는 중...");
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(108, 117, 125));
    }
    
    /**
     * 레이아웃 설정
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // 헤더 패널 레이아웃
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel headerLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerLeftPanel.setBackground(PRIMARY_COLOR);
        headerLeftPanel.add(backButton);
        
        JPanel headerCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerCenterPanel.setBackground(PRIMARY_COLOR);
        headerCenterPanel.add(titleLabel);
        
        headerPanel.add(headerLeftPanel, BorderLayout.WEST);
        headerPanel.add(headerCenterPanel, BorderLayout.CENTER);
        
        // 필터 패널 레이아웃
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 25));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        
        filterPanel.add(new JLabel("구분:"));
        filterPanel.add(changeTypeFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        
        filterPanel.add(new JLabel("상품명:"));
        filterPanel.add(productNameFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        
        filterPanel.add(new JLabel("시작일:"));
        filterPanel.add(startDateFilter);
        filterPanel.add(new JLabel("종료일:"));
        filterPanel.add(endDateFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        
        filterPanel.add(searchButton);
        filterPanel.add(resetButton);
        
        // 컨텐츠 패널 레이아웃
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 테이블을 스크롤 패널에 추가
        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // 정보 패널
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(LIGHT_GRAY);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        infoPanel.add(infoLabel);
        
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 메인 패널에 추가
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFilters();
            }
        });
        
        // 엔터키로 검색
        productNameFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }
    
    /**
     * 컴포넌트 스타일링
     */
    private void styleComponents() {
        // 테이블 헤더 스타일링
        JTableHeader header = logTable.getTableHeader();
        header.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        header.setBackground(LIGHT_GRAY);
        header.setForeground(new Color(70, 70, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(0, 40));
        
        // 테이블 컬럼 너비 설정
        logTable.getColumnModel().getColumn(0).setPreferredWidth(150); // 날짜
        logTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // 구분
        logTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 수량
        logTable.getColumnModel().getColumn(3).setPreferredWidth(200); // 상품명
        logTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // 사이즈
        logTable.getColumnModel().getColumn(5).setPreferredWidth(150); // 비고
    }
    

    
    /**
     * 전체 입출고 내역 로드 (모든 상품)
     */
    public void loadAllInventoryHistory() {
        titleLabel.setText("📋 전체 입출고 내역");
        
        try {
            // 전체 입출고 내역 조회
            List<InventoryLog> logList = inventoryLogDAO.selectAll();
            displayInventoryLogs(logList, "전체");
            
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.showAlert(this, "오류", "❌ 입출고 내역을 조회하는 중 오류가 발생했습니다.\n" + e.getMessage());
            infoLabel.setText("입출고 내역 조회 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 검색 수행
     */
    private void performSearch() {
        try {
            List<InventoryLog> allLogs = inventoryLogDAO.selectAll();
            List<InventoryLog> filteredLogs = new java.util.ArrayList<>();
            
            String changeTypeSelected = (String) changeTypeFilter.getSelectedItem();
            String productNameText = getTextFieldValue(productNameFilter, "상품명 검색...");
            String startDateText = getTextFieldValue(startDateFilter, "YYYY-MM-DD");
            String endDateText = getTextFieldValue(endDateFilter, "YYYY-MM-DD");
            
            for (InventoryLog log : allLogs) {
                boolean matches = true;
                
                // 구분 필터
                if (!changeTypeSelected.equals("전체")) {
                    String logType = log.getChangeType().name().equals("IN") ? "📥 입고" : "📤 출고";
                    if (!logType.equals(changeTypeSelected)) {
                        matches = false;
                    }
                }
                
                // 상품명 필터
                if (!productNameText.isEmpty()) {
                    String productName = log.getProductDetail().getProduct().getProductName().toLowerCase();
                    if (!productName.contains(productNameText.toLowerCase())) {
                        matches = false;
                    }
                }
                
                // 날짜 필터
                if (!startDateText.isEmpty() || !endDateText.isEmpty()) {
                    LocalDate logDate = log.getChangedAt();
                    
                    if (!startDateText.isEmpty()) {
                        try {
                            LocalDate startDate = LocalDate.parse(startDateText);
                            if (logDate.isBefore(startDate)) {
                                matches = false;
                            }
                        } catch (Exception e) {
                            // 잘못된 날짜 형식은 무시
                        }
                    }
                    
                    if (!endDateText.isEmpty()) {
                        try {
                            LocalDate endDate = LocalDate.parse(endDateText);
                            if (logDate.isAfter(endDate)) {
                                matches = false;
                            }
                        } catch (Exception e) {
                            // 잘못된 날짜 형식은 무시
                        }
                    }
                }
                
                if (matches) {
                    filteredLogs.add(log);
                }
            }
            
            displayInventoryLogs(filteredLogs, "검색 결과");
            
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.showAlert(this, "오류", "❌ 검색 중 오류가 발생했습니다.\n" + e.getMessage());
        }
    }
    
    /**
     * 필터 초기화
     */
    private void resetFilters() {
        changeTypeFilter.setSelectedIndex(0);
        
        productNameFilter.setText("상품명 검색...");
        productNameFilter.setForeground(new Color(150, 150, 150));
        
        startDateFilter.setText("YYYY-MM-DD");
        startDateFilter.setForeground(new Color(150, 150, 150));
        
        endDateFilter.setText("YYYY-MM-DD");
        endDateFilter.setForeground(new Color(150, 150, 150));
        
        // 전체 내역 다시 로드
        loadAllInventoryHistory();
    }
    
    /**
     * 텍스트 필드 값 가져오기 (플레이스홀더 제외)
     */
    private String getTextFieldValue(JTextField textField, String placeholder) {
        String text = textField.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }
    
    /**
     * 입출고 내역 테이블에 표시
     */
    private void displayInventoryLogs(List<InventoryLog> logList, String description) {
        // 테이블 데이터 클리어
        tableModel.setRowCount(0);
        
        // 데이터 추가
        for (InventoryLog log : logList) {
            ProductDetail detail = log.getProductDetail();
            Product product = detail.getProduct();
            
            Object[] rowData = {
                log.getChangedAt().toString(), // 날짜
                log.getChangeType().name().equals("IN") ? "📥 입고" : "📤 출고", // 구분
                log.getQuantity() + "개", // 수량
                product.getProductName(), // 상품명
                detail.getProductSizeName(), // 사이즈
                log.getChangeType().name().equals("IN") ? "재고 증가" : "재고 감소" // 비고
            };
            tableModel.addRow(rowData);
        }
        
        // 정보 라벨 업데이트
        infoLabel.setText(description + ": 총 " + logList.size() + "건의 입출고 내역이 있습니다.");
        
        System.out.println("📋 입출고 내역 로드: " + logList.size() + "건");
    }
    
    /**
     * 뒤로가기
     */
    private void goBack() {
        if (testmain != null) {
            try {
                testmain.getClass().getMethod("showProductShipPage").invoke(testmain);
                System.out.println("✅ ProductShip으로 돌아감");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ 뒤로가기 실패: " + e.getMessage());
            }
        }
    }
    
    /**
     * 스타일된 버튼 생성
     */
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 호버 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * 텍스트 필드에 플레이스홀더 추가
     */
    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(new Color(150, 150, 150));
        
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(150, 150, 150));
                }
            }
        });
    }
} 