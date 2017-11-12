package models;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NewFilter {
	private List<FilterProperty> filterProperties = new ArrayList<>();
	private String oemNumberTagName = "OEM_Number";
	
	private NewFilterEquivalents equivalents = new NewFilterEquivalents();

	
	private NewFilter(String OEMnumber){
		this.addProperty(oemNumberTagName, OEMnumber);
	}
	
	
	public static NewFilter createFilterUsingOEMnumber(String OEMnumber) {
		return new NewFilter(OEMnumber);
	}
	
	
	private NewFilter(Element zamiennik, String oemNumberTagName) {
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
	
	
	public static NewFilter createFilterFromXmlElement(Element zamiennik, String oemNumberTagName) {
		return new NewFilter(zamiennik, oemNumberTagName);
	}
	
	
	private NewFilter(List<FilterProperty> filterProperties) {
		this.filterProperties = filterProperties;
	}
	
	
	public static NewFilter createFilterFromProperties(List<FilterProperty> filterProperties) {
		return new NewFilter(filterProperties);
	}

	
	public String getPropertyValueByName(String tagName) {
		for(FilterProperty fp : filterProperties){
			if(fp.getPropertyName().equals(tagName)){
				return fp.getPropertyValue();
			}
		}
		return null;
	}

	
	public void addEquivalents(NewFilterEquivalents equivalents) {
		this.equivalents.addAll(equivalents);
	}
	
	
	public void addEquivalent(NewFilter newEquivalent) {
		this.equivalents.add(newEquivalent);
	}
	

	public void addProperty(String propertyName, String propertyValue){
		FilterProperty fp = new FilterProperty(propertyName, propertyValue);
		this.addPropertyWithoutWhiteSpaces(fp);
	}
	
	
	private void addPropertyWithoutWhiteSpaces(FilterProperty property){
		property.getRidOfWhiteSpaces();
		this.filterProperties.add(property);
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(FilterProperty fp : filterProperties){
			sb.append(fp).append("     ");
		}
		
		for(NewFilter f : equivalents){
			for(FilterProperty fp : f.getProperties()){
				sb.append(fp).append("     ");
			}
		}
		return sb.toString().trim();
	}


	public List<FilterProperty> getProperties() {
		return filterProperties;
	}

	public String getOemNumber() {
		return this.getPropertyValueByName(oemNumberTagName);
	}

	public NewFilterEquivalents getEquivalents() {
		return this.equivalents;
	}
}
