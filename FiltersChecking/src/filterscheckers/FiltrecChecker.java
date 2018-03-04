package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class FiltrecChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Filtrec";
	private static final String SERVER_URL_STRING = "http://www.filtrec.it/cross/cross_en.aspx";
	private static final String SUCCESS_RESPONSE = "<th align=\"left\" scope=\"col\" style=\"width:200px;white-space:nowrap;\">";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "xpartno";
	private static final String SEARCH_BUTTON_ID = "Button2";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);
	
	
	public FiltrecChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<tr style=\"color:Black;background-color:.*?;height:30px;\">\\s*"
				+ "<td align=\"left\" style=\"width:200px;white-space:nowrap;\">\\s*"
				+ "(.*?)\\s*" // OEM Number
				+ "</td>\\s*"
				+ "<td align=\"left\" style=\"width:300px;white-space:nowrap;\">\\s*"
				+ "(.*?)\\s*" // OEM
				+ "</td>\\s*"
				+ "<td align=\"left\" style=\"width:270px;white-space:nowrap;\">\\s*"
				+ "(.*?)\\s*" // Filtrec Number
				+ "</td>\\s*"
				+ "</tr>\\s*");
		
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
