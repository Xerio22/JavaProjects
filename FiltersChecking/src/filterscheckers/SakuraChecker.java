package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class SakuraChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Sakura";
	private static final String SERVER_URL_STRING = "http://sakurafilter.com/index.php/product";
	private static final String SUCCESS_RESPONSE = "Ref Number";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "nomor_0";
	private static final String SEARCH_BUTTON_ID = "yt0";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);
	
	public SakuraChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	// TODO nie chce dzialac regex
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
					"<tr class=\"even\">\\s*"
                     + "<td>\\s*"
                     +   "(.*?)" // OEM Number
                     + "</td>\\s*"
                     + "<td>\\s*"
                     +   "(.*?)" // OEM
                     + "</td>\\s*"
                     + "<td>\\s*"
                     +   "<a href=\"/index\\.php/productdetail/index\\?snum=.*?&title=Sakura Number \\- .*?&id=2\">\\s*"
                     +     "(.*?)" // Sakura Number
                     +   "</a>\\s*"
                     + "</td>\\s*"
                     + "<td>\\s*"
                     +   ".*?"
                     + "</td>\\s*"
                     + "<td>\\s*"
                     +   "RELEASE\\s*PRODUCT\\s*" 
                     +   "<a href=\"/index\\.php/index\\.php/productdetail/index\\?snum=&id=6&title=Product Detail \\- \">\\s*"
                     +   "</a>\\s*"
                     + "</td>\\s*"
                   + "</tr>");
		
		
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
