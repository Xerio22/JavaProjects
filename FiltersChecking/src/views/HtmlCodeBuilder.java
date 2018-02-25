package views;

public abstract class HtmlCodeBuilder {
	private static final StringBuilder HTML_TOP_PART = new StringBuilder( 
			"<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<title>Wyniki wyszukiwania</title>"
			+ "</head>"
			+ "<body>");
	private StringBuilder HTML_BODY = new StringBuilder();
	private static final StringBuilder HTML_BOTTOM_PART = new StringBuilder("</body></html>");
	
	private static int titleEndIdx = HTML_TOP_PART.indexOf("</title>");
	private static int startAppendingIdx = titleEndIdx + 8; // tag length
	
	
	public void insertStylesheet(String stylesheetHref){
		HTML_TOP_PART.insert(startAppendingIdx, createStylesheetTagFrom(stylesheetHref));
	}
	
	private Object createStylesheetTagFrom(String stylesheetHref) {
		return "<link rel=\"stylesheet\" href=\"" + stylesheetHref + "\">";
	}


	public void insertScript(String scriptHref){
		HTML_TOP_PART.insert(startAppendingIdx, createScriptTagFrom(scriptHref));
	}
	
	private Object createScriptTagFrom(String scriptHref) {
		return "<script src=\"" + scriptHref + "\"></script>";
	}


	public StringBuilder insertHtmlCodeToBody(String html){
		return HTML_BODY.append(html);
	}
	
	
	public String getFullHtmlCode(){
		StringBuilder generatedHtmlCode = new StringBuilder();
		
		generatedHtmlCode.append(HTML_TOP_PART)
						 .append(HTML_BODY)
						 .append(HTML_BOTTOM_PART);
		
		return generatedHtmlCode.toString();
	}


	public abstract void provideRequiredObject(Object obj);
	public abstract void createTable();
}
