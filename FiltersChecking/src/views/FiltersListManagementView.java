package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import controllers.EnablingButtonsOnListChangeListener;
import controllers.FiltersCheckingManager;
import filtersreaders.FiltersReader;
import filtersreaders.FiltersReaderFromListModel;
import filtersreaders.FiltersReaderFromTxt;
import filtersreaders.FiltersReaderFromXml;
import utils.XmlTxtFilter;
import models.Filter;
import models.FiltersListModel;
import observers.CheckingManagerObserver;
import utils.FileLoader;
import utils.Utils;
import utils.XmlStructureProvider;

public class FiltersListManagementView extends JPanel {
	private static final long serialVersionUID = -5756954534235195566L;
	
	private JList<Filter> filtersList;
	private FiltersListModel filtersListModel;
	private JButton addFilterToListButton = new JButton("Dodaj");
	private JButton removeFilterFromListButton = new JButton("Usuń");
	private JButton readFiltersFromFileButton = new JButton("Wczytaj z pliku");
	private JLabel filterOEMLabel = new JLabel("Brand name (OEM): "); 
	private JTextField filterOEMField = new JTextField(15);
	private JLabel filterOEMnumberLabel = new JLabel("OEM number: "); 
	private JTextField filterOEMnumberField = new JTextField(15);
	private JButton startProcessingButton = new JButton("Szukaj");
	private FiltersReader filtersReaderFromFile;

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
		addActionListenersToOtherComponents();
	}


	private void addActionListenersToButtons() {
		addActLsnForOEMnrLabel();
		addActLsnForAddBtn();
		addActLsnForRmvBtn();
		addActLsnForReadFromFileBtn();
		addActLsnForStartProcessingButton();
	}


	private void addActLsnForOEMnrLabel() {
		filterOEMnumberField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				if(isFieldNotEmpty(filterOEMnumberField)){
					addFilterToListButton.setEnabled(true);
				}
				else{
					addFilterToListButton.setEnabled(false);
				}
			}

			private boolean isFieldNotEmpty(JTextField filterOEMnumber) {
				return !filterOEMnumber.getText().trim().isEmpty();
			}
		});
	}


	private void addActLsnForAddBtn() {
		addFilterToListButton.addActionListener(buttonClicked -> {
			String brandName = getBrandName();
			String OEMnumber = getOemNumber();
			clearInputFields();
			
			Filter newFilter = Filter.createFilterUsingBrandNameAndOEMnumber(brandName, OEMnumber);
			
			filtersListModel.addElement(newFilter);
			
			filterOEMnumberField.requestFocus();
		});
	}


	private String getBrandName() {
		return filterOEMField.getText();
	}
	
	
	private String getOemNumber() {
		return filterOEMnumberField.getText();
	}

	
	private void clearInputFields() {
		filterOEMField.setText("");
		filterOEMnumberField.setText("");
	}
	
	
	private void addActLsnForRmvBtn() {
		removeFilterFromListButton.addActionListener(buttonClicked -> {
			removeSelectedFilterFromList();
		});
	}

	
	private void removeSelectedFilterFromList() {
		int selectedIndex = filtersList.getSelectedIndex();
		filtersListModel.remove(selectedIndex);
	}
	
	
	private void addActLsnForReadFromFileBtn() {
		readFiltersFromFileButton.addActionListener(buttonClicked -> {
			FileLoader fileLoader = new FileLoader(new XmlTxtFilter());
			File file = fileLoader.loadFile();
			
			String extension = Utils.getExtension(file);
			
			if(extension.equals(Utils.xml)){
				XmlStructureProvider xsp = new XmlStructureProvider();
				xsp.getXmlStructureUsingDialog();
				filtersReaderFromFile = new FiltersReaderFromXml(file, xsp);
			}
			else {
				filtersReaderFromFile = new FiltersReaderFromTxt(file); 
			}
			
			filtersListModel.addAll(filtersReaderFromFile.getFiltersAsList());
		});
	}

	
	private void addActLsnForStartProcessingButton() {
		setButtonsToInitState();
		
		FiltersCheckingManager filterDataChecker = getProperCheckingManager();
		filterDataChecker.addObserver(new CheckingManagerObserver(filtersList, tabsPanel, this));
		
		startProcessingButton.addActionListener(buttonClicked -> {
			setButtonsEnabled(false);
			
			try{
				filterDataChecker.startProcessing();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		});
	}
	
	
	private FiltersCheckingManager getProperCheckingManager() {		
		if(filtersReaderFromFile != null){
			return new FiltersCheckingManager(filtersReaderFromFile, infoTextPane);
		}
		else{
			return new FiltersCheckingManager(new FiltersReaderFromListModel(filtersListModel), infoTextPane);
		}
	}
	
	
	private void addActionListenersToOtherComponents() {
		filtersListModel.addListDataListener(
				new EnablingButtonsOnListChangeListener(
						filtersList, 
						new ArrayList<>(Arrays.asList(startProcessingButton, removeFilterFromListButton))
				)
		);
		
		filterOEMnumberField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					addFilterToListButton.doClick();
				}
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
		textFieldsPanel.add(filterOEMField);
		textFieldsPanel.add(new JPanel());
		textFieldsPanel.add(filterOEMnumberField);
		
		JPanel inputs = new JPanel(new GridLayout(1, 2));
		inputs.add(fieldsLabelsPanel);
		inputs.add(textFieldsPanel);
		
		inputsPanel = new JPanel(new GridLayout(2,1));
		inputsPanel.add(inputs);
	}
	
	
	private void addButtonsToPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		buttonsPanel.add(addFilterToListButton);
		buttonsPanel.add(removeFilterFromListButton);
		buttonsPanel.add(readFiltersFromFileButton);
		
		inputsPanel.add(buttonsPanel);
	}
	
	
	public void setButtonsEnabled(boolean isEnabled){
		this.addFilterToListButton.setEnabled(isEnabled);
		this.removeFilterFromListButton.setEnabled(isEnabled);
		this.readFiltersFromFileButton.setEnabled(isEnabled);
		this.startProcessingButton.setEnabled(isEnabled);
	}


	public void setButtonsToInitState() {
		this.addFilterToListButton.setEnabled(false);
		this.removeFilterFromListButton.setEnabled(false);
		this.readFiltersFromFileButton.setEnabled(true);
		this.startProcessingButton.setEnabled(false);
	}
}
