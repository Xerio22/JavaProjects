package filterscheckers;

import connectionhandlers.ServerConnectionHandler;
import connectionhandlers.URLBasedConnectionHandler;
import models.FilterEquivalents;

public class CumminsChecker extends FilterChecker {

	private static final String SERVER_URL_STRING = "_FILTERNAME_";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public CumminsChecker() {
		super(connectionHandler);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}


}
