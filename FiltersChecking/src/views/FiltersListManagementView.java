package views;

import javax.swing.JButton;
import javax.swing.JPanel;

public class FiltersListManagementView extends JPanel {
	private static final long serialVersionUID = -5756954534235195566L;
	private JButton addFilterToList;
	private JButton removeFilterFromList;
	
	public FiltersListManagementView() {
		addFilterToList = new JButton("Dodaj");
		removeFilterFromList = new JButton("Usuń");
		
		addActionListeners();
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
	
	
	public void setButtonsEnabled(boolean isEnabled){
		this.addFilterToList.setEnabled(isEnabled);
		this.removeFilterFromList.setEnabled(isEnabled);
	}
}
