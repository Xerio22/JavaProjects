package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class CumminsChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Cummins";
	private static final String SERVER_URL_STRING = "https://catalog.cumminsfiltration.com/catalog/";
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String FAILURE_RESPONSE = "This product is not currently  <br>in our product line,<br>click";
	private static final String INPUT_FIELD_ID = "GSAText";
	private static final String SEARCH_BUTTON_ID = "googleSearchText";
	private static final ServerConnectionHandler connectionHandler = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);
	
	public CumminsChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		System.out.println(serverResponse);
		Pattern p = Pattern.compile(
				"");

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
