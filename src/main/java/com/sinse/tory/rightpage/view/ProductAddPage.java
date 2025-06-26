package com.sinse.tory.rightpage.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.sinse.tory.db.model.*;
import com.sinse.tory.db.repository.*;
import com.sinse.tory.rightpage.db.repository.*;
import com.sinse.tory.rightpage.util.Pages;
import com.sinse.tory.leftpage.view.InventoryUI;

/**
 * ProductAddPage
 * - 깔끔하고 현대적인 상품 추가 페이지
 * - 음성 명령을 통한 자동 입력 지원
 * - 실시간 미리보기 및 검증
 */
public class ProductAddPage extends Pages {

    // UI 컴포넌트들
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel imagePanel;
    private JPanel formPanel;

    // 헤더 버튼들
    private JButton backButton;
    private JButton saveButton;
    private JButton clearButton;

    // 이미지 관련
    private JLabel imageLabel;
    private JLabel placeholderTextLabel;
    private JButton imageUploadButton;
    private String selectedImagePath = null;

    // 폼 입력 필드들
    private JComboBox<TopCategory> topCategoryCombo;
    private JComboBox<SubCategory> subCategoryCombo;
    private JTextField productNameField;
    private JComboBox<Brand> brandCombo;
    private JComboBox<Location> locationCombo;
    private JTextField sizeField;
    private JFormattedTextField priceField;
    private JFormattedTextField quantityField;
    private JTextArea descriptionArea;

    // 데이터 접근 객체들
    private TopCategoryDAO topCategoryDAO;
    private SubCategoryDAO subCategoryDAO;
    private BrandDAO brandDAO;

    // 왼쪽 InventoryUI와의 실시간 연동을 위한 참조
    private InventoryUI inventoryUI;

    // 색상 상수
    private static final Color PRIMARY_COLOR = new Color(52, 144, 220);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    public ProductAddPage(Object testmain) {
        super(testmain);

        // DAO 초기화
        topCategoryDAO = new TopCategoryDAO();
        subCategoryDAO = new SubCategoryDAO();
        brandDAO = new BrandDAO();

        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadInitialData();
    }

    /**
     * 컴포넌트 초기화
     */
    private void initializeComponents() {
        // 메인 패널들
        headerPanel = new JPanel();
        contentPanel = new JPanel();
        imagePanel = new JPanel();
        formPanel = new JPanel();

        // 헤더 버튼들
        backButton = createStyledButton("←뒤로", LIGHT_GRAY, new Color(70, 70, 70));
        saveButton = createStyledButton("저장하기", SUCCESS_COLOR, Color.WHITE);
        clearButton = createStyledButton("초기화", DANGER_COLOR, Color.WHITE);

        // 이미지 관련
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        loadDefaultImage();

        imageUploadButton = createStyledButton("사진 넣기", PRIMARY_COLOR, Color.WHITE);

        // 폼 입력 필드들
        topCategoryCombo = new JComboBox<>();
        subCategoryCombo = new JComboBox<>();
        productNameField = new JTextField();
        brandCombo = new JComboBox<>();
        locationCombo = new JComboBox<>();
        sizeField = new JTextField();

        // 숫자 입력 필드들
        priceField = new JFormattedTextField();
        priceField.setValue(0);
        quantityField = new JFormattedTextField();
        quantityField.setValue(0);

        descriptionArea = new JTextArea(2, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        // 폼 필드 스타일링
        styleFormComponents();
    }

    /**
     * 레이아웃 설정
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 헤더 패널 설정
        setupHeaderPanel();

        // 컨텐츠 패널 설정
        setupContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * 헤더 패널 설정
     */
    private void setupHeaderPanel() {
        System.out.println("🏗️ 헤더 패널 설정 시작...");

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // 제목
        JLabel titleLabel = new JLabel("새 상품 등록");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));

        // 제목을 감싸는 패널 (가운데 정렬용)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        System.out.println("   - 초기화 버튼 추가: " + clearButton.getText());
        System.out.println("   - 초기화 버튼 크기: " + clearButton.getPreferredSize());
        buttonPanel.add(clearButton);

