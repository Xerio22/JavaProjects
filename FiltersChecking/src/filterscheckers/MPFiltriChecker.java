package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

//TODO strona nic nie znajduje...
public class MPFiltriChecker extends FilterChecker {
	private static final String CHECKER_NAME = "MPFiltri";
	private static final String SERVER_URL_STRING = "http://webapp.mpfiltriusa.com/application/catalog/index?q=_FILTERNAME_&ascending=false&column=pn";
	private static final String SUCCESS_RESPONSE = "";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public MPFiltriChecker() {
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
