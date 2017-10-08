package filterscheckers;

import models.FilterEquivalents;

public class DonaldsonChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "https://catalog.donaldson.com/searchResults/en/C/_/N-2v?Ntk=cro&Ntt=_FILTERNAME_";
	
	public DonaldsonChecker() {
		super(SERVER_URL_STRING);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}
	
	
}
