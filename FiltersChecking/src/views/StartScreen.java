package views;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import controllers.FiltersCheckingManager;
import models.Filter;
import models.FiltersListModel;

public class StartScreen extends JPanel {
	private static final long serialVersionUID = 5710757420934143060L;
	private FiltersListManagementView filtersAddingPanel;
	private JButton startProcessingButton;
	
	/* Prepare StartScreen JPanel */
	public StartScreen(JTabbedPane tabsPanel) {
		super(new BorderLayout());
		JPanel mainPanel = new JPanel(new GridLayout(1, 3));
		
		FiltersListModel filtersListModel = new FiltersListModel();
		JList<Filter> filtersList = new JList<>(filtersListModel);
		JScrollPane filtersListScroll = new JScrollPane(filtersList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		filtersAddingPanel = new FiltersListManagementView(filtersList);
		
		ConnectionInformationView infoTextPane = new ConnectionInformationView();
		JScrollPane infoScroll = new JScrollPane(infoTextPane);
		
		startProcessingButton = new JButton("Szukaj");
		
		startProcessingButton.addActionListener(buttonClicked -> {
			setButtonsEnabled(false);
			
			FiltersCheckingManager filterDataChecker = new FiltersCheckingManager(filtersListModel, tabsPanel, infoTextPane);
			filterDataChecker.startProcessing();
			
			setButtonsEnabled(true);
		});
		
		
		mainPanel.add(filtersListScroll);
		mainPanel.add(filtersAddingPanel);
		mainPanel.add(infoScroll);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(startProcessingButton, BorderLayout.SOUTH);
	}
	
	
	private void setButtonsEnabled(boolean isEnabled){
		startProcessingButton.setEnabled(isEnabled);
		filtersAddingPanel.setButtonsEnabled(isEnabled);
	}
}
