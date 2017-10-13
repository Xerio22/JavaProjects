package filterscheckers;

import java.util.Observer;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.FilterEquivalents;

public abstract class FilterChecker {
	private ServerConnectionHandler serverConnectionHandler;
	
	public FilterChecker(ServerConnectionHandler serverConnectionHandler){
		this.serverConnectionHandler = serverConnectionHandler;
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
	
	public void putObserver(Observer obs){
		serverConnectionHandler.addObserver(obs);
	}
}
