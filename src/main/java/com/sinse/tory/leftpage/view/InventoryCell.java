package com.sinse.tory.leftpage.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import com.sinse.tory.db.model.Product;

/**
 * InventoryCell
 * - 단일 재고 셀 UI (60개 블록 수직 쌓기)
 * - 각 블록은 하위카테고리이며, 상위카테고리 색상을 따라감
 * - hover 시 동일 하위카테고리 블록 전체 강조
 * - 툴팁에 상품 목록 포함
 */
public class InventoryCell extends JPanel {

	private final int maxStock = 60; // 최대 블록 개수 (셀 안에 들어갈 최대 수량 기준)
	private JPanel[] slots = new JPanel[maxStock]; // 위치 고정용 패널 배열
	private List<SubCategoryBlock> blocks = new ArrayList<>(); // 셀에 포함될 하위카테고리 블록 목록
	private int currentStock = 0; // 현재 셀에 시각적으로 표시된 총 수량
	private InventoryUI parentUI; // 부모 InventoryUI 참조 (클릭 이벤트 전달용)
	
	// 떨림 애니메이션 관련 필드
	private Timer shakeTimer; // 떨림 애니메이션 타이머
	private Random random = new Random(); // 랜덤 떨림 효과용
	private int originalX, originalY; // 원래 위치 저장
	private boolean isShaking = false; // 떨림 상태 플래그
	private int totalQuantity = 0; // 전체 수량 (60개 넘는지 확인용)

