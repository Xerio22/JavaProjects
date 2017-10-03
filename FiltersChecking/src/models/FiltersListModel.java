package models;

import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;

public class FiltersListModel extends DefaultListModel<Filter> {
	private static final long serialVersionUID = 5686217210155210320L;

	public List<Filter> getFiltersAsList() {
		return Arrays.asList((Filter[])this.toArray());	
	}
}
