package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class HifiChecker extends FilterChecker {
	private static final String CHECKER_NAME = "HIFI";
	private static final String SERVER_URL_STRING = "https://hifi-filter.com/en/catalog/_FILTERNAME_-recherche-equivalence.html";
	private static final String SUCCESS_RESPONSE = "<h2>The result of your request for filter";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "<h2>Merci de patienter ...</h2>";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public HifiChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<tbody>"
				+ "<tr class=\".*?\">"
				+ "<td>(.*?)</td>" // OEM number
				+ "<td>(.*?)</td>" // OEM
				+ "<td>(.*?)</td>" // HIFI number
				+ "<td>.*?</td>"
				+ "</tr>"
				+ "</tbody>");

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
