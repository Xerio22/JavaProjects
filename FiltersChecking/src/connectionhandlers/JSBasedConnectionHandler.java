package connectionhandlers;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import filterscheckers.JSBased.SedziszowChecker;

public class JSBasedConnectionHandler extends ServerConnectionHandler {

	private String filterName;
	private String inputFieldId;
	private String searchButtonIdentificator; // may be ID, NAME or css selector
	
	public JSBasedConnectionHandler(String serverUrlString, String inputFieldId, String searchButtonId) {
		super(serverUrlString);
		this.inputFieldId = inputFieldId;
		this.searchButtonIdentificator = searchButtonId;
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
        webClient.getOptions().setThrowExceptionOnScriptError(false);
	}
	
	
	private String reconnect(WebClient webClient) {
		String serverResponse = "";
		try {
			Thread.sleep(ServerConnectionHandler.RECONNECT_TIME);
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
        
        DomElement filterNameInput = null;
        if(pageDoesNotRequireExecutingJS()) {
        	filterNameInput = (HtmlTextInput) findElementOnPageUsingIdentificator(pageToRequest, this.inputFieldId);
	        ((HtmlTextInput)filterNameInput).setValueAttribute(this.filterName);
        }
        else {
        	handleSedziszow(pageToRequest, filterNameInput);
        }
        
        DomElement findCrossesButton = findElementOnPageUsingIdentificator(pageToRequest, this.searchButtonIdentificator);
        final HtmlPage page = findCrossesButton.click();
        webClient.waitForBackgroundJavaScriptStartingBefore(0);
        
        notifyObserverAboutChange(ServerConnectionHandler.CONNECTED_MESSAGE);
        
        return page != null ? page.asXml() : "";
	}

	private boolean pageDoesNotRequireExecutingJS() {
		 return !hasName("pzlsedziszow");
	}

	private boolean hasName(String name) {
		return super.getServerUrlString().contains(name);
	}

	private DomElement findElementOnPageUsingIdentificator(HtmlPage pageToRequest, String identificator) {
		DomElement element = pageToRequest.getElementById(identificator);
        
        if(notFoundInDom(element)) {
        	element = getElementByName(pageToRequest, identificator); // find by name (first user in Sakura)
        	
        	if(notFoundInDom(element)) {
        		element = getElementUsingCssSelector(pageToRequest, identificator); // find using css selector (first used in Mahle)
            }
        }
        
        return element;
	}

	
	private boolean notFoundInDom(DomElement findCrosses) {
		return findCrosses == null;
	}
	
	
	private DomElement getElementByName(HtmlPage pageToRequest, String name) {
		DomElement elem = null;
		try {
			elem = (DomElement) pageToRequest.getElementByName(name); // might throw exception when didn't find by name
		}
		catch(Exception e){
			System.err.println("Not found by name");
		}
		
		return elem;
	}
	
	
	private DomElement getElementUsingCssSelector(HtmlPage pageToRequest, String selector) {
		DomElement elem = null;
		try {
			elem = (DomElement) pageToRequest.querySelectorAll(selector).get(0); // get might throw exception when didn't find by class
		}
		catch(Exception e){
			System.err.println("Not found by css selector");
		}
		
		return elem;
	}

	
	private void handleSedziszow(HtmlPage pageToRequest, DomElement filterNameInput) {
		pageToRequest.executeJavaScript(
  			  "var form = document.querySelector(\"" + SedziszowChecker.formToSubmit() + "\");"
  			+ "var select = document.querySelector(\"" + SedziszowChecker.selectList() + "\");"
  			+ "select.selectedIndex = " + SedziszowChecker.optionToSelectIndex() + ";"
  			+ "form.submit();");
  	
	  	filterNameInput = (HtmlTextArea) findElementOnPageUsingIdentificator(pageToRequest, this.inputFieldId);
	    ((HtmlTextArea)filterNameInput).setText(this.filterName);
	}
	
	
	private void notifyObserverAboutChange(String reconnectMessage) {
		setChanged();
		notifyObservers(reconnectMessage);
	}
}
