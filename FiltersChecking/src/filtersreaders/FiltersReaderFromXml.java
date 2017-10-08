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

public class FiltersReaderFromXml implements FiltersReader{
	private File file;
	
	public FiltersReaderFromXml(File file) {
		this.file = file;
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
		
        NodeList zamienniki = doc.getElementsByTagName("zamiennik");
       
        for (int i = 0; i < zamienniki.getLength(); i++) {
           Node zamiennikNode = zamienniki.item(i);
           
           if (zamiennikNode.getNodeType() == Node.ELEMENT_NODE) {
              Element zamiennik = (Element) zamiennikNode;
              
              Filter filterToCheck = Filter.createFilterFromXmlElement(zamiennik);
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
