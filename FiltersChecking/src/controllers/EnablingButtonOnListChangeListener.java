package controllers;

import javax.swing.JButton;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class EnablingButtonOnListChangeListener implements ListDataListener {
	private ListModel<?> listModel;
	private JButton button;

	public EnablingButtonOnListChangeListener(ListModel<?>filtersListModel, JButton button) {
		this.listModel = filtersListModel;
		this.button = button;
	}
	
	@Override
	public void intervalRemoved(ListDataEvent e) {
		if(listModel.getSize() == 0){
			button.setEnabled(false);
		}
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) {button.setEnabled(true);}
	
	
	@Override
	public void contentsChanged(ListDataEvent e) {}
}
