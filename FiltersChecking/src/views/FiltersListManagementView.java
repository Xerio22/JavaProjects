package views;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FiltersListManagementView extends JPanel {
	private static final long serialVersionUID = -5756954534235195566L;
	private JButton addFilterToList;
	private JButton removeFilterFromList;
	private JLabel filterOEMLabel;
	private JTextField filterOEM;
	private JLabel filterOEMnumberLabel;
	private JTextField filterOEMnumber;
	
	private JPanel everythingPanel;
	
	public FiltersListManagementView() {
		filterOEMLabel = new JLabel("Brand name (OEM): "); 
		filterOEM = new JTextField(15);
		filterOEMnumberLabel = new JLabel("OEM number: "); 
		filterOEMnumber = new JTextField(15);
		addFilterToList = new JButton("Dodaj");
		removeFilterFromList = new JButton("Usuń");

		addActionListeners();
		arrangePanel();
	}
	
	
	private void addActionListeners() {
		addActLsnForAddBtn();
		addActLsnForRmvBtn();
	}

	
	private void addActLsnForAddBtn() {
		addFilterToList.addActionListener(buttonClicked -> {
			
		});
	}
	
	
	private void addActLsnForRmvBtn() {
		removeFilterFromList.addActionListener(buttonClicked -> {
			
		});
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
