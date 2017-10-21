package filterscheckers;

import java.util.Observable;
import java.util.Observer;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.FilterEquivalents;
import models.ObjectWithMessage;

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
		
		// TODO notifying FilterCheckerObserver about change (about end of getting equivalents) 
		// we can use getCheckerName method in observer to check which checker ended
		ObjectWithMessage<Filter> owm = new ObjectWithMessage<>(filter, "up");
		notifyCheckerUpdate(owm);
		
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
			setChanged();
			notifyObservers(EQUIVALENT_FOUND_MESSAGE);
			setServerBlocked(false);
			return true;
		}
		else if(isIPBlockedByServer(serverResponse)) {
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
	
	
	private void notifyCheckerUpdate(ObjectWithMessage<?> owm) {
		setChanged();
		notifyObservers(owm);
	}
	
	protected abstract FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse);
	protected abstract String getCheckerName();
}
