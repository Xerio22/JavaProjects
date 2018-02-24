package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Filter;
import models.FilterProperty;

public class CheckedFilterTabJTable implements Tabbed {
	private JPanel panel = new JPanel();
	private Filter checkedFilter;
	
	public CheckedFilterTabJTable(Filter checkedFilter) {
		this.checkedFilter = checkedFilter;
		
		createTabGui();
	}

	private void createTabGui() {
		JTable table = new JTable();
		String[] tableHeaders = new String[]{
			"Property name", 
			"Property value"
		};
		
		DefaultTableModel dtm = new DefaultTableModel(tableHeaders, 0);
		table.setModel(dtm);
		Object[] row = new Object[tableHeaders.length];
		
		for(FilterProperty fp : checkedFilter.getProperties()){
			
			row[0] = fp.getPropertyName();
			row[1] = fp.getPropertyValue();
			dtm.addRow(row);
		}
		
		
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(table));
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

}
