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
	public static final String STATE_BAD_OEM_TAG = "Bad_oem_tag";
	private static final String UNSET_FAILURE_RESPONSE = "_ufr_";
	
	private boolean isServerBlocked = false;
	private String successResponse;
	private String failureResponse;
	private String blockedByServerResponse;
	private ServerConnectionHandler serverConnectionHandler;
	private String state;
	private Filter filter;
	
	public FilterChecker(ServerConnectionHandler serverConnectionHandler, String successResponse, String blockedByServerResponse){
		this.serverConnectionHandler = serverConnectionHandler;
		this.successResponse = successResponse;
		this.blockedByServerResponse = blockedByServerResponse;
		this.failureResponse = UNSET_FAILURE_RESPONSE;
	}

	public FilterChecker(ServerConnectionHandler serverConnectionHandler, String successResponse, String blockedByServerResponse, String failureResponse){
		this.serverConnectionHandler = serverConnectionHandler;
		this.successResponse = successResponse;
		this.blockedByServerResponse = blockedByServerResponse;
		this.failureResponse = failureResponse;
	}
	
	
	public FilterEquivalents getEquivalentsFor(Filter filter) {
		this.filter = filter;
		String searchedFilterOEMnumber = filter.getOemNumber();
		
		if(searchedFilterOEMnumber == null){
			setState(STATE_BAD_OEM_TAG);
			notifyCheckerUpdate();
			return new FilterEquivalents();
		}
		
		serverConnectionHandler.supplyFilterOEMnumber(searchedFilterOEMnumber);
		
		FilterEquivalents fe = findEquivalentsOnServer();

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
		if(isFailureResponseSet()) 
			return serverResponse.contains(successResponse) || !serverResponse.contains(failureResponse);
		
		return serverResponse.contains(successResponse);
	}
	
	
	private boolean isFailureResponseSet() {
		return !failureResponse.equals(UNSET_FAILURE_RESPONSE);
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
	
//	public List<String> getColumnsNames() {
//	return new ArrayList<>(
//		Arrays.asList(
//			"Znaleziony numer OEM",
//			"OEM",
//			"Zamiennik"
//		)
//	);
//}
	
	protected abstract FilterEquivalents parseServerResponseAndGetEquivalents(String serverResponse);
	public abstract String getCheckerName();
}
