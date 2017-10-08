package filterscheckers;

import java.util.List;

import models.Filter;
import models.FilterEquivalents;

public class DonaldsonChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "https://catalog.donaldson.com/searchResults/en/C/_/N-2v?Ntk=cro&Ntt=_FILTERNAME_";
	
	public DonaldsonChecker() {
		super.setServerUrlString(SERVER_URL_STRING);
	}
}
