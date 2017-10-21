package filterscheckers;

import java.util.Observable;
import java.util.Observer;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.FilterEquivalents;

public abstract class FilterChecker extends Observable {
	public static final String STATE_EQUIVALENT_FOUND = "Equiv_found";
	public static final String STATE_EQUIVALENT_NOT_FOUND = "Equiv_not_found";
	public static final String STATE_BLOCKED_BY_SERVER = "Blocked";
	
	private boolean isServerBlocked = false;
	private String successResponse;
	private String blockedByServerResponse;
	private ServerConnectionHandler serverConnectionHandler;
	private String state;
	private Filter filter;
	
	
	public FilterChecker(ServerConnectionHandler serverConnectionHandler, String successResponse, String blockedByServerResponse){
		this.serverConnectionHandler = serverConnectionHandler;
		this.successResponse = successResponse;
		this.blockedByServerResponse = blockedByServerResponse;
	}

	
	public FilterEquivalents getEquivalentsFor(Filter filter) {
		this.filter = filter;
		String searchedFilterOEMnumber = filter.getOemNumber();
		
		serverConnectionHandler.supplyFilterOEMnumber(searchedFilterOEMnumber);
		
		FilterEquivalents fe = findEquivalentsOnServer();
		
		// TODO notifying FilterCheckerObserver about change (about end of getting equivalents) 
		// we can use getCheckerName method in observer to check which checker ended
		notifyCheckerUpdate();
		
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
		if(isEquivalentFound(serverResponse)) {
			setState(STATE_EQUIVALENT_FOUND);
			setServerBlocked(false);
			return true;
		}
		else if(isIPBlockedByServer(serverResponse)) {
			setState(STATE_BLOCKED_BY_SERVER);
//			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setState(STATE_EQUIVALENT_NOT_FOUND);
			setServerBlocked(false);
		}
		
		return false;
	}
	
	
	private void setState(String state) {
		this.state = state;
	}


	private boolean isEquivalentFound(String serverResponse) {
		return serverResponse.contains(successResponse);
	}

	
	private boolean isIPBlockedByServer(String serverResponse) {
		return serverResponse.contains(blockedByServerResponse);
	}
	

	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}
	
	
	public void putObserverForConnection(Observer obs){
		serverConnectionHandler.addObserver(obs);
	}
	
	
	public void putObserverForFilterChecker(Observer obs){
		this.addObserver(obs);
	}
	
	
	private void notifyCheckerUpdate() {
		setChanged();
		notifyObservers();
	}
	
	
	public void removeAttachedObservers() {
		this.deleteObservers();
		serverConnectionHandler.deleteObservers();
	}
	
	
	public String getState() {
		return state;
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	protected abstract FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse);
	protected abstract String getCheckerName();
}
