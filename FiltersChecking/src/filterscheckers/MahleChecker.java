package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class MahleChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Mahle";
	private static final String SERVER_URL_STRING = "https://catalog.mahle-aftermarket.com/eu/index.xhtml";
	private static final String FAILURE_RESPONSE = "Brak wyników"; 
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "j_id_1d_9:quick";
	private static final String SEARCH_BUTTON_ID = "j_id_1d_9:searchLuci";
	private static final ServerConnectionHandler connectionHandler = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);
	
	public MahleChecker() {
		super(CHECKER_NAME, connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
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
