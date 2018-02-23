package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class MannChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Mann";
	private static final String SERVER_URL_STRING = "https://catalog.mann-filter.com/EU/eng/oenumbers";
	private static final String SUCCESS_RESPONSE = "oeResultPanel";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "Sorry, an error occurred. Please reload this page.";
	private static final String INPUT_FIELD_ID = "autocompleteOEsearch:autocompleteOEsearch:searchQueryInput";
	private static final String SEARCH_BUTTON_ID = "autocompleteOEsearch:autocompleteOEsearch:searchButton";
	private static final String FAILURE_RESPONSE = "No equivalent";
	private static final ServerConnectionHandler connectionHandler = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);
	
	public MannChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				"<div class=\"row\">\\s*"
				+ "<div class=\"column compare_name\" data-label=\"\">\\s*"
				+ "<div class=\"tableContent\">\\s*(.*?)\\s*" // oem number
				+ "</div>\\s*"
				+ "</div>\\s*"
				+ "<div class=\"column compare_brand\" data-label=\"Manufacturer\">\\s*"
				+ "<div class=\"tableContent\">\\s*(.*?)\\s*" //oem
				+ "</div>\\s*"
				+ "</div>\\s*"
				+ "<div class=\"column compare_mannFilter\" data\\-label=\"MANN-FILTER\">\\s*"
				+ "<div class=\"tableContent\">\\s*"
				+ "<a href=\"/EU/eng/catalog/MANN.*?\" id=\".*?\\-link\">\\s*(.*?)\\s*</a>\\s*" // mann number
				+ "</div>\\s*"
				+ "</div>\\s*"
				+ "<div class=\"column compare_availability\" data-label=\"\">\\s*"
				+ "<div class=\"tableContent\">\\s*(.*?)?\\s*" // availability info
				+ "</div>\\s*"
				+ "</div>\\s*"
				+ "<div class=\"column compare_info\" data-label=\"\">\\s*"
				+ "<div class=\"tableContent\">\\s*"
				+ "</div>\\s*"
				+ "</div>\\s*"
				+ "</div>\\s*");

			Matcher m = p.matcher(serverResponse);
			
			String equivalentOEMNumber = null;
			String equivalentOEM = null;
			String equivalentNumber = null;
//			String equivalentAvailabilityInfo = null;
			
			FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
			
			int propIdx = 1;
			while(m.find()){
				equivalentOEMNumber = m.group(1);
				equivalentOEM = m.group(2);
				equivalentNumber = m.group(3);
//				equivalentAvailabilityInfo = m.group(4);
				
//				if(equivalentAvailabilityInfo.equals("")){
//					equivalentAvailabilityInfo = "Not available";
//				}
				
				equivalentsForThisOem.createAndAddEquivalent(
						getCheckerName(), 
						equivalentOEMNumber, 
						equivalentOEM, 
						equivalentNumber, 
						propIdx
//						new String[]{"Availability", equivalentAvailabilityInfo}
				);
				
				propIdx++;
			}
			
			return equivalentsForThisOem;
	}

	@Override
	public String getCheckerName() {
		return CHECKER_NAME;
	}
	
//	@Override
//	public List<String> getColumnsNames() {
//		List<String> inheritedNames = super.getColumnsNames();
//		inheritedNames.add("Dostępność");
//		return inheritedNames;
//	}
}
