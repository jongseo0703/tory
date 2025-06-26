package com.sinse.tory.rightpage.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ShowMessage {

	
	/**
	 * 상품 등록 후 계속 등록할 것인지 확인하는 메시지 박스를 표시
	 * (저장 완료 전용 메시지 – 길고 정제된 메시지)
	 * 
	 * @param parent 부모 컴포넌트
	 * @param title  제목
	 * @return 사용자가 "예" 버튼을 클릭하면 true, "아니오" 버튼을 클릭하면 false
	 */
	public static boolean showConfirmAfterSave(JComponent parent, String title) {
		JPanel panel = new JPanel();
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(380, 140));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel mainMsg = new JLabel("새로운 상품을 계속 등록하시겠습니까?");
		mainMsg.setFont(new Font(null, Font.BOLD, 16));

		JButton bt_ok = new JButton("예");
		JButton bt_cancel = new JButton("아니오");
		bt_ok.setBackground(Color.decode("#75A5FD"));
		bt_cancel.setBackground(Color.white);

		Dimension buttonSize = new Dimension(80, 35); // 원하는 공통 크기 지정
		bt_ok.setPreferredSize(buttonSize);
		bt_cancel.setPreferredSize(buttonSize);

		Object[] options = { bt_ok, bt_cancel };

		panel.add(mainMsg);

		JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options,
				options[0]);
		JDialog dialog = pane.createDialog(parent, title);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.white));
		dialog.setAlwaysOnTop(true);

		bt_cancel.addActionListener(e -> {
			pane.setValue(bt_cancel);
			dialog.dispose();
		});
		bt_ok.addActionListener(e -> {
			pane.setValue(bt_ok);
			dialog.dispose();
		});

		dialog.setVisible(true);
		return pane.getValue() == bt_ok;
	}

	/**
	 * 확인 메시지 박스를 표시하고 사용자의 선택을 반환
	 *
	 * @param parent  부모 컴포넌트
	 * @param title   제목
	 * @param message 메시지 내용
	 * @return 사용자가 "예" 버튼을 클릭하면 true, "아니오" 버튼을 클릭하면 false
	 */
	public static boolean showConfirm(JComponent parent, String title, String message) {
		JPanel panel = new JPanel();
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(360, 120));
		panel.setMaximumSize(new Dimension(360, 200));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// 메시지 (굵은 글씨)
		JTextArea msgArea = new JTextArea(message); // JTextArea를 사용하여 줄바꿈과 단어 기준 줄바꿈을 지원
		msgArea.setFont(new Font(null, Font.BOLD, 18)); // 폰트 설정
		msgArea.setOpaque(false); // 배경 투명 설정
		msgArea.setEditable(false); // 편집 불가능 설정
		msgArea.setFocusable(false); // 포커스 불가능 설정
		msgArea.setWrapStyleWord(true); // 단어 기준 줄바꿈
		msgArea.setLineWrap(true); // 줄바꿈 설정
		msgArea.setMaximumSize(new Dimension(320, Integer.MAX_VALUE)); // 최대 폭 제한
		msgArea.setBorder(null); // 테두리 제거
	

		// 버튼
		JButton bt_ok = new JButton("예");
		JButton bt_cancel = new JButton("아니오");
		bt_ok.setBackground(Color.decode("#75A5FD")); // 확인 버튼은 파란색 배경
		bt_cancel.setBackground(Color.white); // 취소 버튼은 흰색 배경

		Dimension buttonSize = new Dimension(80, 35); // 원하는 공통 크기 지정
		bt_ok.setPreferredSize(buttonSize);
		bt_cancel.setPreferredSize(buttonSize);

		Object[] options = { bt_ok, bt_cancel };

		// 메시지 구성
		panel.add(msgArea);

		// 다이얼로그 구성
		JOptionPane pane = new JOptionPane(
				panel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,
				options,
				options[0]);
		JDialog dialog = pane.createDialog(parent, title);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.white)); // 테두리 설정
		dialog.setAlwaysOnTop(true); // 항상 위에 표시

		bt_cancel.addActionListener(e -> {
			pane.setValue(bt_cancel); // 취소 버튼 클릭 시 값 설정
			dialog.dispose(); // 다이얼로그 닫기
		});

		bt_ok.addActionListener(e -> {
			pane.setValue(bt_ok); // 확인 버튼 클릭 시 값 설정
			dialog.dispose(); // 다이얼로그 닫기
		});

		dialog.setVisible(true); // 다이얼로그 표시
		return pane.getValue() == bt_ok; // 확인 버튼이 클릭되었는지 여부 반환
	}

	/**
	 * 메시지 박스를 표시
	 *
	 * @param parentComponent 부모 컴포넌트
	 * @param title           제목
	 * @param message         메시지 내용
	 */
	public static void showAlert(JComponent parentComponent, String title, String message) {
		JPanel panel = new JPanel();
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(360, 120));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// 메시지 (굵은 글씨)
		JTextArea msgArea = new JTextArea(message);
		msgArea.setFont(new Font(null, Font.BOLD, 18));
		msgArea.setOpaque(false);
		msgArea.setEditable(false);
		msgArea.setFocusable(false);
		msgArea.setWrapStyleWord(true);
		msgArea.setLineWrap(true);
		msgArea.setBorder(null);
		msgArea.setMaximumSize(new Dimension(320, Integer.MAX_VALUE)); // 최대 폭 제한

		// 버튼
		JButton bt_ok = new JButton("확인");
		bt_ok.setBackground(Color.decode("#75A5FD")); // 확인버튼 파란색 배경

		Object[] options = { bt_ok };

		panel.add(msgArea);

		JOptionPane pane = new JOptionPane(
				panel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,
				options,
				options[0]);
		JDialog dialog = pane.createDialog(parentComponent, title);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.white)); // 테두리 설정
		dialog.setAlwaysOnTop(true); // 항상 위에 표시

		bt_ok.addActionListener(e -> {
			pane.setValue(bt_ok); // 확인 버튼 클릭 시 값 설정
			dialog.dispose(); // 다이얼로그 닫기
		});

		dialog.setVisible(true);
	}
}
