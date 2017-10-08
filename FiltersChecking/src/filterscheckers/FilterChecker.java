package filterscheckers;

import models.Filter;
import models.FilterEquivalents;

public abstract class FilterChecker {
	
	private ServerConnectionHandler serverConnectionHandler;
	
	public FilterChecker(String serverRawUrlString){
		serverConnectionHandler = new ServerConnectionHandler(serverRawUrlString);
	}

	public FilterEquivalents getEquivalentsFor(Filter filter) {
		String searchedFilterOEMnumber = filter.getOemNumber();
		
		serverConnectionHandler.insertFilterNameIntoUrlString(searchedFilterOEMnumber);
		
		FilterEquivalents fe = findEquivalentsOnServerUsingPreparedUrl();
		
		return fe;
	}
	
	
	private FilterEquivalents findEquivalentsOnServerUsingPreparedUrl() {
		
		String serverResponse = serverConnectionHandler.getServerResponse();
		boolean isAnyOEMReplacementPresent = serverConnectionHandler.checkIsAnyReplacementPresent(serverResponse);
		
		if(!isAnyOEMReplacementPresent){
			return null;
		}
		else{
			return this.parseServerResponseAndGetEquivalents(serverResponse);
		}
	}
	
	
	protected abstract FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse);
}
