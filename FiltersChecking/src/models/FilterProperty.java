package models;

import org.w3c.dom.Element;

import main.Utils;

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
	
	public boolean isValuable() {
		return  this.getPropertyName().equals(Utils.obcy_skrot) ||
				this.getPropertyName().equals("firma") || 
				this.getPropertyName().equals(Utils.filtr_wf) || 
				this.getPropertyName().contains("OEM") || 
				this.getPropertyName().contains("brand") || 
				this.getPropertyName().contains("hifiNumber"); 
	}

	public void getRidOfWhiteSpaces() {
		propertyName = propertyName.replaceAll("\\s", "_");
		//propertyValue = propertyValue.replaceAll("\\s", "_");
	}

	public void setPropertyValue(String nameWithoutZeros) {
		this.propertyValue = nameWithoutZeros;
	}
}
