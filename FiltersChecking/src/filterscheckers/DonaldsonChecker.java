package filterscheckers;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class DonaldsonChecker extends FilterChecker {

	private static final String SERVER_URL_STRING = "https://catalog.donaldson.com/searchResults/en/C/_/N-2v?Ntk=cro&Ntt=_FILTERNAME_";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public DonaldsonChecker() {
		super(connectionHandler);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}
	
	
}
