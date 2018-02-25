package views;

import filterscheckers.FilterChecker;
import models.Filter;
import models.FilterProperty;

public class HtmlCheckedFilterViewBuilder extends HtmlCodeBuilder {

	private Filter filter;
	private FilterChecker checker;


	@Override
	public void createTable() {
		int columnsCount = 0;
		
		insertHtmlCodeToBody("<h4 style=\"text-align: center;\">" + checker.getCheckerName() + "</h4>");
		insertHtmlCodeToBody("<table class=\"striped highlight centered\" style=\"table-layout: fixed;\">");
			insertHtmlCodeToBody("<thead>");
        		insertHtmlCodeToBody("<tr>");
        			insertHtmlCodeToBody("<th>");
        				insertHtmlCodeToBody("Numer filtra"); 
        				columnsCount++;
        			insertHtmlCodeToBody("</th>");
        			
        			insertHtmlCodeToBody("<th>");
            			insertHtmlCodeToBody("OEM"); 
            			columnsCount++;
            		insertHtmlCodeToBody("</th>");
            			
            		insertHtmlCodeToBody("<th>");
            			insertHtmlCodeToBody("Zamiennik"); 
            			columnsCount++;
            		insertHtmlCodeToBody("</th>");
            			
        			// uncomment when using "additional properties" for filter equivalents"
//            		for(FilterProperty fp : filter.getProperties()){
//	            		if(isPropertyConcernsThisChecker(fp, checker)){
//	            			if(fp.getPropertyName().contains("1")){
//	            				append("<th>");
//	            					append(fp.getPropertyName().replaceAll(checker.getCheckerName() + "_equiv_\\d+_", ""));
//			            		append("</th>");
//			            		
//								columnsCount++;
//	            			}
//	            		}
//	            	}
        		insertHtmlCodeToBody("</tr>");
    		insertHtmlCodeToBody("</thead>");
    		insertHtmlCodeToBody("<tbody>");
    			int currentColumn = 0;
    		
        		for(FilterProperty fp : filter.getProperties()){
            		if(isPropertyConcernsThisChecker(fp, checker)){
            			if(currentColumn == 0){
							insertHtmlCodeToBody("<tr>");
            			}
            			
            			insertHtmlCodeToBody("<td>");
    					insertHtmlCodeToBody(fp.getPropertyValue());
    					insertHtmlCodeToBody("</td>");
            			
    					currentColumn++;
    					
            			if(currentColumn  == columnsCount){
							insertHtmlCodeToBody("</tr>");
							currentColumn = 0;
            			}
            		}
            	}
    		insertHtmlCodeToBody("</tbody>");
    	insertHtmlCodeToBody("</table><br><br><br><br>");
	}

	
	private boolean isPropertyConcernsThisChecker(FilterProperty filterProperty, FilterChecker checker) {
		return filterProperty.getPropertyName().contains(checker.getCheckerName());
	}


	@Override
	public void provideRequiredObject(Object obj) {
		if(obj instanceof Filter){
			this.filter = (Filter) obj;
		}
		else if(obj instanceof FilterChecker){
			this.checker = (FilterChecker) obj;
		}
	}
}