        System.out.println("   - 저장 버튼 추가: " + saveButton.getText());
        System.out.println("   - 저장 버튼 크기: " + saveButton.getPreferredSize());
        System.out.println("   - 저장 버튼 활성화: " + saveButton.isEnabled());
        buttonPanel.add(saveButton);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        System.out.println("✅ 헤더 패널 설정 완료!");
        System.out.println("   - 버튼 패널 컴포넌트 수: " + buttonPanel.getComponentCount());
    }

    /**
     * 컨텐츠 패널 설정
     */
    private void setupContentPanel() {
        contentPanel.setLayout(new BorderLayout(20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // 이미지 패널 설정
        setupImagePanel();

        // 폼 패널 설정
        setupFormPanel();

        contentPanel.add(imagePanel, BorderLayout.WEST);
        contentPanel.add(formPanel, BorderLayout.CENTER);
    }

    /**
     * 이미지 패널 설정
     */
    private void setupImagePanel() {
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(createTitledBorder("상품 이미지"));

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(280, 280));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageUploadButton = createStyledButton("사진 넣기", PRIMARY_COLOR, Color.WHITE);
        imageUploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(imageLabel);
        centerPanel.add(imageUploadButton);
        centerPanel.add(Box.createVerticalGlue());

        imagePanel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 폼 패널 설정
     */
    private void setupFormPanel() {
        formPanel.setLayout(new BorderLayout());
        formPanel.setBackground(Color.WHITE);

        // 기본 정보 패널
        JPanel basicInfoPanel = createFormSection("기본 정보", new Object[][] {
                { "상위 카테고리", topCategoryCombo },
                { "하위 카테고리", subCategoryCombo },
                { "상품명", productNameField }
        });

        // 상세 정보 패널
        JPanel detailInfoPanel = createFormSection("상세 정보", new Object[][] {
                { "브랜드", brandCombo },
                { "보관 위치", locationCombo },
                { "사이즈", sizeField },
                { "가격 (원)", priceField },
                { "수량", quantityField }
        });

        // 설명 패널
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(Color.WHITE);
        descPanel.setBorder(createTitledBorder("상품 설명"));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(0, 92)); // 높이 조정
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        descPanel.add(scrollPane, BorderLayout.CENTER);

        // 전체 폼 구성
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);

        formContainer.add(basicInfoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(detailInfoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(descPanel);

        formPanel.add(formContainer, BorderLayout.NORTH);
    }

    /**
     * 폼 섹션 생성
     */
    private JPanel createFormSection(String title, Object[][] fields) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(createTitledBorder(title));

        for (Object[] field : fields) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            JLabel label = new JLabel((String) field[0]);
            label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            label.setPreferredSize(new Dimension(100, 25));

            row.add(label, BorderLayout.WEST);
            row.add((Component) field[1], BorderLayout.CENTER);

            section.add(row);
        }

        return section;
    }

    /**
     * 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        System.out.println("🔧 이벤트 리스너 설정 시작...");

        // 뒤로가기 버튼
        System.out.println("   - 뒤로가기 버튼 리스너 설정");
        backButton.addActionListener(e -> {
            System.out.println("🔙 뒤로가기 버튼 클릭됨!");
            goBack();
        });

        saveButton.addActionListener(e -> {
            saveProduct();
        });

        clearButton.addActionListener(e -> {
            clearForm();
        });

        imageUploadButton.addActionListener(e -> {
            selectImage();
        });

        topCategoryCombo.addActionListener(e -> {
            loadSubCategories();
        });
    }

    /**
     * 초기 데이터 로드
     */
    private void loadInitialData() {
        loadTopCategories();
        loadBrands();
        loadLocations();
    }

    /**
     * 상위 카테고리 로드
     */
    private void loadTopCategories() {
        topCategoryCombo.removeAllItems();
        TopCategory dummy = new TopCategory();
        dummy.setTopCategoryName("선택하세요");
        topCategoryCombo.addItem(dummy);

        List<TopCategory> categories = topCategoryDAO.selectAll();
        for (TopCategory category : categories) {
            topCategoryCombo.addItem(category);
        }
    }

    /**
     * 하위 카테고리 로드
     */
    private void loadSubCategories() {
        subCategoryCombo.removeAllItems();

        TopCategory selected = (TopCategory) topCategoryCombo.getSelectedItem();
        if (selected == null || selected.getTopCategoryName().equals("선택하세요")) {
            return;
        }

        SubCategory dummy = new SubCategory();
        dummy.setSubCategoryName("선택하세요");
        subCategoryCombo.addItem(dummy);

        List<SubCategory> subCategories = subCategoryDAO.selectByTop(selected);
        for (SubCategory subCategory : subCategories) {
            subCategoryCombo.addItem(subCategory);
        }
    }

    /**
     * 브랜드 중복 제거 및 정렬
     * - 브랜드 이름을 기준으로 중복 제거
     * - 삽입 순서를 유지하기 위해 LinkedHashMap 사용
     */
    public List<Brand> getUniqueBrands(List<Brand> brands) {
        Map<String, Brand> brandMap = new LinkedHashMap<>(); // insertion-order 유지

        for (Brand brand : brands) {
            String name = brand.getBrandName();
            // 브랜드 이름이 이미 존재하지 않는 경우에만 추가
            if (!brandMap.containsKey(name)) {
                brandMap.put(name, brand);
            }
        }
        return new ArrayList<>(brandMap.values());
    }

    /**
     * 브랜드 로드
     * - 중복된 브랜드 이름은 제거 후 콤보박스에 추가
     */
    private void loadBrands() {
        brandCombo.removeAllItems();

        // 안내용 더미항목 추가
        Brand dummy = new Brand();
        dummy.setBrandName("선택하세요");
        brandCombo.addItem(dummy);

        // 전체 브랜드 로드 및 중복 제거
        List<Brand> brands = brandDAO.selectAllWithSubCategory();
        List<Brand> uniqueBrands = getUniqueBrands(brands); // 중복 제거

        // 중복 제거된 브랜드만 추가
        for (Brand brand : uniqueBrands) {
            brandCombo.addItem(brand);
        }
    }

    /**
     * 위치 로드
     */
    private void loadLocations() {
        locationCombo.removeAllItems();
        Location dummy = new Location();
        dummy.setLocationName("선택하세요");
        locationCombo.addItem(dummy);

        List<Location> locations = RightPageLocationDAO.selectAllName();
        for (Location location : locations) {
            locationCombo.addItem(location);
        }
    }

    /**
     * 이미지 선택
     */
    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            loadImagePreview(selectedFile);
        }
    }

    /**
     * 이미지 미리보기 로드
     */
    private void loadImagePreview(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            Image scaledImage = originalImage.getScaledInstance(280, 280, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
            placeholderTextLabel.setText(""); // 이미지가 로드되면 안내문 제거
        } catch (IOException e) {
            ShowMessage.showAlert(this, "이미지 로드 실패", "이미지를 불러올 수 없습니다.");
            setPlaceholderImage(); // 실패 시 placeholder로 복귀
        }
    }

    /**
     * 기본 이미지 로드
     */
    private void loadDefaultImage() {
        try {
            URL url = getClass().getClassLoader().getResource("images/not-found.png");
            if (url != null) {
                BufferedImage originalImage = ImageIO.read(url);
                Image scaledImage = originalImage.getScaledInstance(280, 280, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText("");
            } else {
                setPlaceholderImage();
            }
        } catch (IOException e) {
            setPlaceholderImage();
        }
    }

    /**
     * 플레이스홀더 이미지 설정 (x_x 이미지 사용)
     */
    private void setPlaceholderImage() {
        try {
            URL url = getClass().getClassLoader().getResource("images/placeholder.png");
            if (url != null) {
                ImageIcon placeholderIcon = new ImageIcon(url);
                Image scaledImage = placeholderIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageLabel.setPreferredSize(new Dimension(280, 280)); // 라벨 크기
                imageLabel.setIcon(new ImageIcon(scaledImage));

                // 안내문은 이미지 아래쪽에 함께 표시
                imageLabel.setText(
                        "<html><div style='text-align: center;'>"
                                + "<div style='margin-top: 8px; font-size: 12px; color: #6c757d;'>"
                                + "상품의 대표 이미지를 업로드해주세요"
                                + "</div></div></html>");

                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);

                // 텍스트 위치 조정
                imageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                imageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("이미지 없음");
            }
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("이미지 로드 실패");
        }
    }

    /**
     * 상품 저장
     */
    private void saveProduct() {
        // 폼 검증
        if (!validateForm()) {
            return;
        }

        // 저장 확인
        if (!ShowMessage.showConfirm(this, "상품 저장", "새 상품을 등록하시겠습니까?")) {
            return;
        }

        try {
            // ProductDetail 객체 생성
            ProductDetail productDetail = createProductDetailFromForm();

            // DB에 저장
            RightPageProductDetailDAO.insert(productDetail);

            // 이미지 저장 처리
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                try {
                    saveProductImage(productDetail.getProduct().getProductId());
                } catch (Exception imageEx) {
                    // 이미지 저장 실패해도 상품 등록은 계속 진행
                }
            }

            // 상품 추가 음성 재생
            playAudioFile("추가.wav");

            // 성공 알림
            showSuccessNotification();

            // 왼쪽 InventoryUI 실시간 업데이트 (충분한 지연으로 DB 커밋 완료 보장)
            if (inventoryUI != null) {
                Timer updateTimer = new Timer(200, e -> {
                    inventoryUI.refreshInventoryData();
                    ((Timer) e.getSource()).stop(); // 한 번만 실행하고 정지
                });
                updateTimer.start();
            }

            // 저장 후 폼 초기화 여부 확인
            if (ShowMessage.showConfirmAfterSave(this, "저장 완료")) {
                clearFormWithoutConfirm();
            }

        } catch (Exception e) {
            e.printStackTrace();

            ShowMessage.showAlert(this, "저장 실패",
                    "❌ 상품 저장 중 오류가 발생했습니다.\n\n" +
                            "오류 내용: " + e.getMessage() + "\n\n" +
                            "입력 내용을 확인하고 다시 시도해주세요.");
        }
    }

    /**
     * 확인 대화상자 없이 폼 초기화 (저장 후 사용)
     */
    public void clearFormWithoutConfirm() {
        // 콤보박스 초기화
        topCategoryCombo.setSelectedIndex(0);
        subCategoryCombo.removeAllItems();
        brandCombo.setSelectedIndex(0);
        locationCombo.setSelectedIndex(0);

        // 텍스트 필드 플레이스홀더로 초기화
        productNameField.setForeground(new Color(150, 150, 150));
        productNameField.setText("예: 나이키 드라이핏 티셔츠");

        sizeField.setForeground(new Color(150, 150, 150));
        sizeField.setText("예: L, XL, 95");

        // 숫자 필드 초기화
        priceField.setValue(0);
        quantityField.setValue(0);

        // 설명 영역 초기화
        descriptionArea.setForeground(new Color(150, 150, 150));
        descriptionArea.setText("상품에 대한 자세한 설명을 입력해주세요...");

        // 이미지 초기화
        selectedImagePath = null;
        loadDefaultImage();

    }

    /**
     * 폼 검증
     */
    private boolean validateForm() {
        System.out.println("🔍 폼 검증 시작...");
        System.out.println("   - 상위카테고리 선택 인덱스: " + topCategoryCombo.getSelectedIndex());

        if (topCategoryCombo.getSelectedIndex() <= 0) {
            System.out.println("❌ 상위 카테고리 미선택");
            ShowMessage.showAlert(this, "입력 오류", "상위 카테고리를 선택해주세요.");
            return false;
        }

        System.out.println("   - 하위카테고리 선택 인덱스: " + subCategoryCombo.getSelectedIndex());
        if (subCategoryCombo.getSelectedIndex() <= 0) {
            System.out.println("❌ 하위 카테고리 미선택");
            ShowMessage.showAlert(this, "입력 오류", "하위 카테고리를 선택해주세요.");
            return false;
        }

        // 상품명 검증 (플레이스홀더 텍스트 제외)
        String productName = productNameField.getText().trim();
        System.out.println("   - 상품명: '" + productName + "'");
        if (productName.isEmpty() || productName.equals("예: 나이키 드라이핏 티셔츠")) {
            System.out.println("❌ 상품명 미입력");
            ShowMessage.showAlert(this, "입력 오류", "상품명을 입력해주세요.");
            productNameField.requestFocus();
            return false;
        }

        if (brandCombo.getSelectedIndex() <= 0) {
            ShowMessage.showAlert(this, "입력 오류", "브랜드를 선택해주세요.");
            return false;
        }

        if (locationCombo.getSelectedIndex() <= 0) {
            ShowMessage.showAlert(this, "입력 오류", "보관 위치를 선택해주세요.");
            return false;
        }

        // 사이즈 검증 (플레이스홀더 텍스트 제외)
        String size = sizeField.getText().trim();
        if (size.isEmpty() || size.equals("예: L, XL, 95")) {
            ShowMessage.showAlert(this, "입력 오류", "사이즈를 입력해주세요.");
            sizeField.requestFocus();
            return false;
        }

        try {
            int price = Integer.parseInt(priceField.getText().replaceAll(",", ""));
            if (price < 0) {
                ShowMessage.showAlert(this, "입력 오류", "가격은 0 이상이어야 합니다.");
                return false;
            }
        } catch (NumberFormatException e) {
            ShowMessage.showAlert(this, "입력 오류", "올바른 가격을 입력해주세요.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                ShowMessage.showAlert(this, "입력 오류", "수량은 0 이상이어야 합니다.");
                return false;
            }
        } catch (NumberFormatException e) {
            ShowMessage.showAlert(this, "입력 오류", "올바른 수량을 입력해주세요.");
            return false;
        }

        System.out.println("✅ 폼 검증 통과!");
        return true;
    }

    /**
     * 폼 데이터로부터 ProductDetail 객체 생성
     */
    private ProductDetail createProductDetailFromForm() {
        // 선택된 항목들 가져오기
        SubCategory selectedSubCategory = (SubCategory) subCategoryCombo.getSelectedItem();
        Brand selectedBrand = (Brand) brandCombo.getSelectedItem();
        Location selectedLocation = (Location) locationCombo.getSelectedItem();

        // 선택된 카테고리 정보 로깅
        System.out.println(
                "🔧 상품 등록: " + selectedSubCategory.getSubCategoryName() + " > " + selectedBrand.getBrandName());

        // 🚨 핵심 수정: 선택된 SubCategory에 해당하는 올바른 Brand와 Location 찾기
        Brand correctBrand = null;
        Location correctLocation = null;

        try {
            // 1. 선택된 SubCategory에 맞는 Brand 찾기
            List<Brand> brandsForSubCategory = brandDAO.selectBySub(selectedSubCategory);
            if (!brandsForSubCategory.isEmpty()) {
                // 사용자가 선택한 브랜드가 이 SubCategory에 속하는지 확인
                for (Brand brand : brandsForSubCategory) {
                    if (brand.getBrandName().equals(selectedBrand.getBrandName())) {
                        correctBrand = brand;
                        break;
                    }
                }
                // 만약 선택한 브랜드가 해당 SubCategory에 없다면 첫 번째 브랜드 사용
                if (correctBrand == null) {
                    correctBrand = brandsForSubCategory.get(0);
                }
            }

            // 2. 올바른 Brand에 맞는 Location 찾기
            if (correctBrand != null) {
                List<Location> locationsForBrand = RightPageLocationDAO.selectByBrand(correctBrand);
                if (!locationsForBrand.isEmpty()) {
                    // 사용자가 선택한 위치가 이 Brand에 속하는지 확인
                    for (Location location : locationsForBrand) {
                        if (location.getLocationName().equals(selectedLocation.getLocationName())) {
                            correctLocation = location;
                            break;
                        }
                    }
                    // 만약 선택한 위치가 해당 Brand에 없다면 첫 번째 위치 사용
                    if (correctLocation == null) {
                        correctLocation = locationsForBrand.get(0);
                    }
                }
            }

            System.out.println("✅ 카테고리 매핑 완료: " +
                    (correctBrand != null ? correctBrand.getBrandName() : "기본값"));

        } catch (Exception e) {
            System.err.println("❌ 카테고리 매핑 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            // 오류 시 기존 선택값 사용
            correctBrand = selectedBrand;
            correctLocation = selectedLocation;
        }

        // Product 객체 구성
        Product product = new Product();
        product.setLocation(correctLocation != null ? correctLocation : selectedLocation);
        product.setProductName(productNameField.getText().trim());
        product.setProductPrice(Integer.parseInt(priceField.getText().replaceAll(",", "")));

        // 설명 텍스트 처리 (플레이스홀더 제외)
        String description = descriptionArea.getText().trim();
        if (description.equals("상품에 대한 자세한 설명을 입력해주세요...")) {
            description = "";
        }
        product.setDescription(description);

        // ProductDetail 객체 구성
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct(product);
        productDetail.setProductSizeName(sizeField.getText().trim());
        productDetail.setProductQuantity(Integer.parseInt(quantityField.getText()));

        return productDetail;
    }

    /**
     * 성공 알림 표시
     */
    private void showSuccessNotification() {
        String productName = productNameField.getText().trim();
        int quantity = Integer.parseInt(quantityField.getText());

        JWindow notification = new JWindow();
        notification.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(212, 237, 218));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 207, 164), 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JLabel titleLabel = new JLabel("✅ 상품 등록 완료!");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setForeground(new Color(21, 87, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productLabel = new JLabel(productName);
        productLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        productLabel.setForeground(new Color(33, 37, 41));
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel quantityLabel = new JLabel(quantity + "개 등록됨");
        quantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        quantityLabel.setForeground(new Color(108, 117, 125));
        quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(productLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(quantityLabel);

        notification.add(panel);
        notification.pack();

        // 화면 중앙 상단에 표시
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - notification.getWidth()) / 2;
        int y = 100;
        notification.setLocation(x, y);

        // 슬라이드 다운 애니메이션
        notification.setLocation(x, y - 50);
        notification.setVisible(true);

        Timer slideTimer = new Timer(10, null);
        slideTimer.addActionListener(new ActionListener() {
            int currentY = y - 50;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentY += 2;
                notification.setLocation(x, currentY);
                if (currentY >= y) {
                    slideTimer.stop();
                }
            }
        });
        slideTimer.start();

        // 4초 후 슬라이드 업 애니메이션으로 사라짐
        Timer hideTimer = new Timer(4000, e -> {
            Timer slideUpTimer = new Timer(10, null);
            slideUpTimer.addActionListener(new ActionListener() {
                int currentY = y;

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentY -= 3;
                    notification.setLocation(x, currentY);
                    if (currentY <= y - 80) {
                        slideUpTimer.stop();
                        notification.dispose();
                    }
                }
            });
            slideUpTimer.start();
        });
        hideTimer.setRepeats(false);
        hideTimer.start();
    }

    /**
     * 폼 초기화
     */
    private void clearForm() {
        // 확인 대화상자 표시
        if (!ShowMessage.showConfirm(
                this,
                "폼 초기화",
                "입력한 모든 내용이 사라집니다. \n계속하시겠습니까?")) {
            return;
        }

        // 콤보박스 초기화
        topCategoryCombo.setSelectedIndex(0);
        subCategoryCombo.removeAllItems();
        brandCombo.setSelectedIndex(0);
        locationCombo.setSelectedIndex(0);

        // 텍스트 필드 플레이스홀더로 초기화
        productNameField.setForeground(new Color(150, 150, 150));
        productNameField.setText("예: 나이키 드라이핏 티셔츠");

        sizeField.setForeground(new Color(150, 150, 150));
        sizeField.setText("예: L, XL, 95");

        // 숫자 필드 초기화
        priceField.setValue(0);
        quantityField.setValue(0);

        // 설명 영역 초기화
        descriptionArea.setForeground(new Color(150, 150, 150));
        descriptionArea.setText("상품에 대한 자세한 설명을 입력해주세요...");

        // 이미지 초기화
        selectedImagePath = null;
        loadDefaultImage();

        // 성공 메시지
        ShowMessage.showAlert(this, "초기화 완료", "모든 입력 내용이 초기화되었습니다.");

        System.out.println("✅ 폼 초기화 완료");
    }

    /**
     * 뒤로가기
     */
    private void goBack() {
        // MainPage의 public 메서드를 통해 ProductShip으로 돌아가기
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
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 폰트 크기 증가
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true); // 배경색 확실히 적용
        button.setContentAreaFilled(true); // 컨텐츠 영역 채우기

        // 텍스트 정렬 명시적으로 중앙 설정
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        // 버튼을 클릭 가능하게 설정
        button.setEnabled(true);

        // 텍스트 길이에 따라 버튼 크기 자동 조정
        int textWidth = button.getFontMetrics(button.getFont()).stringWidth(text);
        int buttonWidth = Math.max(120, textWidth + 40); // 최소 120px, 텍스트 + 여백
        button.setPreferredSize(new Dimension(buttonWidth, 35));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 호버 효과 추가 (텍스트 색상 유지)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor.darker());
                button.setForeground(textColor); // 텍스트 색상 유지
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
                button.setForeground(textColor); // 텍스트 색상 유지
            }
        });

        return button;
    }

    /**
     * 제목 테두리 생성
     */
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                title);
        border.setTitleFont(new Font("맑은 고딕", Font.BOLD, 14));
        border.setTitleColor(new Color(70, 70, 70));
        return border;
    }

    /**
     * 폼 컴포넌트 스타일링
     */
    private void styleFormComponents() {
        Font fieldFont = new Font("맑은 고딕", Font.PLAIN, 14);

        // 콤보박스 스타일링
        JComboBox<?>[] combos = { topCategoryCombo, subCategoryCombo, brandCombo, locationCombo };
        for (JComboBox<?> combo : combos) {
            combo.setFont(fieldFont);
            combo.setBackground(Color.WHITE);
            combo.setPreferredSize(new Dimension(0, 30));
        }

        // 텍스트 필드 스타일링
        productNameField.setFont(fieldFont);
        productNameField.setPreferredSize(new Dimension(0, 30));
        productNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        // 플레이스홀더 텍스트 추가
        addPlaceholder(productNameField, "예: 나이키 드라이핏 티셔츠");

        sizeField.setFont(fieldFont);
        sizeField.setPreferredSize(new Dimension(0, 30));
        sizeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        addPlaceholder(sizeField, "예: L, XL, 95");

        // 숫자 필드 스타일링
        JFormattedTextField[] numberFields = { priceField, quantityField };
        for (JFormattedTextField field : numberFields) {
            field.setFont(fieldFont);
            field.setPreferredSize(new Dimension(0, 30));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        }

        // 텍스트 영역 스타일링
        descriptionArea.setFont(fieldFont);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addPlaceholder(descriptionArea, "상품에 대한 자세한 설명을 입력해주세요...");
    }

    /**
     * 텍스트 필드에 플레이스홀더 추가
     */
    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(new Color(150, 150, 150));
        textField.setText(placeholder);

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
                    textField.setForeground(new Color(150, 150, 150));
                    textField.setText(placeholder);
                }
            }
        });
    }

    /**
     * 텍스트 영역에 플레이스홀더 추가
     */
    private void addPlaceholder(JTextArea textArea, String placeholder) {
        textArea.setForeground(new Color(150, 150, 150));
        textArea.setText(placeholder);

        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(new Color(150, 150, 150));
                    textArea.setText(placeholder);
                }
            }
        });
    }

    /**
     * 음성 명령을 통한 자동 입력 (MicrophoneForm에서 호출)
     */
    public void fillFromVoiceCommand(String voiceText) {
        try {
            // 음성 텍스트 파싱 로직
            // 예: "티셔츠 나이키 엘사이즈 25000원 10개 추가"
            String[] parts = voiceText.toLowerCase().split(" ");

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];

                // 상품명 추출
                if (part.contains("티셔츠") || part.contains("바지") || part.contains("신발")) {
                    productNameField.setText(part);
                }

                // 브랜드 추출
                if (part.contains("나이키") || part.contains("아디다스")) {
                    selectComboItemByText(brandCombo, part);
                }

                // 사이즈 추출
                if (part.contains("사이즈") && i > 0) {
                    sizeField.setText(parts[i - 1]);
                }

                // 가격 추출
                if (part.contains("원")) {
                    String priceStr = part.replaceAll("[^0-9]", "");
                    if (!priceStr.isEmpty()) {
                        priceField.setValue(Integer.parseInt(priceStr));
                    }
                }

                // 수량 추출
                if (part.contains("개") && i > 0) {
                    String quantityStr = parts[i - 1].replaceAll("[^0-9]", "");
                    if (!quantityStr.isEmpty()) {
                        quantityField.setValue(Integer.parseInt(quantityStr));
                    }
                }
            }

            ShowMessage.showAlert(this, "음성 입력 완료", "음성 명령이 폼에 적용되었습니다. 내용을 확인해주세요.");

        } catch (Exception e) {
            ShowMessage.showAlert(this, "음성 입력 오류", "음성 명령을 처리하는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    /**
     * 콤보박스에서 텍스트로 항목 선택
     */
    private void selectComboItemByText(JComboBox<?> combo, String text) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String itemText = combo.getItemAt(i).toString().toLowerCase();
            if (itemText.contains(text)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * InventoryUI 참조 설정 (실시간 업데이트용)
     */
    public void setInventoryUI(InventoryUI inventoryUI) {
        this.inventoryUI = inventoryUI;
    }

    /**
     * 상품 이미지 저장
     * 
     * @param productId 저장된 상품의 ID
     * @return 저장된 이미지 파일 경로
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    private String saveProductImage(int productId) throws IOException {
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            throw new IllegalArgumentException("선택된 이미지 경로가 없습니다.");
        }

        // 원본 파일
        File sourceFile = new File(selectedImagePath);
        if (!sourceFile.exists()) {
            throw new IOException("선택된 이미지 파일이 존재하지 않습니다: " + selectedImagePath);
        }

        // 파일 확장자 추출
        String fileName = sourceFile.getName();
        String fileExtension = "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileExtension = fileName.substring(lastDot);
        }

        // 저장할 파일명 생성 (product{ID}.확장자)
        String newFileName = "product" + productId + fileExtension;

        // 저장 경로 설정 (src/main/resources/images/)
        String projectPath = System.getProperty("user.dir");
        String imagesDir = projectPath + "/src/main/resources/images/";

        // 이미지 디렉토리 생성 (존재하지 않으면)
        File imagesDirFile = new File(imagesDir);
        if (!imagesDirFile.exists()) {
            imagesDirFile.mkdirs();
        }

        // 최종 저장 경로
        String destinationPath = imagesDir + newFileName;
        File destinationFile = new File(destinationPath);

        // 파일 복사
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 상대 경로 반환 (/ 포함한 절대 경로 형태)
        String relativePath = "/images/" + newFileName;

        // 데이터베이스에 이미지 경로 저장 (ProductImage 테이블)
        saveImagePathToDatabase(productId, relativePath);

        System.out.println("📷 이미지 복사 완료:");
        System.out.println("   원본: " + selectedImagePath);
        System.out.println("   대상: " + destinationPath);
        System.out.println("   상대경로: " + relativePath);

        return relativePath;
    }

    /**
     * 이미지 경로를 데이터베이스에 저장
     * 
     * @param productId 상품 ID
     * @param imagePath 이미지 상대 경로
     */
    private void saveImagePathToDatabase(int productId, String imagePath) {
        try {
            // ProductImage 객체 생성
            ProductImage productImage = new ProductImage();
            productImage.setImageURL(imagePath);

            // Product 객체 생성 (ID만 설정)
            Product product = new Product();
            product.setProductId(productId);
            productImage.setProduct(product);

            // 데이터베이스에 저장
            com.sinse.tory.rightpage.util.ProductImageDAO imageDAO = new com.sinse.tory.rightpage.util.ProductImageDAO();
            imageDAO.insert(productImage);

            System.out.println("📷 이미지 경로 DB 저장 완료: " + imagePath);

        } catch (Exception e) {
            System.err.println("❌ 이미지 경로 DB 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 음성 파일 재생
     * 
     * @param audioFileName 재생할 음성 파일명 (예: "추가.wav")
     */
    private void playAudioFile(String audioFileName) {
        try {
            // 음성 파일 경로 설정
            String audioPath = "voice/" + audioFileName;
            URL audioUrl = getClass().getClassLoader().getResource(audioPath);

            if (audioUrl == null) {
                System.err.println("❌ 음성 파일을 찾을 수 없음: " + audioPath);
                return;
            }

            // 오디오 스트림 열기
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // 음성 재생
            clip.start();

            System.out.println("🔊 음성 재생: " + audioFileName);

            // 재생 완료 후 자원 해제 (별도 스레드에서)
            new Thread(() -> {
                try {
                    // 재생 완료까지 대기
                    while (clip.isRunning()) {
                        Thread.sleep(100);
                    }
                    // 자원 해제
                    clip.close();
                    audioInputStream.close();
                } catch (Exception e) {
                    System.err.println("❌ 음성 파일 자원 해제 중 오류: " + e.getMessage());
                }
            }).start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("❌ 지원되지 않는 오디오 형식: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ 오디오 파일 읽기 오류: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("❌ 오디오 라인 사용 불가: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 음성 재생 중 예상치 못한 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}