package com.tory.rightpage.datamodificationpage;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

final class DetailDataTable extends JPanel
{
	DetailDataTable()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
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
	
	
	
	private void addRow(TableData[]... tableDatas)
	{
		for (int i = 0; i < tableDatas.length; i++)
		{
			JPanel row = new JPanel();
			for (int j = 0; j < tableDatas[i].length; j++)
			{
				row.add(tableDatas[i][j]);
			}
			row.setLayout(new GridLayout(1, tableDatas[i].length));
			add(row);
			
			int rowPreferredHeight = row.getPreferredSize().height;
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPreferredHeight));
		}
	}
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
}