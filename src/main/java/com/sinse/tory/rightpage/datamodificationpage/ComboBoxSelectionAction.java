package com.sinse.tory.rightpage.datamodificationpage;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

final class ComboBoxSelectionAction implements ItemListener
{
	private Component componentToActivation;
	
	
	
	ComboBoxSelectionAction(Component componentToActivation)
	{
		this.componentToActivation = componentToActivation;
	}
	
	
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		switch (e.getStateChange())
		{
			case ItemEvent.SELECTED:
				componentToActivation.setEnabled(true);
				break;
			case ItemEvent.DESELECTED:
				componentToActivation.setEnabled(false);
				break;
		}
	}
}