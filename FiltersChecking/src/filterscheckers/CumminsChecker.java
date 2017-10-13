package filterscheckers;

import models.FilterEquivalents;

public class CumminsChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "_FILTERNAME_";
	
	public CumminsChecker() {
		super(SERVER_URL_STRING);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}


}
