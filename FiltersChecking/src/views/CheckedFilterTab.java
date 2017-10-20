package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Filter;

public class CheckedFilterTab extends JPanel {
	private static final long serialVersionUID = 7159948324475908470L;
	private Filter checkedFilter;
	
	public CheckedFilterTab(Filter checkedFilter) {
		this.checkedFilter = checkedFilter;
		
		createTabGui();
	}

	private void createTabGui() {
		JTable table = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(new String[]{"col1", "col2"}, 1);
		table.setModel(dtm);
		
		this.setLayout(new BorderLayout());
		this.add(table);
	}

}
