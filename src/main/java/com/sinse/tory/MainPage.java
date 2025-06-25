package com.sinse.tory;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.leftpage.view.InventoryUI;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.util.PageUtil;
import com.sinse.tory.rightpage.view.MicrophoneForm;
import com.sinse.tory.rightpage.view.ProductShip;
import com.sinse.tory.rightpage.view.ProductAddPage;
import com.sinse.tory.rightpage.view.InventoryLogHistoryPage;

public class MainPage extends JFrame{
	
	// 왼쪽 패널 (통합된 InventoryUI)
	InventoryUI inventoryUI;
	
	// 오른쪽 패널 구성요소들
	JPanel rightPanel;
	JPanel rightPageContent; // 페이지 전환 영역 (CardLayout)
	PageMove pageMove; // CardLayout 전환 로직
	ProductShip productShip; // 첫 번째 페이지 (입고/출고)
	ProductAddPage productAddPage; // 두 번째 페이지 (상품 추가)
	InventoryLogHistoryPage inventoryLogHistoryPage; // 세 번째 페이지 (입출고 내역)
	MicrophoneForm microphoneForm; // 오른쪽 하단 마이크 입력 폼
	
	// 여백 설정용 상수
	private static final int WIDTH_MARGIN = 16;
	private static final int HEIGHT_MARGIN = 48;
	
	public MainPage() {
		
		// 통합된 InventoryUI 사용 (main/view 기능 포함)
		inventoryUI = new InventoryUI();
		inventoryUI.setPreferredSize(new Dimension(960,1080));
		
		// RightPage 컴포넌트들 초기화
		initializeRightPageComponents();
		
		// 오른쪽 패널 구성
		setupRightPanel();
	
		setTitle("음성기반 창고관리 Tory");
		setSize(1920,1080);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1,2));
		add(inventoryUI);
		add(rightPanel);
	
		setVisible(true);
		
		System.out.println("✅ 완전 통합된 MainPage 실행됨");
		System.out.println("🎯 특징:");
		System.out.println("   - 왼쪽: InventoryUI (깔끔한 UI + 완전한 기능)");
		System.out.println("   - 오른쪽: ProductShip + ProductAddPage + InventoryLogHistoryPage + MicrophoneForm");
		System.out.println("   - 정렬 기능: 재고량순, 출고순, 입고일순");
		System.out.println("   - 페이지 전환: CardLayout으로 동적 전환");
	}
	
	/**
	 * RightPage 관련 컴포넌트들 초기화
	 */
	private void initializeRightPageComponents() {
		// 페이지 전환 로직 초기화
		pageMove = new PageMove();
		
		// 각 페이지 초기화
		productAddPage = new ProductAddPage(this); // MainPage 참조 전달
		productShip = new ProductShip(this, null); // MainPage 참조 전달, dataModificationPage는 null
		inventoryLogHistoryPage = new InventoryLogHistoryPage(this); // 입출고 내역 페이지 초기화
		
		// ProductShip에 InventoryUI 참조 전달 (실시간 업데이트용)
		productShip.setInventoryUIReference(inventoryUI);
		
		// ProductAddPage에 InventoryUI 참조 전달 (실시간 업데이트용)
		productAddPage.setInventoryUI(inventoryUI);
		
		// 페이지 리스트 구성
		createPageList();
		
		// 마이크 폼 초기화
		microphoneForm = new MicrophoneForm(this); // MainPage 참조 전달
		
		// 왼쪽 InventoryUI와 오른쪽 ProductShip 연결
		setupLeftRightConnection();
	}
	
	/**
	 * 왼쪽 InventoryUI와 오른쪽 ProductShip 간의 연결 설정
	 */
	private void setupLeftRightConnection() {
		// InventoryUI에 ProductShip 참조 전달 (클릭 이벤트 처리용)
		inventoryUI.setProductShipReference(productShip);
	}
	
	/**
	 * 페이지 등록 메서드
	 */
	private void createPageList() {
		pageMove.list.add(productShip);
		pageMove.list.add(productAddPage);
		pageMove.list.add(inventoryLogHistoryPage);
	}
	
	/**
	 * 오른쪽 패널 구성
	 */
	private void setupRightPanel() {
		rightPanel = new JPanel();
		rightPageContent = new JPanel();
		
		// 오른쪽 UI: BoxLayout으로 수직 구성
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(rightPageContent); // 위쪽: 전환되는 페이지 영역
		rightPanel.add(microphoneForm); // 아래쪽: 고정된 마이크 폼
		
		// 페이지 전환 영역은 CardLayout 사용
		rightPanel.setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		rightPageContent.setLayout(new CardLayout());
		rightPageContent.setPreferredSize(new Dimension(0, 0));
		rightPageContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		// PageMove에 등록된 모든 페이지를 CardLayout에 추가
		for (int i = 0; i < pageMove.list.size(); i++) {
			rightPageContent.add(pageMove.list.get(i), String.valueOf(i));
		}
		
		// 초기 페이지: 첫 번째 페이지(productShip) 표시
		pageMove.showPage(0, 1); // 0번 보이고 1번 숨기기
	}
	
	/**
	 * 페이지 전환을 위한 public 메서드
	 * @param showPageIndex 보여줄 페이지 인덱스
	 * @param hidePageIndex 숨길 페이지 인덱스
	 */
	public void switchPage(int showPageIndex, int hidePageIndex) {
		if (pageMove != null) {
			pageMove.showPage(showPageIndex, hidePageIndex);
		}
	}
	
	/**
	 * ProductAddPage로 전환하는 편의 메서드
	 */
	public void showProductAddPage() {
		showSpecificPage(1); // ProductAddPage 보이기
		System.out.println("✅ ProductAddPage로 전환됨");
	}
	
	/**
	 * ProductShip으로 전환하는 편의 메서드
	 */
	public void showProductShipPage() {
		showSpecificPage(0); // ProductShip 보이기
		System.out.println("✅ ProductShip 페이지로 전환됨");
	}
	
	/**
	 * InventoryLogHistoryPage로 전환하는 편의 메서드
	 */
	public void showInventoryLogHistoryPage() {
		showSpecificPage(2); // InventoryLogHistoryPage 보이기
		System.out.println("✅ 입출고 내역 페이지로 전환됨");
	}
	
	/**
	 * 특정 페이지만 보이도록 전환 (다른 모든 페이지는 숨김)
	 * @param pageIndex 보여줄 페이지 인덱스
	 */
	private void showSpecificPage(int pageIndex) {
		if (pageMove != null && pageIndex >= 0 && pageIndex < pageMove.list.size()) {
			// CardLayout으로 해당 페이지 활성화
			CardLayout cardLayout = (CardLayout) rightPageContent.getLayout();
			cardLayout.show(rightPageContent, String.valueOf(pageIndex));
		}
	}
	
    public static void main( String[] args ){
    	new MainPage();
    }
}
