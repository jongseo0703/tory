package com.sinse.tory.rightpage.view;

import java.awt.Color;
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
	 * 등록 확인 메시지 박스를 표시하고 사용자의 선택을 반환
	 * (등록 전용 메시지 – 짧은 메시지)
	 * 
	 * @param parent  부모 컴포넌트
	 * @param title   제목
	 * @param message 메시지 내용
	 * @return 사용자가 확인 버튼을 클릭하면 true, 취소 버튼을 클릭하면 false
	 */
	public static boolean showConfirmForRegist(JComponent parent, String title, String message) {
		JPanel panel = new JPanel();
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(360, 120));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// 간결한 메시지는 JTextArea로
		JTextArea msgArea = new JTextArea(message);
		msgArea.setFont(new Font(null, Font.BOLD, 18));
		msgArea.setOpaque(false);
		msgArea.setEditable(false);
		msgArea.setFocusable(false);
		msgArea.setWrapStyleWord(true);
		msgArea.setLineWrap(true);
		msgArea.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
		msgArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		msgArea.setBorder(null);

		JLabel subText = new JLabel("(선택하면 입력 내용이 유지됩니다)");
		subText.setFont(new Font(null, Font.PLAIN, 12));
		subText.setForeground(Color.GRAY);
		subText.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JButton bt_ok = new JButton("확인");
		JButton bt_cancel = new JButton("취소");
		bt_ok.setBackground(Color.decode("#75A5FD"));
		bt_cancel.setBackground(Color.white);

		Object[] options = { bt_ok, bt_cancel };

		panel.add(msgArea);
		panel.add(subText);

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
	 * 상품 등록 후 계속 등록할 것인지 확인하는 메시지 박스를 표시
	 * (저장 완료 전용 메시지 – 길고 정제된 메시지)
	 * 
	 * @param parent 부모 컴포넌트
	 * @param title  제목
	 * @return 사용자가 "확인" 버튼을 클릭하면 true, "취소" 버튼을 클릭하면 false
	 */
	public static boolean showConfirmAfterSave(JComponent parent, String title) {
		JPanel panel = new JPanel();
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(380, 140));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// 메시지를 JLabel 2줄로 나누기
		JLabel mainMsg = new JLabel("새로운 상품을 계속 등록하시겠습니까?");
		mainMsg.setFont(new Font(null, Font.BOLD, 16));
		mainMsg.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JLabel subMsg = new JLabel("(아니오를 선택하면 입력 내용이 유지됩니다)");
		subMsg.setFont(new Font(null, Font.BOLD, 14));
		subMsg.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JLabel guide = new JLabel("(선택하면 입력 내용이 유지됩니다)");
		guide.setFont(new Font(null, Font.PLAIN, 12));
		guide.setForeground(Color.GRAY);
		guide.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JButton bt_ok = new JButton("확인");
		JButton bt_cancel = new JButton("취소");
		bt_ok.setBackground(Color.decode("#75A5FD"));
		bt_cancel.setBackground(Color.white);

		Object[] options = { bt_ok, bt_cancel };

		panel.add(mainMsg);
		panel.add(Box.createVerticalStrut(5));
		panel.add(subMsg);
		panel.add(Box.createVerticalStrut(10));
		panel.add(guide);

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
	 * @return 사용자가 확인 버튼을 클릭하면 true, 취소 버튼을 클릭하면 false
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
		msgArea.setAlignmentX(JComponent.LEFT_ALIGNMENT); // 왼쪽 정렬
		msgArea.setBorder(null); // 테두리 제거

		// 서브텍스트
		JLabel subText = new JLabel("(선택하면 입력 내용이 유지됩니다)");
		subText.setFont(new Font(null, Font.PLAIN, 12)); // 폰트 설정
		subText.setForeground(Color.GRAY); // 회색 글씨
		subText.setAlignmentX(JComponent.LEFT_ALIGNMENT); // 왼쪽 정렬

		// 버튼
		JButton bt_ok = new JButton("확인");
		JButton bt_cancel = new JButton("취소");
		bt_ok.setBackground(Color.decode("#75A5FD")); // 확인 버튼은 파란색 배경
		bt_cancel.setBackground(Color.white); // 취소 버튼은 흰색 배경
		Object[] options = { bt_ok, bt_cancel };

		// 메시지 구성
		panel.add(msgArea);
		panel.add(subText);

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
		msgArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		msgArea.setBorder(null);

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
