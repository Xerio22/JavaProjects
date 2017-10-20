package views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import controllers.EnablingButtonOnListChangeListener;
import controllers.FiltersCheckingManager;
import controllers.FiltersCheckingObserver;
import models.Filter;
import models.FiltersListModel;

public class StartScreen extends JPanel {
	private static final long serialVersionUID = 5710757420934143060L;
	private JTabbedPane tabsPanel;
	private FiltersListManagementView filtersListManagementPanel;
	private JButton startProcessingButton = new JButton("Szukaj");
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
		createFiltersListManagementView();
		createConnectionInformationView();
		createAndConfigureStartProcessingButton();
	}

	
	private void createFiltersListWithModelAndWrapItInScroll() {
		filtersListModel = new FiltersListModel();
		filtersList = new JList<>(filtersListModel);
		
		filtersListModel.addListDataListener(new EnablingButtonOnListChangeListener(filtersListModel, startProcessingButton));
		
		filtersListScroll = new JScrollPane(
				filtersList, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
	}
	
	
	private void createFiltersListManagementView() {
		filtersListManagementPanel = new FiltersListManagementView(filtersList);
	}
	
	
	private void createConnectionInformationView() {
		infoTextPane = new ConnectionInformationView();
		infoScroll = new JScrollPane(infoTextPane);
	}
	
	
	private void createAndConfigureStartProcessingButton() {
		startProcessingButton.setEnabled(false);
		
		startProcessingButton.addActionListener(buttonClicked -> {
			setButtonsEnabled(false);
			try{
			FiltersCheckingManager filterDataChecker = new FiltersCheckingManager(filtersListModel, infoTextPane);
			filterDataChecker.addObserver(new FiltersCheckingObserver(filtersList, tabsPanel));
			
			filterDataChecker.startProcessing();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			setButtonsEnabled(true);
		});
	}


	private void setButtonsEnabled(boolean isEnabled){
		startProcessingButton.setEnabled(isEnabled);
		// buttons disable properly but in result of swing worker 
		// and processing in background they instantly are enabled
		filtersListManagementPanel.setButtonsEnabled(isEnabled);
	}
	
	
	private void createMainPanelToHoldAllViewsAndAddItToStartScreen() {
		JPanel mainPanel = new JPanel(new GridLayout(1, 3));
		
		mainPanel.add(filtersListScroll);
		mainPanel.add(filtersListManagementPanel);
		mainPanel.add(infoScroll);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(startProcessingButton, BorderLayout.SOUTH);
	}
}
