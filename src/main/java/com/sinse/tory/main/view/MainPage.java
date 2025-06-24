package com.sinse.tory.main.view;
/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

//스윙 관련 패키지 임포트
import javax.swing.JFrame;
import javax.swing.JPanel;

//선언한 라이브러리 패키지 임포트
import com.sinse.tory.db.model.Product;

//메인페이지
public class MainPage extends JFrame {
	//상수 영역
	private static final int ROWS = 10; //행
	private static final int BOX_SIZE = 65; //박스의 크기
	
	//레이아웃 관련
	MainPageLayoutBuilder layoutBuilder;
	JPanel p_left;
	JPanel p_box;
	JPanel p_right;
	
	//박스 및 애니메이션 관련
	InventoryBox[] boxes;
	InventoryBoxFactory boxFactory;
	BoxFillAnimator animator;
	
	//데이터 로드 및 처리 관련
	MainPageDataLoader mainPageDataLoader;
	CategoryManager categoryManager;
	
	List<String> categoryOrder;
	Map<String, Color> categoryColors;
	Map<String, Integer> productCountPerCategory;
	Map<String, List<Product>> categoryProductMap;
	
	Map<InventoryBox, Product> boxProductMap = new LinkedHashMap<>();
	
	public MainPage() {
		
		//데이터 로드 관련
		mainPageDataLoader = new MainPageDataLoader();
		categoryManager = new CategoryManager();
		
		categoryManager.init(mainPageDataLoader.loadTopCategories(), mainPageDataLoader.loadProducts());
		
		categoryOrder = categoryManager.getCategoryOrder();
		categoryColors = categoryManager.getCategoryColors();
		productCountPerCategory = categoryManager.getProductCountPerCategory();
		categoryProductMap = categoryManager.categorizeProducts(mainPageDataLoader.loadProducts());
		
		//레이아웃 생성 관련
		layoutBuilder = new MainPageLayoutBuilder();
		p_left = layoutBuilder.buildLeft(categoryOrder);
		p_right = layoutBuilder.buildRight();
		
		//박스 생성 관련
		int cols = categoryOrder.size();
		boxFactory = new InventoryBoxFactory(ROWS, cols, BOX_SIZE);
		p_box = layoutBuilder.getBoxPanel();
		boxes = boxFactory.createBoxes(categoryOrder, categoryProductMap, categoryColors, p_box);
		
		//애니메이션 효과 관련
		animator = new BoxFillAnimator(boxes, categoryOrder, categoryColors, productCountPerCategory, ROWS, cols);
		animator.start();
		
		//부착
		add(p_left, BorderLayout.WEST);
		add(p_right, BorderLayout.EAST);
		
		//전체 사이즈 설정
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height); // 화면 가득
		setVisible(true); //화면 보여주기
		setLocationRelativeTo(null); //화면 중앙에 화면 띄우기
		setDefaultCloseOperation(EXIT_ON_CLOSE); //화면을 끄면 자동으로 종료됨
	}
}
