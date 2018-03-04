package filterscheckers.URLBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

public class WixChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Wix";
	private static final String SERVER_URL_STRING = "http://www.wixfilters.com/Lookup/InterchangeMultiSearch.aspx?q=_FILTERNAME_&o=me";
	private static final String SUCCESS_RESPONSE = "table class=";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public WixChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				"<span id=\"lblCompetitor\">(.*?)</span>\\s*" // oem number
				+ "<span id=\"lblOE\">\\s*</span>\\s*"
				+ "</font>\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "<font face=\"Verdana\" color=\"#333333\">"
				+ "\\s*(.*?)\\s*" // oem
				+ "</font>\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "<font face=\"Verdana\" color=\"#333333\">\\s*"
				+ "<a id=\"hlPartNumber\" href=\"javascript:var .*?;w\\.focus\\(\\);\">(.*?)</a>" // wix number
		);

		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;	
		while(m.find()){
			equivalentOEM = m.group(2);
			equivalentOEMNumber = m.group(1);
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
