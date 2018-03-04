package filterscheckers.JSBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

public class SedziszowChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Sędziszów";
	private static final String SERVER_URL_STRING = "http://pzlsedziszow.pl/katalogonline/index.php";
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String FAILURE_RESPONSE = "<td>BRAK</td>";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_CSS_SELECTOR = "#kontener_glowny > div:nth-child(2) > textarea";
	private static final String SEARCH_BUTTON_CSS_SELECTOR = "#kontener_glowny > div:nth-child(2) > input[type=\"submit\"]";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_CSS_SELECTOR, SEARCH_BUTTON_CSS_SELECTOR);
	
	public SedziszowChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE, FAILURE_RESPONSE);
	}

	public static String formToSubmit() {
		return "#tabela_menu_gorne > tbody > tr:nth-child(2) > td:nth-child(1) > form";
	}
	
	public static int optionToSelectIndex() {
		return 2;
	}
	
	public static String selectList() {
		return formToSubmit() + " > select";
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "<tr>\\s*"
				+ "<td>\\s*"
              	+ "<a href=\"index\\.php\\?akcja=pokaz_filtr&amp;filtr=.*?\">\\s*"
				+ "(.*?)\\s*" // Sedziszow Number
				+ "</a>\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "<b>\\s*"
				+ "(.*?)\\s*" // OEM Number
				+ "</b>\\s*"
				+ "</td>\\s*"
				+ "<td>\\s*"
				+ "(.*?)\\s*" // OEM
				+ "</td>\\s*"
				+ "</tr>"
			);
		
		
		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;
		while(m.find()){
			equivalentOEM = m.group(3);
			equivalentNumber = m.group(1);
			equivalentOEMNumber = m.group(2);

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
