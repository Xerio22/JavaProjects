package filterscheckers;

import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "_FILTERNAME_";
	
	public BaldwinChecker() {
		super(SERVER_URL_STRING);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}

}
