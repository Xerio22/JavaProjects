package controllers;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import observers.CheckingManagerObserver;

public class EnablingButtonsOnListChangeListener implements ListDataListener {
	private JList<?> jlist;
	private List<JButton> buttons;

	public EnablingButtonsOnListChangeListener(JList<?> jlist, List<JButton> buttons) {
		this.jlist = jlist;
		this.buttons = buttons;
	}
	
	@Override
	public void intervalRemoved(ListDataEvent e) {
		if(jlist.getModel().getSize() == 0){
			setButtonsEnabled(false);
		}
		
		int index = e.getIndex0();
		int actualSize = jlist.getModel().getSize();
		
		if(index == 0 && actualSize != 1){
			jlist.setSelectedIndex(index + 1);
		}
		else if(actualSize == 1){
			jlist.setSelectedIndex(1);
			jlist.setSelectedIndex(0);
		}
		else{
			jlist.setSelectedIndex(index - 1);
		}
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) {
		jlist.setSelectedIndex(jlist.getModel().getSize() - 1);
		if(searchingNotFinished()) {
			setButtonsEnabled(false);
		}
		else {
			setButtonsEnabled(true);
		}
	}
	
	
	private boolean searchingNotFinished() {
		return !CheckingManagerObserver.SEARCH_FINISHED;
	}

	@Override
	public void contentsChanged(ListDataEvent e) {}
	
	
	private void setButtonsEnabled(boolean isEnabled){
		buttons.forEach(button -> button.setEnabled(isEnabled));
	}
}
