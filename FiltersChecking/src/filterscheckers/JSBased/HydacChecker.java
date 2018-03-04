package filterscheckers.JSBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

public class HydacChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Hydac";
	private static final String SERVER_URL_STRING = "https://www.hydac.com/de-en/nc/service/online-tools/betterfit.html";
	private static final String SUCCESS_RESPONSE = "<td class=\"first\">";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "bf-search-input";
	private static final String SEARCH_BUTTON_NAME = ""; // empty name
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_NAME);
	
	public HydacChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<tr class=\"\\s*\\w*\">\\s*"
				+ "<td class=\"first\">\\s*"
				+ "(.*?)\\s*" // OEM
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "(.*?)\\s*" // OEM Number
				+ "</td>\\s*"
				+ "<td class=\"center\">\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "HYDAC\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "(.*?)\\s*" // Hydac Number
				+ "</td>\\s*"
				+ "<td class=\"last center\">\\s*"
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
