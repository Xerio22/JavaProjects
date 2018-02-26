package connectionhandlers;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class JSBasedConnectionHandler extends ServerConnectionHandler {

	private String filterName;
	private String inputFieldId;
	private String searchButtonId;
	
	public JSBasedConnectionHandler(String serverUrlString, String inputFieldId, String searchButtonId) {
		super(serverUrlString);
		this.inputFieldId = inputFieldId;
		this.searchButtonId = searchButtonId;
	}

	@Override
	public void supplyFilterOEMnumber(String searchedFilterOEMnumber) {
		this.filterName = searchedFilterOEMnumber;
	}

	@Override
	public String getServerResponse() {
		return prepareWebClientAndRunJSToGetResponse();
	}

	
	private String prepareWebClientAndRunJSToGetResponse() {
		String serverResponse = "";
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		try {
			// get rid of HTMLUnit logging messages
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
			
			configureWebClient(webClient);
			serverResponse = execJS(webClient);
			
	        return serverResponse;
		}
		catch(Exception e) {
			e.printStackTrace();
			int tries = 0;
			while(serverResponse.equals("") && tries++ < ServerConnectionHandler.RECONNECT_TRIES){
				notifyObserverAboutChange(ServerConnectionHandler.RECONNECT_MESSAGE);
				
				serverResponse = reconnect(webClient);
			}
		}
		finally {
			webClient.close();
		}
		
		return serverResponse;
	}
	
	
	private void configureWebClient(WebClient webClient) {
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(true); // TODO zmienic spowrotem na false do wersji produkcyjnej
	}
	
	
	private String reconnect(WebClient webClient) {
		String serverResponse = "";
		try {
			Thread.sleep(10000);
			serverResponse = execJS(webClient);
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return serverResponse;
	}
	

	public String getFilterName() {
		return filterName;
	}
	

	private String execJS(WebClient webClient) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		notifyObserverAboutChange(ServerConnectionHandler.CONNECTING_MESSAGE);
		final HtmlPage pageToRequest = webClient.getPage(super.getServerUrlString());
		notifyObserverAboutChange(ServerConnectionHandler.CONNECTED_MESSAGE);
        
        final HtmlTextInput filterNameInput = (HtmlTextInput) pageToRequest.getElementById(this.inputFieldId);
        DomElement findCrossesButton = pageToRequest.getElementById(this.searchButtonId);
        
        if(notFoundById(findCrossesButton)) {
        	findCrossesButton = getElementByClassName(pageToRequest); // as class	
        }
        
        filterNameInput.setValueAttribute(this.filterName);
       
        final HtmlPage page = findCrossesButton.click();
        webClient.waitForBackgroundJavaScriptStartingBefore(0);
        
        return page != null ? page.asXml() : "";
	}
	
	
	private DomElement getElementByClassName(HtmlPage pageToRequest) {
		return (DomElement) pageToRequest.querySelectorAll("." + this.searchButtonId).get(0);
	}

	private boolean notFoundById(DomElement findCrosses) {
		return findCrosses == null;
	}

	private void notifyObserverAboutChange(String reconnectMessage) {
		setChanged();
		notifyObservers(reconnectMessage);
	}
}
