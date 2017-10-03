package main;

import java.util.List;

import models.Filter;

public interface FiltersReader {
	public List<Filter> getFiltersAsList() throws Exception;
}
