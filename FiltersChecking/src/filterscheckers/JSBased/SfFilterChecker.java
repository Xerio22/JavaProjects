package filterscheckers.JSBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

public class SfFilterChecker extends FilterChecker {
	private static final String CHECKER_NAME = "SF-Filter";
	private static final String SERVER_URL_STRING = "https://www.sf-filter.com/de/vergleichsliste/";
	private static final String SUCCESS_RESPONSE = "<form id=\"frm_1\" action=\"javascript:return false\">";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "txt_ArtSearch";
	private static final String SEARCH_BUTTON_CSS_SELECTOR = ".form-icon";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_CSS_SELECTOR);
	
	
	public SfFilterChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				"<form id=\"frm_\\d+?\" action=\"javascript:return false\">\\s*"
				+ "<li class=\"product-entry\">\\s*"
				+ "<div class=\"pe-seperator\">\\s*"
				+ "<div class=\"pe-orignr\">\\s*"
				+ "<span class=\"responsive-label\">\\s*"
				+ "Originalnummer:\\s*</span>\\s*"
				+ "<span title=\"\">\\s*"
				+ "(.*?)\\s*" // OEM number
				+ "</span>\\s*</div>\\s*"
				+ "<div class=\"pe-producer\">\\s*<span class=\"responsive-label\">\\s*Hersteller:\\s*</span>\\s*"
				+ "<span title=\"\">\\s*(.*?)\\s*</span>\\s*" // OEM
				+ "</div>\\s*</div>\\s*"
				+ "<div class=\"pe-seperator\">\\s*"
				+ "<div class=\"pe-img\">\\s*"
				+ "<img class=\"img-responsive img-thumbnail\" src=\".*?\"/>\\s*</div>\\s*"
				+ "<div class=\"pe-data\">\\s*"
				+ "<div class=\"pe-article\">\\s*"
				+ "<span class=\"responsive-label\">\\s*SF-Artikel:\\s*</span>\\s*"
				+ "<span title=\"\">\\s*"
				+ "(.*?)\\s*" // SF number
				+ "</span>\\s*</div>\\s*"
				+ "<div class=\"pe-type\" style=\".*?\">\\s*<span class=\"responsive-label\">\\s*Typ:\\s*</span>\\s*"
				+ "(.*?)\\s*" // type
				+ "</div>\\s*</div>\\s*</div>\\s*</li>\\s*"
				+ "</form>");
		
			Matcher m = p.matcher(serverResponse);
			
			String equivalentOEMNumber = null;
			String equivalentOEM = null;
			String equivalentNumber = null;
		
			FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
			
			int propIdx = 1;
			while(m.find()){
				equivalentOEMNumber = m.group(1);
				equivalentOEM = m.group(2);
				equivalentNumber = m.group(3);
				
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
