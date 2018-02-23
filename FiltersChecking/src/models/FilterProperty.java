package models;

import org.w3c.dom.Element;

public class FilterProperty {
	private String propertyName;
	private String propertyValue;

	public FilterProperty(Element property) {
		propertyName = property.getTagName();
		propertyValue = property.getTextContent();
	}
	
	public FilterProperty(String propertyName, String propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}
	
	@Override
	public String toString() {
		return propertyName + ": " + propertyValue;
	}

	public void getRidOfWhiteSpacesFromPropertyName() {
		propertyName = propertyName.replaceAll("\\s", "_");
	}

	public void setPropertyValue(String nameWithoutZeros) {
		this.propertyValue = nameWithoutZeros;
	}
}
