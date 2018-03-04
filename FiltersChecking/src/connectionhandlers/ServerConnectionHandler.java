package connectionhandlers;

import java.util.Observable;

public abstract class ServerConnectionHandler extends Observable {
	public static final String RECONNECT_MESSAGE = "Reconnect";
	public static final String CONNECTING_MESSAGE = "Connecting";
	public static final String CONNECTED_MESSAGE = "Connected";
	public static final String URL_ERROR = "URL_error";
	public static int RECONNECT_TIME = 10000;
	public static int RECONNECT_TRIES = 5;
	private String serverUrlString;
	
	public ServerConnectionHandler(String serverUrlString) {
		this.serverUrlString = serverUrlString;
	}
	
	public String getServerUrlString(){
		return serverUrlString;
	}
	
	public static int getReconnectTimeInSeconds(){
		return RECONNECT_TIME / 1000;
	}
	
	public abstract void supplyFilterOEMnumber(String searchedFilterOEMnumber);
	public abstract String getServerResponse();
}
