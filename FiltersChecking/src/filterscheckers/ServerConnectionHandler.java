package filterscheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;

import utils.Utils;

public class ServerConnectionHandler extends Observable {
	private boolean isServerBlocked = false;
	private String serverUrlString;
	
	
	public ServerConnectionHandler(String serverUrlString) {
		this.serverUrlString = serverUrlString;
	}

	
	public void insertFilterNameIntoUrlString(String filterName) {
		serverUrlString = serverUrlString.replaceAll("_FILTERNAME_", filterName);
	}

	
	public String getServerResponse() {
		
		// Create URL and open connection
		URLConnection uc = createURLConnectionFromString(serverUrlString);
			
		BufferedReader br = tryToGetBufferedReader(uc);
		
		String serverResponse = getResponseFromServerAndGetRidOfTabs(br);
		
		closeBufferedReader(br);
		
		return serverResponse;
	}
	
	
	private BufferedReader tryToGetBufferedReader(URLConnection uc) {
		BufferedReader br = null;
		
		try{
			setChanged();
			notifyObservers("Connecting");
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));

			setChanged();
			notifyObservers("Connected");
		}
		catch(Exception e){
			while(br == null){
				setChanged();
				notifyObservers("Reconnect");
				br = reconnect(br, uc);
			}
		}
		return br;
	}
	
	
	private BufferedReader reconnect(BufferedReader br, URLConnection uc) {
		try {
			Thread.sleep(10000);
			
			uc = createURLConnectionFromString(serverUrlString);
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
			setChanged();
			notifyObservers("URL_error");
		}
		
		return uc;
	}
	
	
	private void setupConnection(URLConnection uc) {
		uc.setUseCaches(false);
		uc.setDefaultUseCaches(false);
		uc.setReadTimeout(10000);
		uc.setConnectTimeout(10000);
	}


	public boolean checkIsAnyReplacementPresent(String serverResponse) {
		if(serverResponse.contains(Utils.SUCCESS_RESPONSE)) {
			setChanged();
			notifyObservers("Equiv_found");
			setServerBlocked(false);
			return true;
		}
		else if(serverResponse.contains(Utils.BLOCKED_BY_SERVER_RESPONSE)) {
			setChanged();
			notifyObservers("Blocked");
//			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setChanged();
			notifyObservers("Equiv_not_found");
			setServerBlocked(false);
		}
		
		return false;
	}
	
	
	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}
}
