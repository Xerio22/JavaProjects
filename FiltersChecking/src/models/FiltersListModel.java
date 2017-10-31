package models;

import java.util.List;

import javax.swing.DefaultListModel;

public class FiltersListModel extends DefaultListModel<Filter> {
	private static final long serialVersionUID = 5686217210155210320L;

	public void addAll(List<Filter> list) {
		for(Filter filter : list) {
			this.addElement(filter);
		}
	}
}
