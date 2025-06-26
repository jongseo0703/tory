# 🏭 Tory - 음성기반 창고관리 시스템

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> **음성 인식 기반 창고관리 솔루션**  
> 직관적인 시각화와 실시간 재고 추적을 통한 스마트 창고 운영

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [핵심 기능](#-핵심-기능)
- [기술 스택](#-기술-스택)
- [시스템 아키텍처](#-시스템-아키텍처)
- [데이터베이스 설계](#-데이터베이스-설계)
- [UI/UX 설계](#-uiux-설계)
- [성능 최적화](#-성능-최적화)
- [설치 및 실행](#-설치-및-실행)
- [개발자 가이드](#-개발자-가이드)

---

## 🎯 프로젝트 개요

**Tory**는 중소규모 창고를 위한 혁신적인 음성기반 창고관리 시스템입니다. 전통적인 키보드/마우스 입력의 한계를 극복하고, 음성 명령을 통한 직관적인 재고 관리를 제공합니다.

### 🌟 프로젝트 비전

- **효율성**: 음성 명령으로 입출고 작업 시간 50% 단축
- **직관성**: 시각적 재고 현황으로 한눈에 파악 가능한 창고 상태
- **정확성**: 실시간 데이터 동기화로 재고 오차 최소화
- **확장성**: 모듈화된 아키텍처로 기능 확장 용이

---

## ✨ 핵심 기능

### 🎤 음성 기반 인터페이스

```java
// 음성 명령 예시
"티셔츠 나이키 엘사이즈 25000원 10개 추가"
"청바지 리바이스 32인치 5개 출고"
```

### 📊 실시간 시각화 대시보드

- **재고 현황 매트릭스**: 60칸 격자로 카테고리별 재고량 표시
- **과재고 경고**: 60개 초과 시 덜덜 떨림 애니메이션
- **색상 코딩**: 상위 카테고리별 고유 색상으로 직관적 분류
- **동적 정렬**: 재고량순, 입고순, 출고순 정렬 지원

### 📦 통합 재고 관리

- **상품 등록**: 카테고리, 브랜드, 위치 기반 체계적 분류
- **입출고 처리**: 실시간 수량 업데이트 및 로그 기록
- **이미지 관리**: 상품별 이미지 등록 및 자동 경로 관리
- **재고 추적**: 상세한 입출고 내역 조회 및 필터링

### 🔍 고급 검색 및 필터링

- **다중 조건 검색**: 날짜, 상품명, 입출고 구분별 검색
- **실시간 필터링**: 클라이언트 사이드 필터링으로 빠른 응답
- **히스토리 추적**: 모든 재고 변동 이력 완전 보존

---

## 🛠 기술 스택

### **Core Technologies**

| 기술      | 버전     | 용도           | 선택 이유                           |
| --------- | -------- | -------------- | ----------------------------------- |
| **Java**  | 8+       | 백엔드 로직    | 플랫폼 독립성, 강력한 OOP 지원      |
| **Swing** | Built-in | GUI 프레임워크 | 네이티브 Look&Feel, 풍부한 컴포넌트 |
| **MySQL** | 8.0+     | 데이터베이스   | ACID 보장, 관계형 데이터 최적화     |
| **Maven** | 3.6+     | 빌드 도구      | 의존성 관리, 표준화된 프로젝트 구조 |

### **Design Patterns & Architecture**

```java
// 싱글톤 패턴 - 데이터베이스 연결 관리
public class DBManager {
    private static DBManager instance;
    private Connection con;

    public static DBManager getInstance() {
        if(instance == null) {
            instance = new DBManager();
        }
        return instance;
    }
}

// DAO 패턴 - 데이터 접근 추상화
public class ProductDAO {
    DBManager dbManager = DBManager.getInstance();

    public List<Product> selectAll() {
        // 복잡한 JOIN 쿼리로 완전한 객체 그래프 구성
    }
}

// Strategy 패턴 - 정렬 알고리즘
public interface SortStrategy {
    List<String> sort(List<String> categories, List<Product> products);
}
```

### **Key Libraries & Dependencies**

```xml
<dependencies>
    <!-- MySQL JDBC Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- JUnit for Testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>3.8.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 🏗 시스템 아키텍처

### **전체 아키텍처 다이어그램**

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   InventoryUI   │  │ ProductAddPage  │  │ ProductShip  │ │
│  │   (재고현황)     │  │   (상품등록)     │  │  (입출고)    │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │            InventoryLogHistoryPage                      │ │
│  │                (입출고 내역)                             │ │
│  └─────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                     Business Layer                          │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ CategoryManager │  │ DataLoader      │  │ SortStrategy │ │
│  │   (카테고리관리)  │  │  (데이터로딩)    │  │   (정렬로직)  │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                        │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────┐ │
│  │ ProductDAO  │ │ BrandDAO    │ │ LocationDAO │ │   ...   │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────┘ │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                  DBManager                              │ │
│  │              (싱글톤 연결 관리)                          │ │
│  └─────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    Database Layer                           │
├─────────────────────────────────────────────────────────────┤
│                       MySQL 8.0                            │
└─────────────────────────────────────────────────────────────┘
```

### **핵심 설계 원칙**

#### 1. **관심사의 분리 (Separation of Concerns)**

```java
// UI 레이어 - 오직 사용자 인터페이스만 담당
public class InventoryUI extends JPanel {
    private MainPageDataLoader dataLoader;    // 데이터 로딩 위임
    private CategoryManager categoryManager;   // 카테고리 관리 위임
}

// 비즈니스 레이어 - 도메인 로직 처리
public class CategoryManager {
    public Map<String, Color> getCategoryColors() {
        // 카테고리별 색상 매핑 로직
    }
}

// 데이터 접근 레이어 - 순수 데이터 CRUD
public class ProductDAO {
    public List<Product> selectAll() {
        // 복잡한 JOIN 쿼리 실행
    }
}
```

#### 2. **의존성 역전 (Dependency Inversion)**

```java
// 고수준 모듈이 저수준 모듈에 의존하지 않음
public interface SortStrategy {
    List<String> sort(List<String> categories, List<Product> products);
}

public class SortByQuantity implements SortStrategy {
    @Override
    public List<String> sort(List<String> categories, List<Product> products) {
        // 재고량 기준 정렬 구현
    }
}
```

#### 3. **단일 책임 원칙 (Single Responsibility)**

```java
// 각 클래스는 하나의 명확한 책임만 가짐
public class DatabaseInitializer {
    // 오직 데이터베이스 초기화만 담당
    public static void initializeSchema() { }
}

public class CellFillAnimator {
    // 오직 셀 채우기 애니메이션만 담당
    public void startAnimation() { }
}
```

---

## 🗄 데이터베이스 설계

### **ERD (Entity Relationship Diagram)**

```
TopCategory (1) ─────────── (N) SubCategory
                                     │
                                     │ (1)
                                     │
                                     ▼
                                   Brand (1) ─────────── (N) Location
                                                              │
                                                              │ (1)
                                                              │
                                                              ▼
                                                          Product (1) ─┬─ (N) ProductDetail
                                                                       │
                                                                       └─ (N) ProductImage

ProductDetail (1) ─────────── (N) InventoryLog
```

### **테이블 구조 상세**

#### 🏷 **TopCategory** - 상위 카테고리

```sql
CREATE TABLE TopCategory(
    top_category_id INT PRIMARY KEY AUTO_INCREMENT,
    top_category_name VARCHAR(20) UNIQUE  -- 상의, 하의, 신발 등
);
```

#### 🏷 **SubCategory** - 하위 카테고리

```sql
CREATE TABLE SubCategory(
    sub_category_id INT PRIMARY KEY AUTO_INCREMENT,
    sub_category_name VARCHAR(20) UNIQUE,  -- 티셔츠, 청바지, 운동화 등
    top_category_id INT,
    FOREIGN KEY(top_category_id) REFERENCES TopCategory(top_category_id)
);
```

#### 🏢 **Brand** - 브랜드

```sql
CREATE TABLE Brand(
    brand_id INT PRIMARY KEY AUTO_INCREMENT,
    brand_name VARCHAR(50),  -- 나이키, 아디다스, 스투시 등
    sub_category_id INT,
    FOREIGN KEY(sub_category_id) REFERENCES SubCategory(sub_category_id)
);
```

#### 📍 **Location** - 창고 위치

```sql
CREATE TABLE Location(
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    location_name VARCHAR(20) UNIQUE,  -- 창고 내 물리적 위치
    brand_id INT,
    FOREIGN KEY(brand_id) REFERENCES Brand(brand_id)
);
```

#### 📦 **Product** - 상품

```sql
CREATE TABLE Product(
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(60),     -- 상품명
    product_price INT,            -- 가격
    product_description TEXT,     -- 상품 설명
    location_id INT,
    FOREIGN KEY(location_id) REFERENCES Location(location_id)
);
```

#### 📏 **ProductDetail** - 상품 상세 (사이즈별)

```sql
CREATE TABLE ProductDetail(
    product_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    product_size_name VARCHAR(20),  -- S, M, L, XL 등
    product_quantity INT,           -- 재고 수량
    product_id INT,
    FOREIGN KEY(product_id) REFERENCES Product(product_id)
);
```

#### 🖼 **ProductImage** - 상품 이미지

```sql
CREATE TABLE ProductImage(
    product_image_id INT PRIMARY KEY AUTO_INCREMENT,
    image_url TEXT,  -- 이미지 파일 경로
    product_id INT,
    FOREIGN KEY(product_id) REFERENCES Product(product_id)
);
```

#### 📊 **InventoryLog** - 입출고 로그

```sql
CREATE TABLE InventoryLog(
    inventory_log_id INT PRIMARY KEY AUTO_INCREMENT,
    change_type ENUM('IN', 'OUT'),              -- 입고/출고 구분
    quantity INT,                               -- 변동 수량
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 변동 시각
    product_detail_id INT,
    FOREIGN KEY(product_detail_id) REFERENCES ProductDetail(product_detail_id)
);
```

### **데이터베이스 최적화 전략**

#### 1. **인덱스 최적화**

```sql
-- 자주 조회되는 컬럼에 인덱스 생성
CREATE INDEX idx_product_name ON Product(product_name);
CREATE INDEX idx_inventory_log_date ON InventoryLog(changed_at);
CREATE INDEX idx_product_detail_quantity ON ProductDetail(product_quantity);
```

#### 2. **쿼리 최적화**

```java
// 복잡한 JOIN을 한 번의 쿼리로 처리하여 N+1 문제 해결
public List<Product> selectAll() {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT p.*, l.*, b.*, s.*, t.*, pd.*, pi.* ");
    sql.append("FROM Product p ");
    sql.append("INNER JOIN Location l ON p.location_id = l.location_id ");
    sql.append("INNER JOIN Brand b ON l.brand_id = b.brand_id ");
    sql.append("INNER JOIN SubCategory s ON b.sub_category_id = s.sub_category_id ");
    sql.append("INNER JOIN TopCategory t ON s.top_category_id = t.top_category_id ");
    sql.append("LEFT JOIN ProductDetail pd ON p.product_id = pd.product_id ");
    sql.append("LEFT JOIN ProductImage pi ON p.product_id = pi.product_id");

    // 한 번의 쿼리로 완전한 객체 그래프 구성
}
```

#### 3. **연결 풀링 (Connection Pooling)**

```java
// 싱글톤 패턴으로 연결 재사용
public class DBManager {
    private static DBManager instance;
    private Connection con;

    // 연결 재사용으로 오버헤드 최소화
    public Connection getConnection() {
        return con;
    }
}
```

---

## 🎨 UI/UX 설계

### **전체 레이아웃 구조**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Tory 창고관리 시스템                            │
├─────────────────────────────┬───────────────────────────────────────────┤
│                             │  ┌─────────────────────────────────────┐  │
│                             │  │            Header Panel            │  │
│                             │  │  ← 뒤로  │  페이지 제목  │  버튼들   │  │
│                             │  └─────────────────────────────────────┘  │
│         Left Panel          │  ┌─────────────────────────────────────┐  │
│       (InventoryUI)         │  │                                     │  │
│                             │  │         Content Panel               │  │
│   ┌─────────────────────┐   │  │     (CardLayout 페이지 전환)         │  │
│   │   로고  │    시계   │   │  │                                     │  │
│   └─────────────────────┘   │  │  • ProductShip (입출고)              │  │
│   ┌─────────────────────┐   │  │  • ProductAddPage (상품등록)         │  │
│   │ 재고현황 │ 정렬필터 │   │  │  • InventoryLogHistoryPage          │  │
│   └─────────────────────┘   │  │                                     │  │
│   ┌─────────────────────┐   │  └─────────────────────────────────────┘  │
│   │                     │   │  ┌─────────────────────────────────────┐  │
│   │    60칸 재고 격자    │   │  │         MicrophoneForm              │  │
│   │   (10 x 6 매트릭스)  │   │  │         (음성 입력)                  │  │
│   │                     │   │  └─────────────────────────────────────┘  │
│   └─────────────────────┘   │                                          │
│                             │                                          │
└─────────────────────────────┴───────────────────────────────────────────┘
```

### **핵심 UI 컴포넌트**

#### 1. **InventoryCell - 재고 시각화의 핵심**

```java
public class InventoryCell extends JPanel {
    private final int maxStock = 60;  // 최대 60개 제한
    private List<SubCategoryBlock> blocks;

    // 과재고 시 떨림 애니메이션
    private void startShakeAnimation() {
        Timer shakeTimer = new Timer(50, e -> {
            int shakeX = originalX + random.nextInt(7) - 3;
            int shakeY = originalY + random.nextInt(7) - 3;
            setLocation(shakeX, shakeY);
        });
    }

    // 블록별 호버 효과
    private void highlight(String subCategoryName, boolean on) {
        // 동일 하위카테고리 전체 강조
    }
}
```

#### 2. **동적 페이지 전환 시스템**

```java
public class MainPage extends JFrame {
    private CardLayout cardLayout;

    // 깔끔한 페이지 전환
    private void showSpecificPage(int pageIndex) {
        CardLayout layout = (CardLayout) rightPageContent.getLayout();
        layout.show(rightPageContent, String.valueOf(pageIndex));
    }
}
```

#### 3. **실시간 성공 알림**

```java
private void showSuccessNotification(String action, int quantity, String productName) {
    JWindow notification = new JWindow();
    // 슬라이드 다운 애니메이션
    slideDownAnimation(notification, targetY);

    // 4초 후 자동 사라짐
    Timer timer = new Timer(4000, e -> slideUpAnimation(notification, y));
}
```

### **색상 시스템 & 브랜딩**

```java
// 일관된 색상 팔레트
private static final Color PRIMARY_COLOR = new Color(52, 144, 220);    // 메인 블루
private static final Color SUCCESS_COLOR = new Color(40, 167, 69);     // 성공 그린
private static final Color DANGER_COLOR = new Color(220, 53, 69);      // 경고 레드
private static final Color WARNING_COLOR = new Color(255, 149, 0);     // 주의 오렌지
private static final Color LIGHT_GRAY = new Color(248, 249, 250);      // 배경 그레이

// 카테고리별 고유 색상 자동 생성
public Map<String, Color> generateCategoryColors(List<String> categories) {
    Map<String, Color> colors = new HashMap<>();
    float hueStep = 360.0f / categories.size();

    for (int i = 0; i < categories.size(); i++) {
        float hue = i * hueStep / 360.0f;
        Color color = Color.getHSBColor(hue, 0.7f, 0.9f);
        colors.put(categories.get(i), color);
    }
    return colors;
}
```

### **접근성 & 사용성 고려사항**

#### ♿ **접근성 (Accessibility)**

- **키보드 네비게이션**: 모든 기능을 키보드로 접근 가능
- **색상 대비**: WCAG 2.1 AA 기준 준수 (4.5:1 대비율)
- **툴팁 정보**: 상세한 상품 정보를 툴팁으로 제공
- **음성 피드백**: 작업 완료 시 음성 알림

#### 🎯 **사용성 (Usability)**

- **직관적 아이콘**: 📦(상품), 📥(입고), 📤(출고), 🔍(검색)
- **상태 피드백**: 로딩, 성공, 오류 상태를 명확히 표시
- **단축키 지원**: Ctrl+S(저장), Ctrl+R(새로고침), Esc(취소)
- **반응형 레이아웃**: 다양한 화면 크기에 대응

---

## ⚡ 성능 최적화

### **메모리 관리**

#### 1. **객체 풀링 (Object Pooling)**

```java
// 재사용 가능한 객체 풀 관리
public class ComponentPool {
    private Queue<InventoryCell> cellPool = new LinkedList<>();

    public InventoryCell borrowCell() {
        return cellPool.isEmpty() ? new InventoryCell() : cellPool.poll();
    }

    public void returnCell(InventoryCell cell) {
        cell.reset();  // 상태 초기화
        cellPool.offer(cell);
    }
}
```

#### 2. **지연 로딩 (Lazy Loading)**

```java
public class ProductDAO {
    // 필요할 때만 이미지 로드
    private void loadProductImages(Product product) {
        if (product.getProductImages() == null) {
            product.setProductImages(selectImagesByProductId(product.getId()));
        }
    }
}
```

### **UI 성능 최적화**

#### 1. **가상화 (Virtualization)**

```java
// 대량 데이터 처리 시 가상화 적용
public class VirtualizedTable extends JTable {
    @Override
    public void setModel(TableModel model) {
        // 보이는 영역의 행만 렌더링
        super.setModel(new VirtualTableModel(model));
    }
}
```

#### 2. **비동기 처리**

```java
// EDT 블로킹 방지
public void refreshInventoryData() {
    SwingUtilities.invokeLater(() -> {
        // 백그라운드에서 데이터 로드
        CompletableFuture.supplyAsync(() -> {
            return dataLoader.loadProducts();
        }).thenAcceptAsync(products -> {
            // UI 업데이트는 EDT에서
            SwingUtilities.invokeLater(() -> updateUI(products));
        });
    });
}
```

### **데이터베이스 최적화**

#### 1. **배치 처리**

```java
public void batchInsertInventoryLogs(List<InventoryLog> logs) {
    String sql = "INSERT INTO InventoryLog (change_type, quantity, product_detail_id) VALUES (?, ?, ?)";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        for (InventoryLog log : logs) {
            pstmt.setString(1, log.getChangeType().name());
            pstmt.setInt(2, log.getQuantity());
            pstmt.setInt(3, log.getProductDetailId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();  // 한 번에 실행
    }
}
```

#### 2. **쿼리 캐싱**

```java
public class QueryCache {
    private Map<String, Object> cache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5분

    public <T> T getCachedResult(String key, Supplier<T> supplier) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return (T) entry.getValue();
        }

        T result = supplier.get();
        cache.put(key, new CacheEntry(result, System.currentTimeMillis()));
        return result;
    }
}
```

### **애니메이션 최적화**

#### 1. **프레임 레이트 제어**

```java
public class OptimizedAnimator {
    private static final int TARGET_FPS = 60;
    private static final long FRAME_TIME = 1000 / TARGET_FPS;

    public void startAnimation() {
        Timer timer = new Timer((int) FRAME_TIME, e -> {
            // 부드러운 60fps 애니메이션
            updateFrame();
        });
        timer.start();
    }
}
```

#### 2. **하드웨어 가속 활용**

```java
// 시스템 프로퍼티 설정으로 하드웨어 가속 활성화
System.setProperty("sun.java2d.opengl", "true");
System.setProperty("sun.java2d.d3d", "true");
```

---

## 🚀 설치 및 실행

### **시스템 요구사항**

| 구분             | 최소 사양                             | 권장 사양                            |
| ---------------- | ------------------------------------- | ------------------------------------ |
| **OS**           | Windows 10, macOS 10.14, Ubuntu 18.04 | Windows 11, macOS 12+, Ubuntu 20.04+ |
| **Java**         | JDK 8+                                | JDK 11+                              |
| **메모리**       | 4GB RAM                               | 8GB RAM                              |
| **저장공간**     | 500MB                                 | 1GB                                  |
| **데이터베이스** | MySQL 5.7+                            | MySQL 8.0+                           |

### **설치 가이드**

#### 1. **저장소 클론**

```bash
git clone https://github.com/your-org/tory.git
cd tory
```

#### 2. **데이터베이스 설정**

```sql
-- MySQL 데이터베이스 생성
CREATE DATABASE tory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여
CREATE USER 'tory_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON tory.* TO 'tory_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. **설정 파일 수정**

```java
// src/main/java/com/sinse/tory/db/common/config/Config.java
public class Config {
    public static final String url = "jdbc:mysql://localhost:3306/tory";
    public static final String user = "tory_user";
    public static final String pass = "secure_password";
}
```

#### 4. **빌드 및 실행**

```bash
# Maven을 사용한 빌드
mvn clean compile

# 애플리케이션 실행
mvn exec:java -Dexec.mainClass="com.sinse.tory.App"

# 또는 IDE에서 직접 실행
# MainPage.main() 메서드 실행
```

### **Docker를 이용한 실행 (선택사항)**

```dockerfile
# Dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app
COPY target/tory-*.jar app.jar
COPY src/main/resources/ resources/

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: tory
      MYSQL_USER: tory_user
      MYSQL_PASSWORD: secure_password
    ports:
      - '3306:3306'
    volumes:
      - mysql_data:/var/lib/mysql
      - ./db/schema.sql:/docker-entrypoint-initdb.d/schema.sql

  tory:
    build: .
    depends_on:
      - mysql
    ports:
      - '8080:8080'
    environment:
      DB_HOST: mysql
      DB_PORT: 3306

volumes:
  mysql_data:
```

---

## 👨‍💻 개발자 가이드

### **프로젝트 구조**

```
tory/
├── src/main/java/com/sinse/tory/
│   ├── App.java                          # 메인 애플리케이션 진입점
│   ├── MainPage.java                     # 통합 메인 UI
│   ├── common/view/                      # 공통 UI 컴포넌트
│   │   └── Clock.java
│   ├── db/                              # 데이터베이스 레이어
│   │   ├── common/
│   │   │   ├── config/Config.java       # DB 연결 설정
│   │   │   ├── exception/               # 커스텀 예외
│   │   │   └── util/
│   │   │       ├── DBManager.java       # 싱글톤 DB 매니저
│   │   │       └── DatabaseInitializer.java
│   │   ├── model/                       # 엔티티 모델
│   │   │   ├── Product.java
│   │   │   ├── ProductDetail.java
│   │   │   ├── InventoryLog.java
│   │   │   └── ...
│   │   └── repository/                  # DAO 레이어
│   │       ├── ProductDAO.java
│   │       ├── InventoryLogDAO.java
│   │       └── ...
│   ├── leftpage/view/                   # 왼쪽 패널 (재고 현황)
│   │   ├── InventoryUI.java
│   │   ├── InventoryCell.java
│   │   ├── CategoryManager.java
│   │   ├── MainPageDataLoader.java
│   │   └── sort/                        # 정렬 전략
│   │       ├── SortStrategy.java
│   │       ├── SortByQuantity.java
│   │       └── SortByRecentShipment.java
│   └── rightpage/                       # 오른쪽 패널 (기능별 페이지)
│       ├── view/
│       │   ├── ProductShip.java         # 입출고 페이지
│       │   ├── ProductAddPage.java      # 상품 등록 페이지
│       │   ├── InventoryLogHistoryPage.java  # 입출고 내역
│       │   ├── MicrophoneForm.java      # 음성 입력
│       │   └── ShowMessage.java         # 알림 메시지
│       ├── db/repository/               # 오른쪽 전용 DAO
│       ├── identifier/                  # 식별자 업데이트
│       └── util/                        # 유틸리티
├── src/main/resources/
│   ├── images/                          # 상품 이미지
│   └── voice/                           # 음성 파일
├── db/
│   └── schema.sql                       # 데이터베이스 스키마
└── pom.xml                              # Maven 설정
```

### **코딩 컨벤션**

#### **네이밍 규칙**

```java
// 클래스: PascalCase
public class ProductDetailDAO { }

// 메서드: camelCase
public List<Product> selectByCategory() { }

// 변수: camelCase
private int productQuantity;

// 상수: UPPER_SNAKE_CASE
private static final String DEFAULT_IMAGE_PATH = "/images/default.png";

// 패키지: lowercase
package com.sinse.tory.db.repository;
```

#### **주석 규칙**

```java
/**
 * 상품 상세 정보를 데이터베이스에서 조회하는 DAO 클래스
 *
 * 주요 기능:
 * - 상품별 사이즈 정보 관리
 * - 재고 수량 업데이트
 * - 입출고 로그 연동
 *
 * @author 개발팀
 * @version 1.0
 * @since 2024-01-01
 */
public class ProductDetailDAO {

    /**
     * 특정 상품의 모든 사이즈 정보를 조회
     *
     * @param productId 조회할 상품 ID
     * @return 해당 상품의 사이즈별 상세 정보 리스트
     * @throws SQLException 데이터베이스 연결 오류 시
     */
    public List<ProductDetail> selectByProductId(int productId) throws SQLException {
        // 구현 내용
    }
}
```

---

### **코드 리뷰 체크리스트**

- [ ] 코딩 컨벤션 준수
- [ ] 단위 테스트 작성
- [ ] 성능 영향 분석
- [ ] 보안 취약점 검토
- [ ] 문서 업데이트

---

<div align="center">

**🏭 Tory - 미래의 창고관리를 오늘 경험하세요**

</div>
