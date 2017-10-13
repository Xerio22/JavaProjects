package filterscheckers;

import connectionhandlers.URLBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {

	private static final String SERVER_URL_STRING = "_FILTERNAME_";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);
	
	public BaldwinChecker() {
		super(connectionHandler);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}

}
