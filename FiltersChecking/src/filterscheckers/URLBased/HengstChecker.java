package filterscheckers.URLBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

public class HengstChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Hengst";
	private static final String SERVER_URL_STRING = "https://www.hengst.com/en/online-catalog/search-results/?searchType=freetext&catalog=eu&freetext=_FILTERNAME_";
	private static final String SUCCESS_RESPONSE = "Limit search results";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public HengstChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
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
