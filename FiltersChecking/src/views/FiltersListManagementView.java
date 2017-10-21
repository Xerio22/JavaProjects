package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import controllers.CheckingManagerObserver;
import controllers.EnablingButtonsOnListChangeListener;
import controllers.FiltersCheckingManager;
import models.Filter;
import models.FiltersListModel;

public class FiltersListManagementView extends JPanel {
	private static final long serialVersionUID = -5756954534235195566L;
	
	private JList<Filter> filtersList;
	private FiltersListModel filtersListModel;
	private JButton addFilterToList = new JButton("Dodaj");
	private JButton removeFilterFromList = new JButton("Usuń");
	private JLabel filterOEMLabel = new JLabel("Brand name (OEM): "); 
	private JTextField filterOEM = new JTextField(15);
	private JLabel filterOEMnumberLabel = new JLabel("OEM number: "); 
	private JTextField filterOEMnumber = new JTextField(15);
	private JButton startProcessingButton = new JButton("Szukaj");

	private JPanel inputsPanel;
	private ConnectionInformationView infoTextPane;
	private JTabbedPane tabsPanel;
	
	public FiltersListManagementView(JList<Filter> filtersList, ConnectionInformationView infoTextPane, JTabbedPane tabsPanel) {
		this.filtersList = filtersList;
		this.infoTextPane = infoTextPane;
		this.tabsPanel = tabsPanel;
		this.filtersListModel = (FiltersListModel) filtersList.getModel();
		
		addActionListeners();
		arrangePanel();
	}
	
	
	private void addActionListeners() {
		addActionListenersToButtons();
		
		filtersListModel.addListDataListener(
				new EnablingButtonsOnListChangeListener(
						filtersList, 
						new ArrayList<>(Arrays.asList(startProcessingButton, removeFilterFromList))
				)
		);
	}
	

	private void addActionListenersToButtons() {
		addActLsnForAddBtn();
		addActLsnForRmvBtn();
		addActLsnForStartProcessingButton();
	}


	private void addActLsnForAddBtn() {
		addFilterToList.addActionListener(buttonClicked -> {
			String brandName = getBrandName();
			String OEMnumber = getOemNumber();
			clearInputFields();
			
			Filter newFilter = Filter.createFilterUsingBrandNameAndOEMnumber(brandName, OEMnumber);
			
			filtersListModel.addElement(newFilter);
			
			filterOEM.requestFocus();
		});
	}


	private String getBrandName() {
		return filterOEM.getText();
	}
	
	
	private String getOemNumber() {
		return filterOEMnumber.getText();
	}

	
	private void clearInputFields() {
		filterOEM.setText("");
		filterOEMnumber.setText("");
	}
	
	
	private void addActLsnForRmvBtn() {
		removeFilterFromList.addActionListener(buttonClicked -> {
			int selectedIndex = filtersList.getSelectedIndex();
			removeFilterFromList(selectedIndex);
		});
	}

	
	private void removeFilterFromList(int selectedIndex) {
		filtersListModel.remove(selectedIndex);
	}

	
	private void addActLsnForStartProcessingButton() {
		setButtonsToInitState();
		
		startProcessingButton.addActionListener(buttonClicked -> {
			setButtonsEnabled(false);
			try{
				FiltersCheckingManager filterDataChecker = new FiltersCheckingManager(filtersListModel, infoTextPane);
				filterDataChecker.addObserver(new CheckingManagerObserver(filtersList, tabsPanel, this));
				
				filterDataChecker.startProcessing();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		});
	}
	
	
	private void arrangePanel() {
		this.setLayout(new GridLayout(2, 1));
		
		JPanel northInputsPanel = new JPanel();
		addComponentsToInputPanel();
		northInputsPanel.add(inputsPanel);
		
		JPanel southButtonPanel = new JPanel(new BorderLayout());
		southButtonPanel.add(startProcessingButton, BorderLayout.SOUTH);
		
		this.add(northInputsPanel);
		this.add(southButtonPanel);
	}

	
	private void addComponentsToInputPanel() {
		addInputComponentsToPanel();
		addButtonsToPanel();
	}

	
	private void addInputComponentsToPanel() {
		JPanel fieldsLabelsPanel = new JPanel(new GridLayout(3, 1));
		fieldsLabelsPanel.add(filterOEMLabel);
		fieldsLabelsPanel.add(new JPanel());
		fieldsLabelsPanel.add(filterOEMnumberLabel);
		
		JPanel textFieldsPanel = new JPanel(new GridLayout(3, 1));
		textFieldsPanel.add(filterOEM);
		textFieldsPanel.add(new JPanel());
		textFieldsPanel.add(filterOEMnumber);
		
		JPanel inputs = new JPanel(new GridLayout(1, 2));
		inputs.add(fieldsLabelsPanel);
		inputs.add(textFieldsPanel);
		
		inputsPanel = new JPanel(new GridLayout(2,1));
		inputsPanel.add(inputs);
	}
	
	
	private void addButtonsToPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		
		buttonsPanel.add(addFilterToList);
		buttonsPanel.add(removeFilterFromList);
		
		inputsPanel.add(buttonsPanel);
	}
	
	
	public void setButtonsEnabled(boolean isEnabled){
		this.addFilterToList.setEnabled(isEnabled);
		this.removeFilterFromList.setEnabled(isEnabled);
		this.startProcessingButton.setEnabled(isEnabled);
	}


	public void disableButtonsOnInit() {
		this.removeFilterFromList.setEnabled(false);
		this.startProcessingButton.setEnabled(false);
	}


	public void setButtonsToInitState() {
		this.addFilterToList.setEnabled(true);
		this.removeFilterFromList.setEnabled(false);
		this.startProcessingButton.setEnabled(false);
	}
}
