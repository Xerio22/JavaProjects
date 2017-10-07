package filtersreaders;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;

import models.Filter;

public class FiltersReaderFromListModel implements FiltersReader {
	private ListModel<Filter> filtersListModel;
	
	public FiltersReaderFromListModel(ListModel<Filter> filtersListModel) {
		this.filtersListModel = filtersListModel;
	}
	
	@Override
	public List<Filter> getFiltersAsList() {
		List<Filter> filters = new ArrayList<>();
		
		for(int i = 0; i < filtersListModel.getSize(); i++){
			filters.add(filtersListModel.getElementAt(i));
		}
		return filters;
	}

}
