package filterscheckers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import connectionhandlers.JSBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Baldwin";
	private static final String SERVER_URL_STRING = "http://stage.catalog.baldwinfilter.com/Cross-Reference";
	private static final String SUCCESS_RESPONSE = "<th>Part Number\\s*</th><th>Manufacturer\\s*</th><th>Baldwin\\s*</th><th>Qualifiers</th>";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final String INPUT_FIELD_ID = "fieldCrossReference1";
	private static final String SEARCH_BUTTON_ID = "btnFindCrosses";
	private static final ServerConnectionHandler connectionHandler = new JSBasedConnectionHandler(SERVER_URL_STRING, INPUT_FIELD_ID, SEARCH_BUTTON_ID);

	public BaldwinChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		System.out.println(serverResponse);
		
		Pattern p = Pattern.compile(
			"<tr id=\".*?\">\\s*"
			+ "<td class=\"comp_no\">\\s*"
			+ "(.*)?\\s*" // oem number
			+ "</td>\\s*"
			+ "<td class=\"mfg\">\\s*"
			+ "(.*)?\\s*" // oem
			+ "</td>\\s*"
			+ "<td class=\"partLink partLeft\">\\s*"
			+ "<a class=\"btnViewProductDetail\" href=\"#\">\\s*"
			+ "(.*)?\\s*" // baldwin number
			+ "</a>\\s*"
			+ "</td>\\s*"
			+ "<td class=\"partComment\"/>\\s*"
			+ "</tr>\\s*");

		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
//		String equivalentQualifiers = null;
		
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

	@Override
	public String getCheckerName() {
		return CHECKER_NAME;
	}
	
	@Override
	public FilterEquivalents getEquivalentsFor(Filter filter) {
		FilterEquivalents equivalents = super.getEquivalentsFor(filter);
		
		if(filter.getOemNumber().startsWith("0")){
			Filter filterWithoutLeadingZeros = getFilterEquivalentForSuppliedFilterUsingOemNumberWithoutLeadingZeros(filter);
			equivalents.addEquivalent(filterWithoutLeadingZeros);
		}
		
		return equivalents;
	}
}
