package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class StauffChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Stauff";
	private static final String SERVER_URL_STRING = "http://www.filtersuche.de/de/filtersuche/liste.html?q=_FILTERNAME_&x=0&y=0";
	private static final String SUCCESS_RESPONSE = "<table class=\"filterliste desktop\">";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public StauffChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<td>(.*?)<br/>" // OEM Number
				+ "Hersteller:&nbsp;(.*?)</td>" // OEM
				+ "<td>(.*?)</td>" // Stauff Number
				+ "<td><abbr title=\"Außendurchmesser\">AD</abbr>:.*?<br/>"
				+ "<abbr title=\"Innendurchmesser\">ID</abbr>:.*?<br />"
				+ "<abbr title=\"Höhe\">H</abbr>:.*?</td>"
				+ "<td class=\"aktion\">"
				+ "<a href=\"http://www.filtersuche.de/de/filtersuche/liste.html.*?\">"
				+ "<img src=\"http://www.filtersuche.de/_Resources/.*?\" alt=\"\">merken</a><br />"
				+ "<a href=\"http://www.filtersuche.de/de/filtersuche/details.html.*?\">"
				+ "<img src=\"http://www.filtersuche.de/.*?\" alt=\"\">Details</a><br />"
				+ "<a class=\"request\" href=\"http://www.filtersuche.de/de/filtersuche/liste.html.*?\">Expressanfrage</a>"
				+ "</td></tr>");

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
