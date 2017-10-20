package filterscheckers;

import connectionhandlers.URLBasedConnectionHandler;
import connectionhandlers.ServerConnectionHandler;
import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {
	private static final String CHECKER_NAME = "Baldwin";
	private static final String SERVER_URL_STRING = "_FILTERNAME_";
	private static final String SUCCESS_RESPONSE = "some_success_response";
	private static final String BLOCKED_BY_SERVER_RESPONSE = "some_blocked_by_server_response";
	private static final ServerConnectionHandler connectionHandler = new URLBasedConnectionHandler(SERVER_URL_STRING);

	public BaldwinChecker() {
		super(connectionHandler, SUCCESS_RESPONSE, BLOCKED_BY_SERVER_RESPONSE);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		

		return null;
	}

	@Override
	protected String getCheckerName() {
		return CHECKER_NAME;
	}
}
