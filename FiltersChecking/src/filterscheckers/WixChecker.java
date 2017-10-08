package filterscheckers;

import models.FilterEquivalents;

public class WixChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "_FILTERNAME_";
	
	public WixChecker() {
		super(SERVER_URL_STRING);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}



}
