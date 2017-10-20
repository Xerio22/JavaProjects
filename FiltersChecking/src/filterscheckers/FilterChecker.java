package filterscheckers;

import java.util.Observable;
import java.util.Observer;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.FilterEquivalents;

public abstract class FilterChecker extends Observable {
	public static final String EQUIVALENT_FOUND_MESSAGE = "Equiv_found";
	public static final String EQUIVALENT_NOT_FOUND_MESSAGE = "Equiv_not_found";
	public static final String BLOCKED_BY_SERVER_MESSAGE = "Blocked";
	
	private boolean isServerBlocked = false;
	private String successResponse;
	private String blockedByServerResponse;
	private ServerConnectionHandler serverConnectionHandler;
	
	
	public FilterChecker(ServerConnectionHandler serverConnectionHandler, String successResponse, String blockedByServerResponse){
		this.serverConnectionHandler = serverConnectionHandler;
		this.successResponse = successResponse;
		this.blockedByServerResponse = blockedByServerResponse;
	}

	
	public FilterEquivalents getEquivalentsFor(Filter filter) {
		String searchedFilterOEMnumber = filter.getOemNumber();
		
		serverConnectionHandler.supplyFilterOEMnumber(searchedFilterOEMnumber);
		
		FilterEquivalents fe = findEquivalentsOnServer();
		
		// TODO notifying ConnectionObserver about change (about end of getting equivalents) we can use getCheckerName method in observer to check which checker ended
		notifyCheckerUpdate(this);
		
		return fe;
	}
	
	
	private FilterEquivalents findEquivalentsOnServer() {
		// TODO maybe put serverResponse as a field and access in checkers by using getter?
		String serverResponse = serverConnectionHandler.getServerResponse();

		if(isAnyReplacementPresentInServerResponse(serverResponse)){
			return this.parseServerResponseAndGetEquivalents(serverResponse);
		}
		else{ 
			return new FilterEquivalents();
		}
	}

	
	public boolean isAnyReplacementPresentInServerResponse(String serverResponse) {
		if(serverResponse.contains(successResponse)) {
			setChanged();
			notifyObservers(EQUIVALENT_FOUND_MESSAGE);
			setServerBlocked(false);
			return true;
		}
		else if(serverResponse.contains(blockedByServerResponse)) {
			setChanged();
			notifyObservers(BLOCKED_BY_SERVER_MESSAGE);
//			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setChanged();
			notifyObservers(EQUIVALENT_NOT_FOUND_MESSAGE);
			setServerBlocked(false);
		}
		
		return false;
	}
	
	
	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}
	
	
	public void putObserver(Observer obs){
		serverConnectionHandler.addObserver(obs);
	}
	
	
	private void notifyCheckerUpdate(FilterChecker checker) {
		setChanged();
		notifyObservers(checker);
	}
	
	protected abstract FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse);
	protected abstract String getCheckerName();
}
