package filterscheckers.URLBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

// TODO Zapytac czy interesuja nas tylko zamienniki czy FILTRY FILTRON tez
public class FiltronChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Filtron";
	private static final String SERVER_URL_STRING = "http://filtron.eu/pl/znajdz-filtr?filtronNumber=_FILTERNAME_&action=showFiltersByNumber&formType=1";
	private static final String SUCCESS_RESPONSE = "<h2>zamienniki</h2>";//"<div class=\"th\">NUMER FILTRA</div>";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public FiltronChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<div class=\"tr trlink\"\\s*onclick=\"document\\.location='.*?'\"\\s*>\\s*"
				+ "<div class=\"td\"><span>(.*?)</span></div>\\s*" // OEM Number
				+ "<div class=\"td\">(.*?)</div>\\s*" // OEM
				+ "<div class=\"td\"><span class=\"bold\">(.*?) <a rel=\".*?\" class=\"info_win\" href=\"#\">\\s*" // Filtron Number
				+ "<img width=\"22\" src=\"/website/images/zoom-plus-sign.png\" alt=\"Podgląd\"/></a>\\s*</span></div>\\s*</div>"
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
