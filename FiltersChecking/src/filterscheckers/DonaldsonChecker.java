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
		Pattern p = Pattern.compile("<table class=\"table table-striped table-hover clear-marginBtm\" width=\"100%\" id=\"searchResults\">"
				+ "<thead><tr align=\"left\">"
				+ "<th class=\"hidden-print item-mark\"></th>"
				+ "<th>Image</th>"
				+ "<th class=\"p-brand\">Manufacturer</th>"
				+ "<th class=\"u-identifier\">Manufacturer Number</th>"
				+ "<th class=\"u-identifier\">Donaldson Number</th>"
				+ "<th class=\"p-name\">Product</th>"
				+ "<th class=\"p-notes\">Notes</th>"
				+ ".*?</tr></thead>"
				+ "<tbody>"
				+ "<!-- Loop Item -->"
				+ "(<tr class=\"item h-product\" align=\"left\">" // row
				+ "<td class=\"hidden-print item-mark\">(\\s*<input type=\"checkbox\" .*\"\\s*/>\\s*)?</td>"
				+ "<td>"
				+ "<a class=\"hidden-print u-url\" href=\".*?\">"
				+ "<img class=\"u-photo\" src=\".*?\" alt=\".*?\" title=\".*?\" />"
				+ "</a>"
				+ "<img class=\"visible-print-block\" src=\".*?\" alt=\".*?\" />"
				+ "</td>"
				+ "<td class=\"p-brand\">(.*?)</td>" // oem
				+ "<td class=\"u-identifier\"><span class=\"type hidden\">OEM</span><span class=\"value\">(.*?)</span></td>" // oem number
				+ "<td class=\"u-identifier\">"
				+ "<span class=\"type hidden\">UPC</span>"
				+ "<a class=\"hidden-print underline value u-url\" href=\".*?\">.*?</a>"
				+ "<span class=\"visible-print-inline\">(.*?)</span>" // donaldson number
				+ "</td>"
				+ "<td class=\"p-name\">(.*?)</td>" // product
				+ "<td class=\"p-notes\">(.*?)</td>" // notes
				+ "<td class=\"operation inventory\">"
				+ "<div class=\"hidden-print\">"
				+ "</div>"
				+ "</td>"
				+ "<td class=\"operation actions\">"
				+ "<div class=\"hidden-print\">"
				+ "</div>"
				+ "</td>"
				+ "</tr>)*?"
				+ "</tbody></table>");

		Matcher m = p.matcher(serverResponse);
		
		String equivalentOEMNumber = null;
		String equivalentOEM = null;
		String equivalentNumber = null;
		String equivalentProduct = null;
		String equivalentNotes = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;
		while(m.find()){
			Pattern pp = Pattern.compile(
					"<td class=\"p-brand\">(.*?)</td>" // oem
					+ "<td class=\"u-identifier\"><span class=\"type hidden\">OEM</span><span class=\"value\">(.*?)</span></td>" // oem number
					+ "<td class=\"u-identifier\">"
					+ "<span class=\"type hidden\">UPC</span>"
					+ "<a class=\"hidden-print underline value u-url\" href=\".*?\">.*?</a>"
					+ "<span class=\"visible-print-inline\">(.*?)</span>" // donaldson number
					+ "</td>"
					+ "<td class=\"p-name\">(.*?)</td>" // product
					+ "<td class=\"p-notes\">(.*?)</td>" // notes
			);
			
			Matcher mm = pp.matcher(m.group(1));
			
			while(mm.find()){
				equivalentOEM = mm.group(1);
				equivalentOEMNumber = mm.group(2);
				equivalentNumber = mm.group(3);
				equivalentProduct = mm.group(4);
				equivalentNotes = mm.group(5);

				equivalentsForThisOem.createAndAddEquivalent(
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
		}
		
		return equivalentsForThisOem;
	}
	
	@Override
	public String getCheckerName() {
		return CHECKER_NAME;
	}
}
