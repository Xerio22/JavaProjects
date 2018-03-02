package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class SogefiChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Sogefi";
	private static final String SERVER_URL_STRING = "http://www.sogefifilterdivision.com/catalogues/FO/scripts/cat_rech_correspondance.php?zone=FR&catalogue=PRO&lang=PL&marque=XXX&search_ref=_FILTERNAME_&Submit.x=0&Submit.y=0";
	private static final String SUCCESS_RESPONSE = "<table width=\"950\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"tab\">";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public SogefiChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<table width=\"950\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"tab\">\\s*"
				+ "<tr>\\s*"
			    + "<td width=\"\\d+\" align=\"center\" valign=\"middle\" class=\"tabtxtgras\">(.*?)</td>\\s*" // OEM
			    + "<td width=\"\\d+\" align=\"center\" valign=\"middle\" class=\"tabtxt\">(.*?)</td>\\s*" // OEM Number
			    + "<td width=\"\\d+\" align=\"center\" valign=\"middle\" class=\"tablien\">\\s*<a href=\"fichetechnique.html\"><a href=.*? class=\'tablien\'>(.*?)</a></a></td>\\s*" // Sogefi Number 
			    + "<td width=\"\\d+\" align=\"center\" valign=\"middle\" class=\"tabtxt\"></td>\\s*"
			    + "</tr>\\s*"
				+ "</table>");

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
