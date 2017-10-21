package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import models.Filter;
import views.CheckedFilterTab;

public class CheckingManagerObserver implements Observer {

	private JTabbedPane tabsPanel;
	private JList<Filter> filtersList;

	public CheckingManagerObserver(JList<Filter> filtersList, JTabbedPane tabsPanel) {
		this.filtersList = filtersList;
		this.tabsPanel = tabsPanel;
	}

	@Override
	public void update(Observable observable, Object filter) {
		// TODO if it would be need for having a message I can create special class CheckedFilter containing checked filter and attached messages 
		addNewTabWithCheckedFilterData((Filter) filter);
		removeCheckedFilterFromList();
	}
	
	private void addNewTabWithCheckedFilterData(Filter filter) {
		Filter checkedFilter = filter;
		tabsPanel.addTab(checkedFilter.getOemNumber(), new CheckedFilterTab(checkedFilter));
	}

	
	private void removeCheckedFilterFromList() {
		DefaultListModel<Filter> listModel = (DefaultListModel<Filter>) filtersList.getModel();
		listModel.removeElementAt(0);
	}
}
