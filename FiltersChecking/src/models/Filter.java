package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.Utils;

public class Filter {
	private List<FilterProperty> filterProperties = new ArrayList<>();

	private Filter(String brandName, String OEMnumber){
		this.addProperty("Brand", brandName);
		this.addProperty("OEM", OEMnumber);
	}
	
	public static Filter createFilterUsingBrandNameAndOEMnumber(String brandName, String OEMnumber) {
		return new Filter(brandName, OEMnumber);
	}
	
	private Filter(Filter filter){
		for (FilterProperty prop : filter.filterProperties) {
			FilterProperty filterProperty = new FilterProperty(prop.getPropertyName(), prop.getPropertyValue());
			this.filterProperties.add(filterProperty);
		}
	}
	
	public static Filter createFilterUsingExistingOne(Filter filter) {
		return new Filter(filter);
	}
	
	
	private Filter(Element zamiennik) {
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
	
	public static Filter createFilterFromXmlElement(Element zamiennik) {
		return new Filter(zamiennik);
	}
	
	
	
	public Filter(List<FilterProperty> filterProperties) {
		this.filterProperties = filterProperties;
	}

	
	public String getValueOfTag(String tagName) {
		for(FilterProperty fp : filterProperties){
			if(fp.getPropertyName().equals(tagName)){
				return fp.getPropertyValue();
			}
		}
		return null;
	}
	
	
	public void setEquivalents(FilterEquivalents equivalents) {
		for(Filter equivalent : equivalents.getEquivalents()) {
			this.addEquivalent(equivalent);
		}
	}
	
	public void addEquivalent(Filter newEquivalent) {
		this.addProperties(newEquivalent.getProperties());
	}
	
	public void addProperties(List<FilterProperty> properties) {
		for(FilterProperty fp : properties){
			this.addProperty(fp);
		}
	}

	public void addProperty(FilterProperty property){
		this.filterProperties.add(property);
	}
	
	public void addPropertyWithoutWhiteSpaces(FilterProperty property){
		property.getRidOfWhiteSpaces();
		this.filterProperties.add(property);
	}
	
	public void addProperty(String propertyName, String propertyValue){
		FilterProperty fp = new FilterProperty(propertyName, propertyValue);
		this.addPropertyWithoutWhiteSpaces(fp);
	}
	
	
	@SuppressWarnings("unused")
	private boolean propertyContainsWhiteSpaces(FilterProperty property) {
		return property.getPropertyName().contains("\\s") || 
			   property.getPropertyValue().contains("\\s");
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(FilterProperty fp : filterProperties){
			if(fp.isValuable()){
				sb.append(fp).append(Utils.newLine);
			}
		}
		
		return sb.toString();
	}


	public List<FilterProperty> getProperties() {
		return filterProperties;
	}


	public void getRidOfLeadingZeros() {
		String name = this.getValueOfTag(Utils.obcy_skrot);
		
		String nameWithoutZeros = null;
		Pattern p = Pattern.compile("(0*)(.*)");
		Matcher m = p.matcher(name);
		
		if(m.matches()){
			nameWithoutZeros = m.group(2);
		}
	
		
		for(int i = 0; i < filterProperties.size(); i++){
			if(filterProperties.get(i).getPropertyValue().equals(name)){
				filterProperties.get(i).setPropertyValue(nameWithoutZeros);
			}
		}
	}
}




//public Filter(List<Element> properties) {
//for (Element prop : properties) {
//	FilterProperty filterProperty = new FilterProperty(prop);
//	filterProperties.add(filterProperty);
//}
//}
