package filterscheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import utils.Utils;

public class ServerConnectionHandler {
	private boolean isServerBlocked = false;
	private String serverUrlString;
	
	
	public ServerConnectionHandler(String serverUrlString) {
		this.serverUrlString = serverUrlString;
	}

	
	public String insertFilterNameIntoUrlString(String filterName) {
		return serverUrlString.replaceAll("_FILTERNAME_", filterName);
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
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
		}
		catch(Exception e){
			while(br == null){
				br = reconnect(br, uc);
			}
		}
		return br;
	}
	
	
	private BufferedReader reconnect(BufferedReader br, URLConnection uc) {
		try {
//			printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
//			printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
			
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
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		// Setup connection
		uc.setUseCaches(false);
		uc.setDefaultUseCaches(false);
		uc.setReadTimeout(10000);
		uc.setConnectTimeout(10000);
		
		return uc;
	}
	
	
	public boolean checkIsAnyReplacementPresent(String serverResponse) {
		if(serverResponse.contains(Utils.SUCCESS_RESPONSE)) {
			setServerBlocked(false);
			return true;
		}
		else if(serverResponse.contains(Utils.BLOCKED_BY_SERVER_RESPONSE)) {
//			printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
//			printInfo("Ponowna proba polaczenia nastapi za " + millisToMinutes(Utils.reconnect_time) + " minut", Color.RED);
			setServerBlocked(true);
		}
		else{
			setServerBlocked(false);
		}
		
		return false;
	}
	
	
	private void setServerBlocked(boolean isAppBlockedByServer) {
		isServerBlocked = isAppBlockedByServer;
	}
}
