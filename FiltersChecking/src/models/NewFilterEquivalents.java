package models;

import java.util.ArrayList;
import java.util.List;

public class NewFilterEquivalents extends ArrayList<NewFilter>{
	private static final long serialVersionUID = 3871295183697752767L;

	public void createAndAddEquivalent(String checkerName, String equivalentOEM, String equvalentBrand, String equivalentNumber, int propIdx, String[]... additionalPropertiesWithNames) {
		NewFilter newEquivalent = createEquivalent(checkerName, equivalentOEM, equvalentBrand, equivalentNumber, propIdx, additionalPropertiesWithNames);
		this.add(newEquivalent);
	}
	
	private NewFilter createEquivalent(String checkerName, String equivalentOEM, String equvalentBrand, String equivalentNumber, int propIdx, String[]... additionalPropertiesWithNames){
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
		
		NewFilter equivalent = NewFilter.createFilterFromProperties(properties);
		
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
