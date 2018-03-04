package filterscheckers.JSBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

// TODO nie nadaje sie do zaimplementowania na stale bo wyszukiwarka zwraca raz ze znaleziono raz ze nie znaleziono - losowo
public class EatonChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Eaton";
	private static final String SERVER_URL_STRING = "http://www.eatonpowersource.com/cross-reference/?page=16346#/p:1/sb:CompetitorPartNumber/o:Asc/ps:100";
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String FAILURE_RESPONSE = "408626 - 296418 of 296418 Results";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "Criteria_CompetitorPartNumber";
	private static final String SEARCH_BUTTON_CSS_SELECTOR = ".btn.btn-primary.js-part-search";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_CSS_SELECTOR);
	
	
	public EatonChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<tr class=\"cross-table-cell \\w+\">\\s*"
				+ "<td class=\"part-type\">\\s*"
				+ ".*?\\s*"
				+ "</td>\\s*"
				+ "<td class=\"cross-left-block\">\\s*"
				+ "<span>\\s*(.*?)\\s*</span>\\s*" // OEM
				+ "<span class=\"part-number\">\\s*(.*?)\\s*</span>\\s*" // OEM Number
				+ "</td>\\s*"
				+ "<td class=\"cross-icon\">\\s*"
				+ "<span class=\"icon-cross-ref\">\\s*.*?\\s*</span>\\s*"
				+ "</td>\\s*"
				+ "<td class=\"cross-right-block\\s*(no-part)?\">\\s*"
				+ ".*?\\s*"
				+ "<span class=\"part-number\">\\s*"
				+ "(<a href=\"/cross-reference/details/.*?\">\\s*)?"
				+ "\\s*(.*?)\\s*"
				+ "(</a>)?\\s*" // Eaton Number
				+ "</span>\\s*"
				+ "<div style=\"color:black\">\\s*"
				+ "</div>\\s*"
				+ "</td>\\s*"
				+ "<td class=\"cross-ref-note\">\\s*"
				+ "</td>\\s*"
				+ "</tr>"
				);
		
			Matcher m = p.matcher(serverResponse);
			
			String equivalentOEMNumber = null;
			String equivalentOEM = null;
			String equivalentNumber = null;
		
			FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
			
			int propIdx = 1;
			while(m.find()){
				equivalentOEM = m.group(1);
				equivalentOEMNumber = m.group(2);
				equivalentNumber = m.group(4);
				
				equivalentsForThisOem.createAndAddEquivalent(
						getCheckerName(), 
						equivalentOEMNumber, 
						equivalentOEM, 
						equivalentNumber, 
						propIdx
				);
				
				propIdx++;
			}
	
			return equivalentsForThisOem;
	}
}
