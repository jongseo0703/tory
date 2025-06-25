package com.sinse.tory.leftpage.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//스윙 관련 패키지 임포트
import javax.swing.Timer;

//InventoryCell을 채우는 애니메이션을 담당하는 클래스
public class CellFillAnimator {
	List<InventoryCell> cells;
	List<String> categoryOrder;
	Map<String, Color> categoryColors;
	Map<String, Integer> productCountPerCategory;
	
	int rows;
	int cols;
	
	Timer animateTimer;
	
	public CellFillAnimator(List<InventoryCell> cells, List<String> categoryOrder,
			Map<String, Color> categoryColors, Map<String, Integer> productCountPerCategory,
			int rows, int cols) {
		this.cells = cells;
		this.categoryOrder = categoryOrder;
		this.categoryColors = categoryColors;
		this.productCountPerCategory = productCountPerCategory;
		this.rows = rows;
		this.cols = cols;
	}
	
	public void start() {
		// InventoryCell은 자체적으로 애니메이션을 처리하므로
		// 여기서는 각 셀의 renderBlocks를 호출하기만 하면 됨
		
		// 애니메이션 효과를 위해 약간의 지연을 두고 시작
		animateTimer = new Timer(200, new ActionListener() {
			private int currentCellIndex = 0;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentCellIndex < cells.size()) {
					InventoryCell cell = cells.get(currentCellIndex);
					// 각 셀은 이미 renderBlocks에서 애니메이션을 처리함
					cell.repaint();
					currentCellIndex++;
				} else {
					animateTimer.stop();
				}
			}
		});
		animateTimer.start();
	}
	
	/**
	 * 애니메이션 중지
	 */
	public void stop() {
		if (animateTimer != null && animateTimer.isRunning()) {
			animateTimer.stop();
		}
	}
} 