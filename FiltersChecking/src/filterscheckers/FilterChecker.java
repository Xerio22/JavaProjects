package filterscheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.FilterProperty;
import models.Brand;
import models.Filter;
import models.FilterEquivalents;
import utils.Utils;

public abstract class FilterChecker {
	private boolean isServerBlocked = false;
	private String serverUrlString;
	private Brand brand;
	
	public FilterChecker(String serverUrlString, Brand brand){
		this.serverUrlString = serverUrlString;
		this.brand = brand;
	}

	public FilterEquivalents getEquivalentsFor(Filter filter) {

		FilterEquivalents fe = new FilterEquivalents();
		
		String searchedFilterOEMnumber = filter.getPropertyValueByName(Utils.OEM_NUMBER_TAG_NAME);
		
		Filter equivalent = Filter.createFilterUsingBrandNameAndOEMnumber(brand.name(), searchedFilterOEMnumber);
		
//			String filterName = filter.getValueOfTag(Utils.filtr_wf);
		
			
		
		
		return fe;
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
	
	
	
	
	
	
	private boolean isFilterInDatabase(String filterName) {
		this.serverUrlString = insertFilterNameIntoUrlString(filterName);
		String serverResponse = getServerResponseFor(filterName);
		
		boolean isFilterInDatabase = checkIsAnyReplacementPresent(serverResponse, filterName);
		
		return isFilterInDatabase;
	}
	
	private String insertFilterNameIntoUrlString(String filterName) {
		return serverUrlString.replaceAll("_FILTERNAME_", filterName);
	}

	private String getServerResponseFor(String name) {
		
		// Create URL and open connection
		URLConnection uc = createURLConnectionFromString(serverUrlString);
			
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
//					printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
//					printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
					
					Thread.sleep(10000);
					
					uc = createURLConnectionFromString(serverUrlString);
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
//			printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
//			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setServerBlocked(false);
		}
		
		return false;
	}
	
	
//	private List<FilterProperty> findReplacementsPropertiesByOEMNumber(Filter filter, String filterName) {
//		
//		boolean isFilterInDatabase = isFilterInDatabase(filterName);
//		
//		if(!isFilterInDatabase){
//			return null;
//		}
//		else{
//			serverResponse = serverResponse.replaceAll("\\t+", "");
//		
//			Pattern p = Pattern.compile("<table class=\"table table-hover\">"
//					+ "<thead><tr>"
//					+ "<th>Cross references</th>"
//					+ "<th>Brand</th>"
//					+ "<th>N° HIFI\\s*</th>"
//					+ "<th></th></tr></thead>"
//					+ "<tbody>"
//					+ "(<tr class=\"product-line img\">"
//					+ "<td>([a-zA-Z_0-9 -\\./\\\\]+)</td>"
//					+ "<td>([a-zA-Z_0-9 -\\./\\(\\)=]+)</td>"
//					+ "<td>([a-zA-Z_0-9 -]+)</td>"
//					+ "<td>.*</td>"
//					+ "</tr>)+</tbody></table>");
//
//			Matcher m = p.matcher(serverResponse);
//			
//			String replacementName = null;
//			String brand = null;
//			String hifiNumber = null;
//			
//			List<FilterProperty> hifiReplacementsForThisOem = new ArrayList<>();
//			
//			int propIdx = 1;
//			if(m.find()){
//				Pattern pp = Pattern.compile(
//						"<tr class=\"product-line img\">"
//						+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\\\]]+)</td>"
//						+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\(][\\)][=]]+)</td>"
//						+ "<td>([[a-z][A-Z][_][0-9][ ][-]]+)</td>"
//						+ "<td><a href=.*?></a></td>"
//						+ "</tr>");
//				
//				Matcher mm = pp.matcher(m.group(1));
//				
//				while(mm.find()){
//					replacementName = mm.group(1);
//					brand = mm.group(2);
//					hifiNumber = mm.group(3);
//	
//					// Create new properties with brand and number values and add them to list of replacement's properties
//					FilterProperty replacementNameProperty = new FilterProperty("OEM_" + propIdx, replacementName);
//					FilterProperty brandProperty = new FilterProperty("brand_" + propIdx, brand);
//					FilterProperty hifiNumberProperty = new FilterProperty("hifiNumber_" + propIdx, hifiNumber);
//					hifiReplacementsForThisOem.add(replacementNameProperty);
//					hifiReplacementsForThisOem.add(brandProperty);
//					hifiReplacementsForThisOem.add(hifiNumberProperty);
//					
//					propIdx++;
//				}
//			}
//			
//			return hifiReplacementsForThisOem;
//		}
//	}
	private void addPropertiesToFilterAndPrintResults(Filter filter, String name, List<FilterProperty> oemReplacementsProperties) {
		if(oemReplacementsProperties != null && !oemReplacementsProperties.isEmpty()){
//			printResult("Zamienniki znalezione!\n");
//			printResult("------ Dane wyszukiwanego filtra ---------------");
			
			filter.addProperties(oemReplacementsProperties);
			
//			printResult(filter);
//			printResult("\n");
		}
		else{
//			printResult("Calkowity brak danych o filtrze " + name + "!");
//			printResult("------ Dane wyszukiwanego filtra ---------------");
//			printResult(filter);
//			printResult("\n");
		}
	}
	
	
	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}
	
	private int millisToMinutes(int millis){
		return millis / 60000;
	}
}
