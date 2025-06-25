package com.sinse.tory.rightpage.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinse.tory.rightpage.datamodification.view.DataModificationPage;
import com.sinse.tory.rightpage.util.PageMove;
import com.sinse.tory.rightpage.util.PageUtil;

/**
 * Testmain
 * - 전체 프레임 구성 테스트용 클래스
 * - 좌우 2분할 레이아웃 (왼쪽: leftPage, 오른쪽: rightPage)
 * - 오른쪽은 상단 페이지 전환 영역 + 하단 마이크 폼 구성
 */
public class Testmain extends JFrame {
	JPanel leftPage; // 왼쪽 패널
	JPanel rightPage; // 오른쪽 전체 패널
	private JPanel rightPageContent; // 오른쪽 내에서 페이지 전환될 영역 (CardLayout)
	PageMove pageMove; // CardLayout 전환 로직을 담당하는 유틸
	ProductShip productShip; // 첫 번째 페이지 (배송 관련)
	DataModificationPage dataModificationPage; // 두 번째 페이지 (데이터 수정 관련)
	MicrophoneForm microphoneForm; // 오른쪽 하단에 고정되는 마이크 입력 폼

	// 여백 설정용 상수
	private static final int WIDTH_MARGIN = 16;
	private static final int HEIGHT_MARGIN = 48;

	public Testmain() {
		// 페이지 유틸 및 컴포넌트 생성
		pageMove = new PageMove();
		dataModificationPage = new DataModificationPage(pageMove);
		productShip = new ProductShip(this, dataModificationPage);

		// 페이지 리스트 구성
		creatPage();

		// 주요 패널 초기화
		rightPage = new JPanel();
		rightPageContent = new JPanel();
		leftPage = new JPanel();
		MicrophoneForm microphoneForm = new MicrophoneForm(this);

		// 전체 레이아웃 설정: 좌우 2분할
		setLayout(new GridLayout(1, 2));
		add(leftPage);
		add(rightPage);

		// 프레임 크기 및 종료 설정
		setSize(new Dimension(PageUtil.Tory_Width, PageUtil.Tory_Hieght));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 오른쪽 UI: BoxLayout으로 수직 구성
		rightPage.setLayout(new BoxLayout(rightPage, BoxLayout.Y_AXIS));
		rightPage.add(rightPageContent); // 위쪽: 전환되는 페이지 영역
		rightPage.add(microphoneForm); // 아래쪽: 고정된 마이크 폼

		// 페이지 전환 영역은 CardLayout 사용
		rightPage.setBorder(BorderFactory.createEmptyBorder(HEIGHT_MARGIN, WIDTH_MARGIN, HEIGHT_MARGIN, WIDTH_MARGIN));
		rightPageContent.setLayout(new CardLayout());
		rightPageContent.setPreferredSize(new Dimension(0, 0));
		rightPageContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		// PageMove에 등록된 모든 페이지를 CardLayout에 추가
		for (int i = 0; i < pageMove.list.size(); i++) {
			rightPageContent.add(pageMove.list.get(i));
		}

		// 초기 페이지: 첫 번째 페이지(productShip) 표시
		pageMove.showPage(0, 1); // 0번 보이고 1번 숨기기
	}

	/**
	 * 페이지 등록 메서드
	 * - CardLayout에 추가될 페이지를 PageMove에 등록
	 */
	public void creatPage() {
		pageMove.list.add(productShip);
		pageMove.list.add(dataModificationPage);
	}

	/**
	 * 애플리케이션 시작점
	 */
	public static void main(String[] args) {
		new Testmain();
	}
}
