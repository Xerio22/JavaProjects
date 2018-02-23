package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Baldwin";
	private static final String SERVER_URL_STRING = "http://stage.catalog.baldwinfilter.com/Cross-Reference";
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String FAILURE_RESPONSE = "<tr>\\s*<th style=\"text-align:center;\">No crosses exist in the database for these items</th>\\s*</tr>";
	private static final String INPUT_FIELD_ID = "fieldCrossReference1";
	private static final String SEARCH_BUTTON_ID = "btnFindCrosses";
	private static final ServerConnectionHandler connectionHandler = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);

	public BaldwinChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
			"<tr id=\".*?\">\\s*"
			+ "<td class=\"comp_no\">\\s*"
			+ "(.*)?\\s*" // oem number
			+ "</td>\\s*"
			+ "<td class=\"mfg\">\\s*"
			+ "(.*)?\\s*" // oem
			+ "</td>\\s*"
			+ "<td class=\"partLink partLeft\">\\s*"
			+ "<a class=\"btnViewProductDetail\" href=\"#\">\\s*"
			+ "(.*)?\\s*" // baldwin number
			+ "</a>\\s*"
			+ "</td>\\s*"
			+ "<td class=\"partComment\"/>\\s*"
			+ "</tr>\\s*");

		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
//		String equivalentQualifiers = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;
		while(m.find()){
			equivalentOEMNumber = m.group(1);
			equivalentOEM = m.group(2);
			equivalentNumber = m.group(3);

			equivalentsForThisOem.findAndAddEquivalent(
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

	@Override
	public String getCheckerName() {
		return CHECKER_NAME;
	}
}
