package filterscheckers;

import controllers.MyObserver;
import models.FilterEquivalents;

public class BaldwinChecker extends FilterChecker {

	public static final String SERVER_URL_STRING = "_FILTERNAME_";
	
	public BaldwinChecker(MyObserver obs) {
		super(SERVER_URL_STRING, obs);
	}

	@Override
	protected FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse) {
		return null;
	}

}
