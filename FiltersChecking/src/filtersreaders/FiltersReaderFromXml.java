package filtersreaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import models.Filter;
import utils.XmlStructureProvider;

public class FiltersReaderFromXml implements FiltersReader {
	private File file;
	private XmlStructureProvider xsp;
	
	public FiltersReaderFromXml(File file, XmlStructureProvider xsp) {
		this.file = file;
		this.xsp = xsp;
	}
	
	public List<Filter> getFiltersAsList() {
		List<Filter> filters = new ArrayList<>();
		
        Document doc = null;
		try {
			doc = prepareDocument(file);
			
			// TODO think how to pass exception info to view
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        NodeList elements = doc.getElementsByTagName(xsp.getElementTagName());
       
        for (int i = 0; i < elements.getLength(); i++) {
           Node elementNode = elements.item(i);
           
           if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
              Element filterElement = (Element) elementNode;
              
              Filter filterToCheck = Filter.createFilterFromXmlElement(filterElement, xsp.getOemNumberTagName());
              filters.add(filterToCheck);
           }
        }
        
        return filters;
	}
	
	
	private Document prepareDocument(File selectedFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(selectedFile);
        doc.getDocumentElement().normalize();
        
		return doc;
	}
}
