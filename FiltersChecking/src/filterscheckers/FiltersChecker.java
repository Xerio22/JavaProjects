package filterscheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import models.Filter;
import utils.Utils;

public abstract class FiltersChecker {
	private boolean isServerBlocked = false;
	
	public abstract boolean isFilterInDatabase(Filter filterToCheck);
	
	public abstract Filter getReplacementFor(Filter filter);
	
	protected boolean checkForFilterInFollowingServer(String filterName, String serverUrlString) {

//		printInfo(Utils.numberOfCheckedFilters + ". Waiting for connection...");
		String serverResponse = getServerResponseFor(filterName, serverUrlString);
//		printInfo("connected");
		
		boolean isFilterInDatabase = checkIsAnyReplacementPresent(serverResponse, filterName);
		
		return isFilterInDatabase;
	}

	private String getServerResponseFor(String name, String urlString) {
		
		// Create URL and open connection
		URLConnection uc = createURLConnectionFromString(urlString);
			
		// Get server response and put it into String Buffer
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			
			br.lines().forEach(line -> sb.append(line));
			br.close();
		}
		catch(Exception e){
			while(br == null){
				try {
//					printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
//					printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
					
					Thread.sleep(10000);
					
					uc = createURLConnectionFromString(urlString);
					br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		
		return sb.toString();
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
	
	
	private boolean checkIsAnyReplacementPresent(String serverResponse, String name) {
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
	
	private int millisToMinutes(int millis){
		return millis / 60000;
	}
}
