package controllers;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import main.FileLoader;
import main.FiltersReader;
import main.Utils;
import main.FiltersReaderFromXml;
import models.Filter;
import models.FilterProperty;
import models.FiltersListModel;
import views.MainView;

public class FilterDataChecker {
	private boolean isServerBlocked = false;
	private List<Filter> filtersToSave = new ArrayList<>();
	private MainView view;
	
	public FilterDataChecker(FiltersListModel filtersListModel) {
		List<Filter> filters = filtersListModel.getFiltersAsList();
	}
	
	public void startProcessing() {
		
	}


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

	
	
	
	
	private void runFiltersChecking(List<Filter> filtersFromInput) {

		for(; Utils.numberOfCheckedFilters < filtersFromInput.size(); Utils.numberOfCheckedFilters++) {	
			saveProgressIfNeeded();
			Filter filter = filtersFromInput.get(Utils.numberOfCheckedFilters);
			
			String name = filter.getValueOfTag(Utils.filtr_wf);
			boolean isWfPresent = isFilterInDatabase(filter, name);
			
			while(isServerBlocked){
				try {
					restartRouter();
					
					Thread.sleep(Utils.reconnect_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isWfPresent = isFilterInDatabase(filter, name);
			} 
			
			if(isWfPresent){
				printInfo(name, Color.GREEN.darker());
			}
			else{
				printInfo(name, Color.RED);
				
				printResult("Brak danych dla filtra " + name + " w bazie... Wyszukiwanie po oryginale...");
				
				name = filter.getValueOfTag(Utils.obcy_skrot);
				printResult("Numer oryginalu -> " + name);
				
				List<FilterProperty> oemReplacementsProperties = findReplacementsPropertiesByOEMNumber(filter, name);
				
				if(name.startsWith("0")){
					Filter filterWithoutLeadingZeros = new Filter(filter);
					filterWithoutLeadingZeros.getRidOfLeadingZeros();
					String nameWithoutLeadingZeros = filterWithoutLeadingZeros.getValueOfTag(Utils.obcy_skrot);
					
					printResult("Numer ma na poczatku zera.. Sprawdzanie dla numeru bez zer..");
					printResult("Numer oryginalu bez zer -> " + nameWithoutLeadingZeros + "\n");
					
					List<FilterProperty> oemWithoutLeadingZerosReplacementsProperties = findReplacementsPropertiesByOEMNumber(filterWithoutLeadingZeros, nameWithoutLeadingZeros);
					
					addPropertiesToFilterAndPrintResults(filterWithoutLeadingZeros, nameWithoutLeadingZeros, oemWithoutLeadingZerosReplacementsProperties);
					
					filtersToSave.add(filterWithoutLeadingZeros);
				}
				
				// w tym miejscu zeby w razie zer nie bylo zdublowane
				addPropertiesToFilterAndPrintResults(filter, name, oemReplacementsProperties);
				
				filtersToSave.add(filter);
			}
		}
	}

	
	private void restartRouter() {
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setRedirectEnabled(true);
	        webClient.getOptions().setThrowExceptionOnScriptError(true);
	        webClient.getOptions().setCssEnabled(true);
	        webClient.getOptions().setUseInsecureSSL(true);
	        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
	        webClient.getCookieManager().setCookiesEnabled(true);
	        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			
	        HtmlPage loggedPage = loginToRouter(webClient);
			restartInternetConnection(webClient, loggedPage);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	private HtmlPage loginToRouter(WebClient webClient) throws IOException {
		final HtmlPage loggingPage = webClient.getPage("http://10.0.0.1/");
        final HtmlForm form = loggingPage.getFormByName("Login");
        
        final HtmlTextInput usernameInputField = (HtmlTextInput) loggingPage.getElementById("PopupUsername");//.getInputByName("username");
        final HtmlPasswordInput passInputField = (HtmlPasswordInput) loggingPage.getElementById("PopupPassword");
        final HtmlSubmitInput loginButton = form.getInputByValue("zaloguj");//.getElementById("bt_authenticate");
        usernameInputField.setValueAttribute("admin");
        passInputField.setValueAttribute("rs1970as1965ms1");
        
        loginButton.click();
        
        final HtmlPage loggedPage = webClient.getPage("http://10.0.0.1/advConfigAccessType.html");
        
	    return loggedPage;
	}
	
	private void restartInternetConnection(WebClient webClient, HtmlPage loggedPage) throws IOException {
		HtmlButtonInput restartButton =  (HtmlButtonInput) loggedPage.getElementById("bt_refresh");
        restartButton.click();
	}

	private void saveProgressIfNeeded() {
		if(Utils.numberOfCheckedFilters % Utils.numberOfIterationsBefereSaving == 0 && Utils.numberOfCheckedFilters != 0){
			try {
				saveProgress();
				filtersToSave.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			printInfo("Progress saved", Color.BLUE);
		}
	}
	

	private void addPropertiesToFilterAndPrintResults(Filter filter, String name, List<FilterProperty> oemReplacementsProperties) {
		if(oemReplacementsProperties != null && !oemReplacementsProperties.isEmpty()){
			printResult("Zamienniki znalezione!\n");
			printResult("------ Dane wyszukiwanego filtra ---------------");
			
			filter.addProperties(oemReplacementsProperties);
			
			printResult(filter);
			printResult("\n");
		}
		else{
			printResult("Calkowity brak danych o filtrze " + name + "!");
			printResult("------ Dane wyszukiwanego filtra ---------------");
			printResult(filter);
			printResult("\n");
		}
	}


	
	private boolean isFilterInDatabase(Filter filter, String name) {
		printInfo(Utils.numberOfCheckedFilters + ". Waiting for connection...");
		String serverResponse = getServerResponseFor(name);
		printInfo("connected");
		
		boolean isFilterInDatabase = checkIsAnyReplacementPresent(serverResponse, name);
		
		return isFilterInDatabase;
	}
	


	private String getServerResponseFor(String name) {
		
		// Prepare URL string
		String urlString = "https://hifi-filter.com/en/catalog/" + name + "-recherche-equivalence.html";
		
		// Create URL and open connection
		URLConnection uc = createURLConnectionFromString(urlString);
			
		// Get server response and put it into String Buffer
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			
			br.lines().forEach(line -> sb.append(line));
			br.close();
		}
		catch(Exception e){
			while(br == null){
				try {
					printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
					printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
					
					Thread.sleep(10000);
					
					uc = createURLConnectionFromString(urlString);
					br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		
		return sb.toString();
	}
	
	

	private URLConnection createURLConnectionFromString(String urlString) {
		URL hifiUrl = null;
		URLConnection uc = null;
		try {
			hifiUrl = new URL(urlString);
			uc = hifiUrl.openConnection();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		// Setup connection
		uc.setUseCaches(false);
		uc.setDefaultUseCaches(false);
		uc.setReadTimeout(10000);
		uc.setConnectTimeout(10000);
		
		return uc;
	}

	private boolean checkIsAnyReplacementPresent(String serverResponse, String name) {
		if(serverResponse.contains(Utils.SUCCESS_RESPONSE)) {
			setServerBlocked(false);
			return true;
		}
		else if(serverResponse.contains(Utils.BLOCKED_BY_SERVER_RESPONSE)) {
			printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setServerBlocked(false);
		}
		
		return false;
	}

	
	
	private List<FilterProperty> findReplacementsPropertiesByOEMNumber(Filter filter, String name) {
		// Manually get br to use it in further calculations without double connecting
		String serverResponse = getServerResponseFor(name);
		boolean isAnyOEMReplacementPresent = checkIsAnyReplacementPresent(serverResponse, name);
		
		if(!isAnyOEMReplacementPresent){
			return null;
		}
		else{
			serverResponse = serverResponse.replaceAll("\\t+", "");
		
			Pattern p = Pattern.compile("<table class=\"table table-hover\">"
					+ "<thead><tr>"
					+ "<th>Cross references</th>"
					+ "<th>Brand</th>"
					+ "<th>N° HIFI\\s*</th>"
					+ "<th></th></tr></thead>"
					+ "<tbody>"
					+ "(<tr class=\"product-line img\">"
					+ "<td>([a-zA-Z_0-9 -\\./\\\\]+)</td>"
					+ "<td>([a-zA-Z_0-9 -\\./\\(\\)=]+)</td>"
					+ "<td>([a-zA-Z_0-9 -]+)</td>"
					+ "<td>.*</td>"
					+ "</tr>)+</tbody></table>");

			Matcher m = p.matcher(serverResponse);
			
			String replacementName = null;
			String brand = null;
			String hifiNumber = null;
			
			List<FilterProperty> hifiReplacementsForThisOem = new ArrayList<>();
			
			int propIdx = 1;
			if(m.find()){
				Pattern pp = Pattern.compile(
						"<tr class=\"product-line img\">"
						+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\\\]]+)</td>"
						+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\(][\\)][=]]+)</td>"
						+ "<td>([[a-z][A-Z][_][0-9][ ][-]]+)</td>"
						+ "<td><a href=.*?></a></td>"
						+ "</tr>");
				
				Matcher mm = pp.matcher(m.group(1));
				
				while(mm.find()){
					replacementName = mm.group(1);
					brand = mm.group(2);
					hifiNumber = mm.group(3);
	
					// Create new properties with brand and number values and add them to list of replacement's properties
					FilterProperty replacementNameProperty = new FilterProperty("OEM_" + propIdx, replacementName);
					FilterProperty brandProperty = new FilterProperty("brand_" + propIdx, brand);
					FilterProperty hifiNumberProperty = new FilterProperty("hifiNumber_" + propIdx, hifiNumber);
					hifiReplacementsForThisOem.add(replacementNameProperty);
					hifiReplacementsForThisOem.add(brandProperty);
					hifiReplacementsForThisOem.add(hifiNumberProperty);
					
					propIdx++;
				}
			}
			
			return hifiReplacementsForThisOem;
		}
	}
	

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
	
	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}

	
	
	public void saveProgress() throws IOException {
		createConfigDirectoryWithProperFiles();
		
		saveNumberOfCheckedFilters();
		saveResultsToFiles(filtersToSave);
	}
	
	
	private void createConfigDirectoryWithProperFiles() {
		try {
			Files.createDirectories(Paths.get(Utils.configFolderPath));
//			Files.createFile(Paths.get(Utils.xmlFilePath));
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
	
	private int millisToMinutes(int millis){
		return millis / 60000;
	}

	
}
