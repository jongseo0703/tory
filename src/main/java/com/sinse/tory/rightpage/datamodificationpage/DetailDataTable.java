package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Content 부분의 테이블 영역
final class DetailDataTable extends JPanel
{
	DetailDataTable()
	{
		// 열을 세로로 정렬하기 위한 레이아웃
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// 테이블에 열을 채우는 과정.
		addRow
		(
			new TableData[]
			{
				new TableData("serial number", getTextField("11111111", false)),
				new TableData("name", new JComboBox())
			},
			new TableData[]
			{
				new TableData("brand", new JComboBox()),
				new TableData("price", getFormattedTextField(15000, true)),
			}
		);
	}
	
	
	
	// 테이블에 TableData를 추가하는 함수
	// 한 열에 n개의 TableData를 삽입할 수 있음
	private void addRow(TableData[]... tableDatas)
	{
		// 삽입할 열의 수만큼 반복
		for (int i = 0; i < tableDatas.length; i++)
		{
			// 열 패널
			JPanel row = new JPanel();
			for (int j = 0; j < tableDatas[i].length; j++)
			{
				// 현재 열에 TableData 삽입
				row.add(tableDatas[i][j]);
			}
			// 열 내의 TableData 수에 맞게 너비를 자동 조정
			row.setLayout(new GridLayout(1, tableDatas[i].length));
			add(row);
			
			// 높이는 TableData의 높이에 맞게 조정
			int rowPreferredHeight = row.getPreferredSize().height;
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPreferredHeight));
		}
	}
	// TableData의 생성자의 2번째 매개변수에 넣을 컴포넌트를 생서하여 반환하는 함수
	private JTextField getTextField(String text, boolean enabled)
	{
		JTextField textField = new JTextField(text);
		
		textField.setEnabled(enabled);
		
		return textField;
	}
	private JFormattedTextField getFormattedTextField(int number, boolean enabled)
	{
		JFormattedTextField textField = new JFormattedTextField(number);
		
		textField.setEnabled(enabled);
		
		return textField;
	}
	//
}