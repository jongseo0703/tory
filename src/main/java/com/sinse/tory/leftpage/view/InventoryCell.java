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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * InventoryCell
 * - 단일 재고 셀 UI (60개 블록 수직 쌓기)
 * - 각 블록은 하위카테고리이며, 상위카테고리 색상을 따라감
 * - hover 시 동일 하위카테고리 블록 전체 강조
 * - 툴팁 및 애니메이션 포함
 */
public class InventoryCell extends JPanel {

	private final Color categoryColor; // 해당 셀의 상위 카테고리 대표 색상
	private final int maxStock = 60; // 최대 블록 개수 (셀 안에 들어갈 최대 수량 기준)
	private JPanel[] slots = new JPanel[maxStock]; // 위치 고정용 패널 배열
	private List<SubCategoryBlock> blocks = new ArrayList<>(); // 셀에 포함될 하위카테고리 블록 목록
	private int currentStock = 0; // 현재 셀에 시각적으로 표시된 총 수량

	/**
	 * InventoryCell 생성자 
	 * - 셀의 전체 배경색(상위카테고리용 색상)을 받아 초기화 
	 * - 내부 레이아웃은 BoxLayout(Y_AXIS)로 설정하여 수직 블록 쌓기
	 */
	public InventoryCell(Color categoryColor) {
		this.categoryColor = categoryColor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false); // 배경 투명 처리 (직접 그릴 것)

		// 툴팁 딜레이 0으로 설정 (마우스 올리면 바로 뜨게)
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	/**
	 * 하위카테고리 블록 정보 등록 (UI에 직접 추가되진 않음)
	 */
	public void addBlock(String subCategoryName, int quantity, Color color) {
		SubCategoryBlock block = new SubCategoryBlock(subCategoryName, quantity, color);
		blocks.add(block);
	}

	/**
	 * 블록을 아래에서 위로 순차적으로 채움 (스레드 기반 애니메이션)
	 */
	public void renderBlocks(Map<String, Color> subCategoryColorMap) {
		removeAll();

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
					Color color = subCategoryColorMap.getOrDefault(block.subCategoryName, Color.LIGHT_GRAY);

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
		Color color = block.color;

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

		// 하위 카테고리별 툴팁 설정
		panel.setToolTipText("<html>" + "<span style='color:#555555;'>상품 유형: </span>"
				+ "<span style='color:#1E90FF; font-weight:bold;'>" + subCategoryName + "</span><br>"
				+ "<span style='color:#555555;'>재고 수량: </span>" + "<span style='color:#1E90FF; font-weight:bold;'>"
				+ block.quantity + "</span>" + "</html>");

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
		});

		return panel;
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
	 *  - 하위카테고리명, 수량, 색상, hover 상태, 연결된 패널 리스트 포함 
	 */
	private static class SubCategoryBlock extends JPanel {
		String subCategoryName;
		int quantity;
		Color color;
		boolean isHovered = false;
		List<JPanel> panels = new ArrayList<>();

		SubCategoryBlock(String subCategoryName, int quantity, Color color) {
			this.subCategoryName = subCategoryName;
			this.quantity = quantity;
			this.color = color;
		}
	}
}
