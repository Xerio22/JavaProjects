package views;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	private JPanel everythingPanel;
	
	public FiltersListManagementView(JList<Filter> filtersList) {
		this.filtersList = filtersList;
		this.filtersListModel = (FiltersListModel) filtersList.getModel();
		
		addActionListeners();
		arrangePanel();
	}
	
	
	private void addActionListeners() {
		addActLsnForAddBtn();
		addActLsnForRmvBtn();
	}

	
	private void addActLsnForAddBtn() {
		addFilterToList.addActionListener(buttonClicked -> {
			String brandName = getBrandName();
			String OEMnumber = getOemNumber();
			
			Filter newFilter = Filter.createFilterUsingBrandNameAndOEMnumber(brandName, OEMnumber);
			
			filtersListModel.addElement(newFilter);
		});
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


	private String getBrandName() {
		return filterOEM.getText();
	}
	
	
	private String getOemNumber() {
		return filterOEMnumber.getText();
	}
	
	
	private void arrangePanel() {
		everythingPanel = new JPanel(new GridLayout(2,1));
		
		addComponentsToEverythingPanel();
		
		this.add(everythingPanel);
	}

	
	private void addComponentsToEverythingPanel() {
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
		
		everythingPanel.add(inputs);
	}
	
	
	private void addButtonsToPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		
		buttonsPanel.add(addFilterToList);
		buttonsPanel.add(removeFilterFromList);
		
		everythingPanel.add(buttonsPanel);
	}
	
	
	public void setButtonsEnabled(boolean isEnabled){
		this.addFilterToList.setEnabled(isEnabled);
		this.removeFilterFromList.setEnabled(isEnabled);
	}
}
