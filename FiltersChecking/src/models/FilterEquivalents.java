package models;

import java.util.ArrayList;
import java.util.List;

public class FilterEquivalents {
	private List<Filter> equivalents = new ArrayList<>();
	
	public void addEquivalent(Filter equivalent) {
		equivalents.add(equivalent);
	}
	
	public List<Filter> getEquivalents() {
		return equivalents;
	}

	public void createAndAddEquivalent(String searchResultOEM, String equvalentBrand, String equivalentNumber, int propIdx) {
		Filter newEquivalent = createEquivalent(searchResultOEM, equvalentBrand, equivalentNumber, propIdx);
		this.addEquivalent(newEquivalent);
	}
	
	private Filter createEquivalent(String searchResultOEM, String equvalentBrand, String equivalentNumber, int propIdx){
		// Create new properties with brand and number values and add them to list of replacement's properties
		FilterProperty searchResultOEMnameProperty = new FilterProperty("equiv_" + propIdx + "_OEM", searchResultOEM);
		FilterProperty equivalentBrandProperty = new FilterProperty("equiv_" + propIdx + "_brand", equvalentBrand);
		FilterProperty equivalentNumberProperty = new FilterProperty("equiv_" + propIdx + "_replacement", equivalentNumber);
		
		List<FilterProperty> properties = new ArrayList<>();
		properties.add(searchResultOEMnameProperty);
		properties.add(equivalentBrandProperty);
		properties.add(equivalentNumberProperty);
		
		Filter equivalent = Filter.createFilterFromProperties(properties);
		
		return equivalent;
	}
}
