package views;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import models.Filter;
import models.FiltersListModel;

public class StartScreen extends JPanel {
	private static final long serialVersionUID = 5710757420934143060L;
	private JTabbedPane tabsPanel;
	private FiltersListManagementView filtersListManagementPanel;
	private FiltersListModel filtersListModel;
	private JList<Filter> filtersList;
	private ConnectionInformationView infoTextPane;
	private JScrollPane filtersListScroll;
	private JScrollPane infoScroll;
	
	/* Prepare StartScreen JPanel */
	public StartScreen(JTabbedPane tabsPanel) {
		super(new BorderLayout());
		this.tabsPanel = tabsPanel;
		
		createAndPrepareViews();
		createMainPanelToHoldAllViewsAndAddItToStartScreen();
	}
	
	
	private void createAndPrepareViews() {
		createFiltersListWithModelAndWrapItInScroll();
		createConnectionInformationView();
		createFiltersListManagementView();
	}

	
	private void createFiltersListWithModelAndWrapItInScroll() {
		filtersListModel = new FiltersListModel();
		filtersList = new JList<>(filtersListModel);
		
		filtersListScroll = new JScrollPane(
				filtersList, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
	}
	
	
	private void createFiltersListManagementView() {
		filtersListManagementPanel = new FiltersListManagementView(filtersList, infoTextPane, tabsPanel);
	}
	
	
	private void createConnectionInformationView() {
		infoTextPane = new ConnectionInformationView();
		infoScroll = new JScrollPane(infoTextPane);
	}
	
	
	private void createMainPanelToHoldAllViewsAndAddItToStartScreen() {
		JPanel mainPanel = new JPanel(new GridLayout(1, 3));
		
		mainPanel.add(filtersListScroll);
		mainPanel.add(filtersListManagementPanel);
		mainPanel.add(infoScroll);
		
		this.add(mainPanel, BorderLayout.CENTER);
	}
}
