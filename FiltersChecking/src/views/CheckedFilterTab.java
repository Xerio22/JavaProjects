package views;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import filterscheckers.FilterChecker;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.NewFilterEquivalents;
import models.FilterProperty;
import models.NewFilter;
import utils.Utils;

public class CheckedFilterTab extends JPanel {
	private static final long serialVersionUID = 6214439754839595927L;
	
	private JFXPanel mainPanel = new JFXPanel();
	private final VBox vbox = new VBox();
	private NewFilter filter;
	
	public CheckedFilterTab(NewFilter filter) {
		this.setLayout(new BorderLayout());
		this.filter = filter;
		
		vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
		
		Platform.runLater(new Runnable() { 
            @Override
            public void run() {
            	for(FilterChecker checker : Utils.getFiltersCheckers()){
	            	TableView<NewFilter> tableViewForChecker = createAndSetupTable();
	            	
//        			createColumns(checker.getColumnsNames(), tableViewForChecker);
        			
        			int i = 0;
        			for(NewFilter equivalent : getFilterEquivalents()){
        				if(equivalent.getProperties().get(0).getPropertyName().contains(checker.getCheckerName())){
			    			for(FilterProperty fp : equivalent.getProperties()){
			    				System.out.println(fp);
			        			String propName = fp.getPropertyName();
			        			
			        			((TableColumn<NewFilter, String>)tableViewForChecker
			        					.getColumns().get(i++)).setCellValueFactory(
			        							cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPropertyValueByName(propName)));
			        		}
        				}
        				i = 0;
        			}
	    			
	    			tableViewForChecker.setItems(getFilterEquivalents());
	        		
	        		prepareTablePresentationLayer(checker, tableViewForChecker);
	        		
            	}
            	
            	Scene mainScene = new Scene(vbox);
        		mainPanel.setScene(mainScene);
            }



			private void createColumns(List<String> columnsNames, TableView<NewFilter> tableViewForChecker) {
				for(String columnName : columnsNames){
					TableColumn<NewFilter, String> column = new TableColumn<>(columnName);
					tableViewForChecker.getColumns().add(column);
				}
			}



			private void prepareTablePresentationLayer(FilterChecker checker, TableView<NewFilter> tableViewForChecker) {
				final Label checkerNameLabel = createAndPrepareCheckerNameLabel(checker);
                
                vbox.getChildren().addAll(checkerNameLabel, tableViewForChecker);	
			}
			

			private Label createAndPrepareCheckerNameLabel(FilterChecker checker) {
				final Label checkerNameLabel = new Label(checker.getCheckerName());
                checkerNameLabel.setFont(new Font("Arial", 20));
                
                return checkerNameLabel;
			}


			private TableView<NewFilter> createAndSetupTable() {
				TableView<NewFilter> tableView = new TableView<>();
            	tableView.setEditable(true);
            	tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        	
				return tableView;
			}
			
			
			private ObservableList<NewFilter> getFilterEquivalents() {
				return FXCollections.observableArrayList(filter.getEquivalents());
			}
		});

		this.add(mainPanel);
	}
}