	/**
	 * InventoryCell 생성자 
	 * - 셀의 전체 배경색(상위카테고리용 색상)을 받아 초기화 
	 * - 내부 레이아웃은 BoxLayout(Y_AXIS)로 설정하여 수직 블록 쌓기
	 */
	public InventoryCell(Color categoryColor) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false); // 배경 투명 처리 (직접 그릴 것)

		// 툴팁 딜레이 0으로 설정 (마우스 올리면 바로 뜨게)
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	/**
	 * 하위카테고리 블록 정보 등록 (UI에 직접 추가되진 않음)
	 * @param subCategoryName 하위카테고리명
	 * @param quantity 수량
	 * @param color 색상
	 * @param products 해당 하위카테고리에 속한 상품 목록
	 */
	public void addBlock(String subCategoryName, int quantity, Color color, List<Product> products) {
		SubCategoryBlock block = new SubCategoryBlock(subCategoryName, quantity, color, products);
		blocks.add(block);
		totalQuantity += quantity; // 전체 수량 누적
	}

	/**
	 * 기존 호환성을 위한 오버로드 메서드
	 */
	public void addBlock(String subCategoryName, int quantity, Color color) {
		addBlock(subCategoryName, quantity, color, new ArrayList<>());
	}

	/**
	 * 부모 InventoryUI 참조 설정
	 * @param parentUI 부모 InventoryUI 인스턴스
	 */
	public void setParentUI(InventoryUI parentUI) {
		this.parentUI = parentUI;
	}

	/**
	 * 블록을 아래에서 위로 순차적으로 채움 (스레드 기반 애니메이션)
	 */
	public void renderBlocks(Map<String, Color> subCategoryColorMap) {
		removeAll();
		currentStock = 0; // 현재 스톡 초기화
		
		// 떨림 애니메이션 중지 (새로 렌더링하므로)
		if (isShaking) {
			stopShakeAnimation();
		}

		// 고정 위치 확보용 빈 패널 60개 삽입
		for (int i = 0; i < maxStock; i++) {
			JPanel placeholder = new JPanel(); // 초기엔 투명
			placeholder.setPreferredSize(new Dimension(60, 12));
			placeholder.setMaximumSize(new Dimension(60, 12));
			placeholder.setOpaque(false);
			slots[i] = placeholder;
			add(placeholder); // 아래에서 위로 붙이려면 나중에 거꾸로 add도 가능
		}

		Thread thread = new Thread(() -> {

			try {
				for (SubCategoryBlock block : blocks) {
					int limit = Math.min(block.quantity, maxStock - currentStock);

					for (int i = 0; i < limit; i++) {
						final int index = maxStock - 1 - currentStock; // 아래에서 위로
						JPanel panel = createBlockPanel(block);
						block.panels.add(panel);

						// Swing 컴포넌트는 EDT에서만 수정해야 함
						SwingUtilities.invokeLater(() -> {
							remove(slots[index]); // 기존 placeholder 제거
							slots[index] = panel;
							add(panel, index); // 해당 자리에 새로 삽입
							revalidate();
							repaint();
						});

						currentStock++;
						Thread.sleep(20); // 속도 조절 (ms)
					}

					if (currentStock >= maxStock)
						break;
				}
				
				// 렌더링 완료 후 60개 초과 시 떨림 애니메이션 시작
				SwingUtilities.invokeLater(() -> {
					if (totalQuantity > maxStock) {
						startShakeAnimation();
					}
				});
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		thread.start();
		revalidate();
		repaint();
	}

	/**
	 * 블록 단위 JPanel 생성 
	 * - 지정된 색상으로 채워진 둥근 사각형 형태의 패널을 생성 
	 * - Hover 시 같은 하위 카테고리 블록 전체 강조되도록 이벤트 처리
	 */
	private JPanel createBlockPanel(SubCategoryBlock block) {
		String subCategoryName = block.subCategoryName;
		
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g; // 더 정밀한 그래픽 처리를 위해

				// 투명 배경 전체 초기화 - 둥근 사각형 외부를 지우기 위한 처리
				g2.setColor(getParent().getBackground()); // 부모 배경색으로 초기화
				g2.fillRect(0, 0, getWidth(), getHeight());

				// 블록 내부 색 채우기 (hover 시 darker 처리)
				// 안티앨리어싱: 모서리를 부드럽게 그리기 위한 설정
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(block.isHovered ? block.color.darker() : block.color);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 사각형

				// 흰색 테두리 그리기
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2)); // 선 두께 2px
				g2.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 흰색 테두리
			}
		};

		panel.setPreferredSize(new Dimension(60, 12));
		panel.setMaximumSize(new Dimension(60, 12));
		panel.setOpaque(true);
		panel.setBackground(new Color(0, 0, 0, 0)); // 완전 투명처럼 보이게 설정

		// 상품 목록을 포함한 툴팁 생성
		String productListHtml = generateProductListHtml(block.products);
		panel.setToolTipText("<html>" + 
			"<div style='width: 300px; padding: 10px;'>" +
			"<span style='color:#555555;'>상품 유형: </span>" +
			"<span style='color:#1E90FF; font-weight:bold;'>" + subCategoryName + "</span><br>" +
			"<span style='color:#555555;'>재고 수량: </span>" + 
			"<span style='color:#1E90FF; font-weight:bold;'>" + block.quantity + "</span><br><br>" +
			"<span style='color:#555555; font-weight:bold;'>포함 상품:</span><br>" +
			productListHtml +
			"</div>" +
			"</html>");

		// Hover 시 강조 처리
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				highlight(subCategoryName, true); // 해당 하위카테고리 블록 전체 강조
			}

			@Override
			public void mouseExited(MouseEvent e) {
				highlight(subCategoryName, false); // 복원
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 시 해당 하위카테고리의 첫 번째 상품을 오른쪽 페이지로 전달
				if (parentUI != null && !block.products.isEmpty()) {
					Product selectedProduct = block.products.get(0); // 첫 번째 상품 선택
					parentUI.sendProductToRightPage(selectedProduct);
					
					System.out.println("🖱️ 셀 클릭됨:");
					System.out.println("   - 하위카테고리: " + subCategoryName);
							System.out.println("📦 선택된 상품: " + selectedProduct.getProductName());
				}
			}
		});

		return panel;
	}

	/**
	 * 상품 목록을 HTML 형태로 생성
	 */
	private String generateProductListHtml(List<Product> products) {
		if (products == null || products.isEmpty()) {
			return "<span style='color:#888888; font-style:italic;'>상품 정보 없음</span>";
		}

		StringBuilder html = new StringBuilder();
		int maxDisplayProducts = 8; // 최대 표시할 상품 수
		
		for (int i = 0; i < Math.min(products.size(), maxDisplayProducts); i++) {
			Product product = products.get(i);
			html.append("<span style='color:#2E8B57;'>• ")
				.append(product.getProductName())
				.append("</span>");
			
			// 가격 정보가 있으면 표시
			if (product.getProductPrice() > 0) {
				html.append(" <span style='color:#888888; font-size:11px;'>(")
					.append(String.format("%,d", product.getProductPrice()))
					.append("원)</span>");
			}
			
			if (i < Math.min(products.size(), maxDisplayProducts) - 1) {
				html.append("<br>");
			}
		}
		
		// 더 많은 상품이 있으면 표시
		if (products.size() > maxDisplayProducts) {
			html.append("<br><span style='color:#888888; font-style:italic;'>")
				.append("외 ").append(products.size() - maxDisplayProducts).append("개 상품 더</span>");
		}
		
		return html.toString();
	}

	/**
	 * Hover 강조 효과 - 동일 하위 카테고리명을 가진 블록 전체를 어둡게 강조하거나 원래 색으로 복원
	 */
	private void highlight(String subCategoryName, boolean on) {
		for (SubCategoryBlock block : blocks) {
			if (block.subCategoryName.equals(subCategoryName)) {
				block.isHovered = on;
				for (JPanel p : block.panels) {
					p.setBackground(on ? block.color.darker() : block.color);
					p.setOpaque(on); // 강조 시만 칠함
					p.repaint();
				}
			}
		}
	}

	/**
	 * 내부 클래스: SubCategoryBlock
	 * - 하위카테고리명, 수량, 색상, hover 상태, 연결된 패널 리스트, 상품 목록 포함 
	 */
	private static class SubCategoryBlock extends JPanel {
		String subCategoryName;
		int quantity;
		Color color;
		boolean isHovered = false;
		List<JPanel> panels = new ArrayList<>();
		List<Product> products = new ArrayList<>(); // 상품 목록 추가

		SubCategoryBlock(String subCategoryName, int quantity, Color color, List<Product> products) {
			this.subCategoryName = subCategoryName;
			this.quantity = quantity;
			this.color = color;
			this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
		}
	}
	
	/**
	 * 떨림 애니메이션 시작 (60개 초과 시)
	 */
	private void startShakeAnimation() {
		if (isShaking) return; // 이미 떨고 있으면 중복 실행 방지
		
		isShaking = true;
		
		// 원래 위치 저장
		originalX = getX();
		originalY = getY();
		
		// 떨림 타이머 생성 (50ms마다 위치 변경)
		shakeTimer = new Timer(50, e -> {
			if (isShaking) {
				// 랜덤한 떨림 효과 (-3 ~ +3 픽셀 범위)
				int shakeX = originalX + random.nextInt(7) - 3;
				int shakeY = originalY + random.nextInt(7) - 3;
				
				setLocation(shakeX, shakeY);
				repaint();
			}
		});
		
		shakeTimer.start();
		
		// 툴팁에 과재고 경고 추가
		updateToolTipForOverstock();
		
		System.out.println("🔥 과재고 경고! 셀이 떨림 애니메이션 시작 (총 수량: " + totalQuantity + "/60)");
	}
	
	/**
	 * 떨림 애니메이션 중지
	 */
	private void stopShakeAnimation() {
		if (!isShaking) return;
		
		isShaking = false;
		
		if (shakeTimer != null) {
			shakeTimer.stop();
			shakeTimer = null;
		}
		
		// 원래 위치로 복원
		setLocation(originalX, originalY);
		repaint();
		
		System.out.println("✅ 떨림 애니메이션 중지됨");
	}
	
	/**
	 * 과재고 시 툴팁 업데이트
	 */
	private void updateToolTipForOverstock() {
		// 첫 번째 블록의 툴팁에 과재고 경고 추가
		if (!blocks.isEmpty() && !blocks.get(0).panels.isEmpty()) {
			JPanel firstPanel = blocks.get(0).panels.get(0);
			String originalTooltip = firstPanel.getToolTipText();
			
			// 과재고 경고 메시지 추가
			String warningMessage = "<div style='background-color:#ffebee; border:2px solid #f44336; padding:8px; margin-bottom:10px; border-radius:5px;'>" +
									"<span style='color:#d32f2f; font-weight:bold; font-size:12px;'>⚠️ 과재고 경고!</span><br>" +
									"<span style='color:#d32f2f; font-size:11px;'>현재 수량: " + totalQuantity + "개 (최대: 60개)</span>" +
									"</div>";
			
			if (originalTooltip != null && originalTooltip.contains("<html>")) {
				// 기존 툴팁에 경고 메시지 삽입
				String newTooltip = originalTooltip.replace("<div style='width: 300px; padding: 10px;'>", 
															warningMessage + "<div style='width: 300px; padding: 10px;'>");
				firstPanel.setToolTipText(newTooltip);
			}
		}
	}
	
	/**
	 * 총 수량 재계산 및 떨림 상태 업데이트
	 */
	public void updateShakeStatus() {
		totalQuantity = 0;
		for (SubCategoryBlock block : blocks) {
			totalQuantity += block.quantity;
		}
		
		if (totalQuantity > maxStock && !isShaking) {
			startShakeAnimation();
		} else if (totalQuantity <= maxStock && isShaking) {
			stopShakeAnimation();
		}
	}
}
