package connectionhandlers;

import java.util.Observable;

public abstract class ServerConnectionHandler extends Observable {
	public static final String RECONNECT_MESSAGE = "Reconnect";
	public static final String CONNECTING_MESSAGE = "Connecting";
	public static final String CONNECTED_MESSAGE = "Connected";
	public static final String URL_ERROR = "URL_error";
	private String serverUrlString;
	
	public ServerConnectionHandler(String serverUrlString) {
		this.serverUrlString = serverUrlString;
	}
	
	public abstract void supplyFilterOEMnumber(String searchedFilterOEMnumber);
	public abstract String getServerResponse();
	
	public String getServerUrlString(){
		return serverUrlString;
	}
}
