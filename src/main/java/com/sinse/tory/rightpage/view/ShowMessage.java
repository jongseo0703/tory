package com.sinse.tory.rightpage.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ShowMessage{
	
	public static boolean showConfirm(Testmain testmain, String title, String message) {
		JPanel panel = new JPanel();// 메시지를 담을 패널
		JLabel msg = new JLabel();//메시지 내용
		JButton bt_ok = new JButton("확인");
		JButton bt_cancel = new JButton("취소");
		
		
		//버튼 스타일
		bt_ok.setBackground(Color.decode("#75A5FD"));
		bt_cancel.setBackground(Color.white);
		// 패널 스타일과 메시지내용
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(300,100));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		msg.setFont(new Font(null,1, 20));
		msg.setText(message);
		panel.add(msg);
		
		Object[] options = {bt_ok,bt_cancel};
		
		JOptionPane pane = new JOptionPane(
				panel,
				JOptionPane.PLAIN_MESSAGE //아이콘 필요없음
				,JOptionPane.DEFAULT_OPTION //버튼자동 생성 필요없음
				,null //아이콘 설정안함
				,options
				,options[0]//"확인"에 포커스를 둠
				);
		JDialog dialog = pane.createDialog(testmain,title);
		dialog.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.white));
		dialog.setAlwaysOnTop(true);
		
		
		bt_cancel.addActionListener(e->{
			pane.setValue(bt_cancel);   // 사용자 선택값 세팅
		    dialog.dispose();           // 창 닫기
		});
		bt_ok.addActionListener(e->{
			pane.setValue(bt_ok);   // 사용자 선택값 세팅
			dialog.dispose();           // 창 닫기
		});
		
		dialog.setVisible(true);	
		return pane.getValue() == bt_ok;
	}
	public static void showAlert(JComponent parentComponent, String title, String message)
	{
		JPanel panel = new JPanel();// 메시지를 담을 패널
		JLabel msg = new JLabel();//메시지 내용
		JButton bt_ok = new JButton("확인");
		
		
		//버튼 스타일
		bt_ok.setBackground(Color.decode("#75A5FD"));
		// 패널 스타일과 메시지내용
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(300,100));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		msg.setFont(new Font(null,1, 20));
		msg.setText(message);
		panel.add(msg);
		
		Object[] options = {bt_ok};
		
		JOptionPane pane = new JOptionPane(
				panel,
				JOptionPane.PLAIN_MESSAGE //아이콘 필요없음
				,JOptionPane.DEFAULT_OPTION //버튼자동 생성 필요없음
				,null //아이콘 설정안함
				,options
				,options[0]//"확인"에 포커스를 둠
				);
		JDialog dialog = pane.createDialog(parentComponent ,title);
		dialog.getRootPane().setBorder(
				BorderFactory.createLineBorder(Color.white));
		dialog.setAlwaysOnTop(true);
		
		
		bt_ok.addActionListener(e->{
			pane.setValue(bt_ok);   // 사용자 선택값 세팅
			dialog.dispose();           // 창 닫기
		});
		
		dialog.setVisible(true);	
	}
}
