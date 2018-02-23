package observers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controllers.FiltersCheckingManager;
import models.Filter;
import views.CheckedFilterTab;
import views.FiltersListManagementView;
import views.TabTitle;

public class CheckingManagerObserver implements Observer {
	private JTabbedPane tabsPanel;
	private JList<Filter> filtersList;
	private FiltersListManagementView filtersListManagementPanel;

	
	public CheckingManagerObserver(JList<Filter> filtersList, JTabbedPane tabsPanel, FiltersListManagementView filtersListManagementPanel) {
		this.filtersList = filtersList;
		this.tabsPanel = tabsPanel;
		this.filtersListManagementPanel = filtersListManagementPanel;
	}

	
	@Override
	public void update(Observable observable, Object filter) {
		FiltersCheckingManager manager = (FiltersCheckingManager) observable;
		
		switch(manager.getState()){
			case FiltersCheckingManager.STATE_FILTER_CHECKED:
				addNewTabWithCheckedFilterData((Filter) filter);
				removeCheckedFilterFromList();
			break;
			
			case FiltersCheckingManager.STATE_FINISHED_CHECKING:
				filtersListManagementPanel.setButtonsToInitState();
			break;
		}
	}
	
	
	private void addNewTabWithCheckedFilterData(Filter checkedFilter) {
		addCloseableTabToTabsPanel(checkedFilter.getOemNumber(), new CheckedFilterTab(checkedFilter));
	}

	
	private void addCloseableTabToTabsPanel(String title, JPanel checkedFilterTab) {
		tabsPanel.addTab(title, checkedFilterTab);
		TabTitle tabTitle = new TabTitle(title, tabsPanel);
		tabsPanel.setTabComponentAt(tabsPanel.getTabCount()-1, tabTitle);
	}

	
	private void removeCheckedFilterFromList() {
		DefaultListModel<Filter> listModel = (DefaultListModel<Filter>) filtersList.getModel();
		listModel.removeElementAt(0);
	}
}
