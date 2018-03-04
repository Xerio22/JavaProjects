package filterscheckers.URLBased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import filterscheckers.FilterChecker;
import models.FilterEquivalents;

// TODO potrzebny jakis numer zeby cos znalazl
public class PurfluxChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Purflux";
	private static final String SERVER_URL_STRING = "http://www.sogefifilterdivision.com/catalogues/FO/scripts/cat_fich_filtre.php?zone=FR&catalogue=PFX&lang=GB&searchref=_FILTERNAME_&Submit.x=0&Submit.y=0&old_marque=&ref_filtre=XXX&valid=OK";
	private static final String SUCCESS_RESPONSE = "";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler SERVER_CONNECTION_HANDLER = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public PurfluxChecker() {
		super(CHECKER_NAME, SERVER_CONNECTION_HANDLER, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}
	
	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		Pattern p = Pattern.compile(
				  "");

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
