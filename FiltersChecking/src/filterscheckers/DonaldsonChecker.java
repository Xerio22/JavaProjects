package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class DonaldsonChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Donaldson";
	private static final String SERVER_URL_STRING = "https://catalog.donaldson.com/searchResults/en/C/_/N-2v?Ntk=cro&Ntt=_FILTERNAME_";
	private static final String SUCCESS_RESPONSE = "table class=";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public DonaldsonChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<td class=\"p-brand\">(.*?)</td>\\s*" // oem
				+ "<td class=\"u-identifier\"><span class=\"type hidden\">OEM</span><span class=\"value\">(.*?)</span></td>\\s*" // oem number
				+ "<td class=\"u-identifier\">\\s*"
				+ "<span class=\"type hidden\">UPC</span>"
				+ ".*?"
				+ "<span class=\".*?\">(.*?)</span>\\s*" // donaldson number
				+ "</td>\\s*"
				+ "<td class=\"p-name\">(.*?)</td>\\s*" // product
				+ "<td class=\"p-notes\">(.*?)</td>\\s*"); // notes"

		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
		String equivalentProduct = null;
		String equivalentNotes = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;
		while(m.find()){
			equivalentOEM = m.group(1);
			equivalentOEMNumber = m.group(2);
			equivalentNumber = m.group(3);
			equivalentProduct = m.group(4);
			equivalentNotes = m.group(5);

			equivalentsForThisOem.findAndAddEquivalent(
					getCheckerName(), 
					equivalentOEMNumber, 
					equivalentOEM, 
					equivalentNumber, 
					propIdx, 
					new String[][]{{"Product", equivalentProduct},
								   {"Notes", equivalentNotes}}
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

 
