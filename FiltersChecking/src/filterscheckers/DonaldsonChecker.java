package filterscheckers;

import controllers.MyObserver;
import models.FilterEquivalents;

public class DonaldsonChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "https://catalog.donaldson.com/searchResults/en/C/_/N-2v?Ntk=cro&Ntt=_FILTERNAME_";
	
	public DonaldsonChecker(MyObserver obs) {
		super(SERVER_URL_STRING, obs);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}
	
	
}
