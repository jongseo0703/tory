package com.sinse.tory.main.view;

/*
 * 임포트 순서 static 임포트 패키지 -> java 패키지 -> javax 패키지 -> 외부라이브러리
 */

//컬렉션 관련 패키지 임포트
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//awt 관련 패키지 임포트
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//스윙 관련 패키지 임포트
import javax.swing.Timer;

//박스를 채우는 애니메이션을 담당하는 클래스
public class BoxFillAnimator {
	InventoryBox[] boxes;
	List<String> categoryOrder;
	Map<String, Color> categoryColors;
	Map<String, Integer> productCountPerCategory;
	
	int rows;
	int cols;
	
	int[] filled;
	
	Timer animateTimer;
	
	public BoxFillAnimator(InventoryBox[] boxes, List<String> categoryOrder,
			Map<String, Color> categoryColors, Map<String, Integer> productCountPerCategory,
			int rows, int cols) {
		this.boxes = boxes;
		this.categoryOrder = categoryOrder;
		this.categoryColors = categoryColors;
		this.productCountPerCategory = productCountPerCategory;
		this.rows = rows;
		this.cols = cols;
		this.filled = new int[cols];
	}
	
	public void start() {
		animateTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean more = false;
				for(int col = 0; col < cols; col++) {
					int count = Math.min(productCountPerCategory.getOrDefault(categoryOrder.get(col), 0), rows);
					if(filled[col] < count) {
						int row = rows - filled[col] - 1;
						int index = row * cols + col;
						if(index >= 0 && index < boxes.length && boxes[index] != null) {
							boxes[index].setFillColor(categoryColors.get(categoryOrder.get(col)));
							boxes[index].repaint();
						}
						filled[col]++;
						more = true;
					}
				}
				if(!more) animateTimer.stop();
			}
		});
		animateTimer.start();
	}
}
