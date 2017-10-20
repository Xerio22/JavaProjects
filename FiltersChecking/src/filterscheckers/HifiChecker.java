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
		Pattern p = Pattern.compile("<table class=\"table table-hover\">"
				+ "<thead><tr>"
				+ "<th>Cross references</th>"
				+ "<th>Brand</th>"
				+ "<th>N° HIFI\\s*</th>"
				+ "<th></th></tr></thead>"
				+ "<tbody>"
				+ "(<tr class=\"product-line img\">"
				+ "<td>([a-zA-Z_0-9 -\\./\\\\]+)</td>"
				+ "<td>([a-zA-Z_0-9 -\\./\\(\\)=]+)</td>"
				+ "<td>([a-zA-Z_0-9 -]+)</td>"
				+ "<td>.*</td>"
				+ "</tr>)+</tbody></table>");

		Matcher m = p.matcher(serverResponse);
		
		String searchResultOEM = null;
		String equivalentBrand = null;
		String equivalentNumber = null;
		
		FilterEquivalents equivalentsForThisOem = new FilterEquivalents();
		
		int propIdx = 1;
		if(m.find()){
			Pattern pp = Pattern.compile(
					"<tr class=\"product-line img\">"
					+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\\\]]+)</td>"
					+ "<td>([[a-z][A-Z][_][0-9][ ][-][\\.][/][\\(][\\)][=]]+)</td>"
					+ "<td>([[a-z][A-Z][_][0-9][ ][-]]+)</td>"
					+ "<td><a href=.*?></a></td>"
					+ "</tr>");
			
			Matcher mm = pp.matcher(m.group(1));
			
			while(mm.find()){
				searchResultOEM = mm.group(1);
				equivalentBrand = mm.group(2); // TODO mozna zastapic brand.name()
				equivalentNumber = mm.group(3);

				equivalentsForThisOem.createAndAddEquivalent(searchResultOEM, equivalentBrand, equivalentNumber, propIdx);
				
				propIdx++;
			}
		}
		
		return equivalentsForThisOem;
	}

	@Override
	protected String getCheckerName() {
		return CHECKER_NAME;
	}
}
