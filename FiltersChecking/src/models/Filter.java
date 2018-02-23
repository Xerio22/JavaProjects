package models;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Filter {
	private List<FilterProperty> filterProperties = new ArrayList<>();
	private String oemNumberTagName = "OEM_Number";

	
	public static Filter createFilterUsingOEMnumber(String OEMnumber) {
		return new Filter(OEMnumber);
	}
	
	private Filter(String OEMnumber){
		this.addProperty(oemNumberTagName, OEMnumber);
	}
	
	private void addProperty(String propertyName, String propertyValue){
		FilterProperty fp = new FilterProperty(propertyName, propertyValue);
		this.addPropertyWithoutWhiteSpaces(fp);
	}
	
	private void addPropertyWithoutWhiteSpaces(FilterProperty property){
		property.getRidOfWhiteSpacesFromPropertyName();
		this.filterProperties.add(property);
	}
	
	
	public static Filter createFilterFromXmlElement(Element zamiennik, String oemNumberTagName) {
		return new Filter(zamiennik, oemNumberTagName);
	}
	
	private Filter(Element zamiennik, String oemNumberTagName) {
		this.oemNumberTagName = oemNumberTagName;
		
		NodeList zamiennikPropertiesList = zamiennik.getChildNodes();

		for (int j = 0; j < zamiennikPropertiesList.getLength(); j++) {
			Node propertyNode = zamiennikPropertiesList.item(j);

			if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
				Element property = (Element) propertyNode;

				FilterProperty fp = new FilterProperty(property);
				filterProperties.add(fp);
			}
		}
	}
	
	
	public static Filter createFilterFromProperties(List<FilterProperty> filterProperties) {
		return new Filter(filterProperties);
	}
	
	private Filter(List<FilterProperty> filterProperties) {
		this.filterProperties = filterProperties;
	}

	
	public void adjustEquivalentsNumerationToFitThoseFromFilter(Filter filter) {
		int numberOfLastEquivalent = filter.getNumberOfLastEquivalent();
		int equivalentNewNumber = numberOfLastEquivalent + 1;
		
		List<FilterProperty> props = new ArrayList<>();
		
		int prev = 1;
		for(FilterProperty fp : this.getProperties()){
			if(!fp.getPropertyName().equals("OEM_Number")){
				
				if(!fp.getPropertyName().contains(prev+"")){
					prev++;
					equivalentNewNumber++;
				}
				
				FilterProperty ff = new FilterProperty(fp.getPropertyName().replaceAll(prev+"", equivalentNewNumber + ""), fp.getPropertyValue());
				props.add(ff);
			}
		}
		
		this.replaceProperties(props);
	}
	
	public int getNumberOfLastEquivalent() {
		if(this.getProperties().size() == 1){
			return 0;
		}
		
		String equivalentNumberString = this.getProperties()
										    .get(this.getProperties().size()-1)
										    .getPropertyName()
										    .split("_")[2];
		
		return Integer.parseInt(equivalentNumberString);
	}
	
	
	public void addEquivalents(FilterEquivalents equivalents) {
		for(Filter equivalent : equivalents.getEquivalents()) {
			this.addEquivalent(equivalent);
		}
	}
	
	public void addEquivalent(Filter newEquivalent) {
		this.addProperties(newEquivalent.getProperties());
	}
	
	private void addProperties(List<FilterProperty> properties) {
		for(FilterProperty fp : properties){
			this.addProperty(fp);
		}
	}

	private void addProperty(FilterProperty property){
		this.filterProperties.add(property);
	}
	
	
	public List<FilterProperty> getProperties() {
		return filterProperties;
	}


	public String getOemNumber() {
		return getPropertyValueByName(oemNumberTagName);
	}
	
	public String getPropertyValueByName(String tagName) {
		for(FilterProperty fp : filterProperties){
			if(fp.getPropertyName().equals(tagName)){
				return fp.getPropertyValue();
			}
		}
		return null;
	}
	
	
	public void replaceProperties(List<FilterProperty> props) {
		this.filterProperties = props;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(FilterProperty fp : filterProperties){
			sb.append(fp).append("     ");
		}
		
		return sb.toString().trim();
	}
}