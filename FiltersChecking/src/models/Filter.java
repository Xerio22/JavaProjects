package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.Utils;

public class Filter {
	private List<FilterProperty> filterProperties = new ArrayList<>();

	public Filter(Filter filter){
		for (FilterProperty prop : filter.filterProperties) {
			FilterProperty filterProperty = new FilterProperty(prop.getPropertyName(), prop.getPropertyValue());
			filterProperties.add(filterProperty);
		}
	}
	
	
	public Filter(Element zamiennik) {
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
	
	
	public void addProperty(FilterProperty property){
		property.getRidOfWhiteSpaces();
		this.filterProperties.add(property);
	}
	
	
	@SuppressWarnings("unused")
	private boolean propertyContainsWhiteSpaces(FilterProperty property) {
		return property.getPropertyName().contains("\\s") || 
			   property.getPropertyValue().contains("\\s");
	}


	public void addProperty(String propertyName, String propertyValue){
		FilterProperty fp = new FilterProperty(propertyName, propertyValue);
		this.addProperty(fp);
	}
	
	
	public void addProperties(List<FilterProperty> oemReplacementsProperties) {
//		this.addProperty(new FilterProperty("----- Wyniki wyszukiwania po numerze OEM -----", ""));
		//this.addProperty(new FilterProperty("----------", "----------"));
		for(FilterProperty fp : oemReplacementsProperties){
			this.addProperty(fp);
		}
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
