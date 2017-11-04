package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterEquivalents {
	private List<Filter> equivalents = new ArrayList<>();
	
	public void addEquivalent(Filter equivalent) {
		equivalents.add(equivalent);
	}
	
	public List<Filter> getEquivalents() {
		return equivalents;
	}

	public void createAndAddEquivalent(String checkerName, String equivalentOEM, String equvalentBrand, String equivalentNumber, int propIdx, String[]... additionalPropertiesWithNames) {
		Filter newEquivalent = createEquivalent(checkerName, equivalentOEM, equvalentBrand, equivalentNumber, propIdx, additionalPropertiesWithNames);
		this.addEquivalent(newEquivalent);
	}
	
	private Filter createEquivalent(String checkerName, String equivalentOEM, String equvalentBrand, String equivalentNumber, int propIdx, String[]... additionalPropertiesWithNames){
		// Create new properties with brand and number values and add them to list of replacement's properties
		FilterProperty searchResultOEMnameProperty = new FilterProperty(prepareFullPropertyNameForEquivalent(checkerName, "OEM", propIdx), equivalentOEM);
		FilterProperty equivalentBrandProperty = new FilterProperty(prepareFullPropertyNameForEquivalent(checkerName, "brand", propIdx), equvalentBrand);
		FilterProperty equivalentNumberProperty = new FilterProperty(prepareFullPropertyNameForEquivalent(checkerName, "replacement", propIdx), equivalentNumber);
		
		List<FilterProperty> properties = new ArrayList<>();
		properties.add(searchResultOEMnameProperty);
		properties.add(equivalentBrandProperty);
		properties.add(equivalentNumberProperty);
		
		if(additionalPropertiesWithNames != null){
			properties.addAll(createAdditionalPropertiesFromNameValueArray(additionalPropertiesWithNames, checkerName, propIdx));
		}
		System.out.println(properties);
		Filter equivalent = Filter.createFilterFromProperties(properties);
		
		return equivalent;
	}

	private String prepareFullPropertyNameForEquivalent(String checkerName, String propertyName, int propIdx){
		return checkerName + "_equiv_" + propIdx + "_" + propertyName;
	}
	
	private List<FilterProperty> createAdditionalPropertiesFromNameValueArray(String[][] additionalPropertiesWithNames, String checkerName, int propIdx) {
		List<FilterProperty> properties = new ArrayList<>();
		
		for(int i = 0; i < additionalPropertiesWithNames.length; i++){
			FilterProperty prop = new FilterProperty(
					prepareFullPropertyNameForEquivalent(checkerName, additionalPropertiesWithNames[i][0], propIdx), 
					additionalPropertiesWithNames[i][1]
			);
			
			properties.add(prop);
		}
		
		return properties;
	}
}
