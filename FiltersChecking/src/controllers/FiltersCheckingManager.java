package controllers;

import java.util.List;

import javax.swing.ListModel;
import javax.swing.SwingWorker;

import filterscheckers.FilterChecker;
import filtersreaders.FiltersReader;
import filtersreaders.FiltersReaderFromListModel;
import models.Filter;
import models.FilterEquivalents;
import utils.Utils;

public class FiltersCheckingManager {
	
	private ListModel<Filter> filtersListModel;
	
	public FiltersCheckingManager(ListModel<Filter> filtersListModel) {
		this.filtersListModel = filtersListModel;
	}
	
	
	public void startProcessing() {
		List<Filter> filtersFromInput = getFiltersFromInput();
		
		runBackgroundChecking(filtersFromInput);
	}

	
	private List<Filter> getFiltersFromInput() {
		FiltersReader filtersReader = new FiltersReaderFromListModel(filtersListModel);	
		
		return filtersReader.getFiltersAsList();
	}
	
	
	private void runBackgroundChecking(List<Filter> filtersFromInput) {
		SwingWorker<Void, Void> myWorker = new SwingWorker<Void, Void>() {
		    @Override
		    protected Void doInBackground() {
				runFiltersChecking(filtersFromInput);
				
				return null;
		    }
		};
		
		myWorker.execute();
	}
	

	private void runFiltersChecking(List<Filter> filtersFromInput) {
		for(Filter filter : filtersFromInput) {
			findFilterEquivalentsFromEveryServer(filter);
		}
	}


	private void findFilterEquivalentsFromEveryServer(Filter filter) {
		for(FilterChecker checker : Utils.getFiltersCheckers()) {
			FilterEquivalents newEquivalents = checker.getEquivalentsFor(filter); 
			filter.addEquivalents(newEquivalents);
		}		
	}
}





























/*
private void printInfo(String info) {
	view.printInfo(info);
}

private void printInfo(String info, Color color) {
    view.printInfo(info, color);
}

private void printResult(Filter filter) {
	view.printResult(filter.toString());
}

private void printResult(String str) {
	view.printResult(str);
}





public void saveProgress() throws IOException {
	createConfigDirectoryWithProperFiles();
	
	saveNumberOfCheckedFilters();
	saveResultsToFiles(filtersToSave);
}


private void createConfigDirectoryWithProperFiles() {
	try {
		Files.createDirectories(Paths.get(Utils.configFolderPath));
//		Files.createFile(Paths.get(Utils.xmlFilePath));
		Files.createFile(Paths.get(Utils.txtFilePath));
		Files.createFile(Paths.get(Utils.configFilePath));
	} catch (IOException e) {
		//System.out.println(e);
	}
}

private void saveNumberOfCheckedFilters() throws IOException {
	Files.write(Paths.get(Utils.configFilePath), String.valueOf(Utils.numberOfCheckedFilters).getBytes(), StandardOpenOption.WRITE);
}


private void saveResultsToFiles(List<Filter> filtersToSave) throws IOException {
	saveToXml(filtersToSave);
	saveToTxt(filtersToSave);
}


private void saveToXml(List<Filter> filtersToSave) {
	try {
		// prepare document
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
		// root element
		Document doc = null;
		Element rootElement = null;
		
		if(Files.exists(Paths.get(Utils.xmlFilePath))) {
			doc = docBuilder.parse(new File(Utils.xmlFilePath));
			rootElement = doc.getDocumentElement();
		}
		else {
			Files.createFile(Paths.get(Utils.xmlFilePath));
			doc = docBuilder.newDocument();
			rootElement = doc.createElement("katalog");
			doc.appendChild(rootElement);
		}
		
		
		for(Filter filterToSave : filtersToSave) {
			// main elements
			Element zamiennik = doc.createElement("zamiennik");
			rootElement.appendChild(zamiennik);
			
			for(FilterProperty filterProperty : filterToSave.getProperties()) {
				if(filterProperty.isValuable()){
					// new element with property name
					Element zamiennikProperty = doc.createElement(filterProperty.getPropertyName());
					// and property value
					zamiennikProperty.appendChild(doc.createTextNode(filterProperty.getPropertyValue()));
					// add new element to zamienniki
					zamiennik.appendChild(zamiennikProperty);
				}
			}
		}
		
		writeDocumentToFile(doc, Utils.xmlFilePath);

	} catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	} catch (TransformerException tfe) {
		tfe.printStackTrace();
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}


public void writeDocumentToFile(Document document, String path) throws TransformerException {
	File file = new File(path);
	
    // Make a transformer factory to create the Transformer
    TransformerFactory tFactory = TransformerFactory.newInstance();

    // Make the Transformer
    Transformer transformer = tFactory.newTransformer();

    // Mark the document as a DOM (XML) source
    DOMSource source = new DOMSource(document);

    // Say where we want the XML to go
    StreamResult result = new StreamResult(file);

    // Write the XML to file
    transformer.transform(source, result);
}


private void saveToTxt(List<Filter> filtersToSave) {
	filtersToSave.forEach(
			filter -> appendToFile(filter.toString(), Utils.txtFilePath)
	);
}


private void appendToFile(String data, String filePath) {
	try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));){
	    writer.println(data);
	    writer.println();
	} 
	catch(Exception e){
		e.printStackTrace();
	}
}


public void loadDataFromConfigFile() {
	try (BufferedReader br = new BufferedReader(new FileReader(new File(Utils.configFilePath)));){
		Utils.numberOfCheckedFilters = Integer.parseInt(br.readLine());
		Utils.reconnect_time = minutesToMillis(Integer.parseInt(br.readLine()));
		Utils.numberOfIterationsBefereSaving = Integer.parseInt(br.readLine());
	} 
	catch(Exception e){
		e.printStackTrace();
	}
}

private int minutesToMillis(int minutes) {
	return minutes * 60 * 1000;
}
*/

/*
public void loadDataFromFileAndStartProcessing() {
		FileLoader fileLoader = new FileLoader(Utils.default_input_path);
		File xmlFile = fileLoader.loadFile();
		
		FiltersReader xfr = new FiltersReaderFromXml(xmlFile);
		
		try {
			List<Filter> filtersFromInput = xfr.getFiltersAsList();
			
			SwingWorker<Void, Void> myWorker= new SwingWorker<Void, Void>() {
			    @Override
			    protected Void doInBackground() {
					runFiltersChecking(filtersFromInput);
					
					return null;
			    }
			};
			myWorker.execute();
			
			
		} catch (ParserConfigurationException e) {
			printResult("Blad w tworzeniu dokumentu!");
			e.printStackTrace();
		} catch (SAXException e) {
			printResult("Blad w przetwarzaniu pliku XML!");
			printResult(e.toString() + "\n");
			StringWriter stackTraceWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTraceWriter));
			printResult(e.toString() + "\n" + stackTraceWriter.toString());
			e.printStackTrace();
		} catch (IOException e) {
			printResult("Blad w dostepie do pliku XML!");
			e.printStackTrace();
		} catch (Exception e) {
			printResult("Wystapil blad podczal ladowania pliku!");
			e.printStackTrace();
		}
	}
*/
