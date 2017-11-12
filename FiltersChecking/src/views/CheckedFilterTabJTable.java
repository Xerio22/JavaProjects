package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Filter;
import models.FilterProperty;

public class CheckedFilterTab extends JPanel {
	private static final long serialVersionUID = 7159948324475908470L;
	private Filter checkedFilter;
	
	public CheckedFilterTab(Filter checkedFilter) {
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
		
		
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table));
	}

}
