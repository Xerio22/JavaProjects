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
		try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			// get rid of HTMLUnit logging messages
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
			
			configureWebClient(webClient);
			String serverResponse = execJS(webClient);
			
	        return serverResponse;
		}
		catch(Exception e) {
			e.printStackTrace();
			notifyObserverAboutChange(ServerConnectionHandler.RECONNECT_MESSAGE);
		}
		
		return "";
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
        webClient.getOptions().setThrowExceptionOnScriptError(false);
	}

	public String getFilterName() {
		return filterName;
	}
	

	private String execJS(WebClient webClient) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		notifyObserverAboutChange(ServerConnectionHandler.CONNECTING_MESSAGE);
		final HtmlPage pageToRequest = webClient.getPage(super.getServerUrlString());
		notifyObserverAboutChange(ServerConnectionHandler.CONNECTED_MESSAGE);
        
        final HtmlTextInput filterNameInput = (HtmlTextInput) pageToRequest.getElementById(this.inputFieldId);
        DomElement findCrosses = pageToRequest.getElementById(this.searchButtonId);
        
        filterNameInput.setValueAttribute(this.filterName);
       
        final HtmlPage page = findCrosses.click();
        
        return page != null ? page.asXml() : "";
	}
	
	
	private void notifyObserverAboutChange(String reconnectMessage) {
		setChanged();
		notifyObservers(reconnectMessage);
	}
}
