package connectionhandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLBasedConnectionHandler extends ServerConnectionHandler {

	private String currentUrlString;

	public URLBasedConnectionHandler(String serverUrlString) {
		super(serverUrlString);
	}

	
	public void supplyFilterOEMnumber(String filterName) {
		insertFilterNameIntoUrlString(filterName);
	}

	
	private void insertFilterNameIntoUrlString(String filterName) {
		currentUrlString = super.getServerUrlString().replaceAll("_FILTERNAME_", filterName);
	}


	public String getServerResponse() {
		
		BufferedReader br = createConnectionAndOpenStream();
		
		if(br == null) {
			return ""; // return blank response
		}
		
		String serverResponse = getResponseFromServerAndGetRidOfTabs(br);
		
		closeBufferedReader(br);
		
		return serverResponse;
	}
	
	
	private BufferedReader createConnectionAndOpenStream() {
		// Create URL and open connection
		URLConnection uc = createURLConnectionFromString(currentUrlString);
			
		BufferedReader br = tryToGetBufferedReader(uc);
		
		return br;
	}


	private BufferedReader tryToGetBufferedReader(URLConnection uc) {
		BufferedReader br = null;
		
		try{
			notifyObserverAboutChange(ServerConnectionHandler.CONNECTING_MESSAGE);
			
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			
			notifyObserverAboutChange(ServerConnectionHandler.CONNECTED_MESSAGE);
		}
		catch(Exception e){
			int tries = 0;
			while(br == null && tries++ < ServerConnectionHandler.RECONNECT_TRIES){
				notifyObserverAboutChange(ServerConnectionHandler.RECONNECT_MESSAGE);
				
				br = reconnect(br, uc);
			}
		}
		return br;
	}


	private BufferedReader reconnect(BufferedReader br, URLConnection uc) {
		try {
			Thread.sleep(10000);
			
			uc = createURLConnectionFromString(currentUrlString);
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return br;
	}
	
	
	private String getResponseFromServerAndGetRidOfTabs(BufferedReader br) {
		String serverResponse = getResponseFromServer(br);
		serverResponse = getRidOfTabs(serverResponse);
		
		return serverResponse;
	}
	
	
	private String getResponseFromServer(BufferedReader br) {
		StringBuffer sb = new StringBuffer();
		
		br.lines().forEach(line -> sb.append(line));
		
		return sb.toString();
	}


	private String getRidOfTabs(String serverResponse) {
		return serverResponse.replaceAll("\\t+", "");
	}
	

	private void closeBufferedReader(BufferedReader br) {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private URLConnection createURLConnectionFromString(String urlString) {
		URL hifiUrl = null;
		URLConnection uc = null;
		try {
			hifiUrl = new URL(urlString);
			uc = hifiUrl.openConnection();
			
			setupConnection(uc);
			
		} catch (IOException e2) {
			e2.printStackTrace();
			notifyObserverAboutChange(ServerConnectionHandler.URL_ERROR);
		}
		
		return uc;
	}
	
	
	private void setupConnection(URLConnection uc) {
		uc.setUseCaches(false);
		uc.setDefaultUseCaches(false);
		uc.setReadTimeout(10000);
		uc.setConnectTimeout(10000);
	}
	
	
	private void notifyObserverAboutChange(String reconnectMessage) {
		setChanged();
		notifyObservers(reconnectMessage);
	}
}
