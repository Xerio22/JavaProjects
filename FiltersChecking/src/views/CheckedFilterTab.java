package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import filterscheckers.FilterChecker;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import models.Filter;
import models.FilterProperty;
import utils.Utils;

public class CheckedFilterTab extends JPanel {
	private static final long serialVersionUID = 6214439754839595927L;
	
	private JFXPanel mainPanel = new JFXPanel();
	private final VBox vbox = new VBox();
	private Filter filter;
	
	public CheckedFilterTab(Filter filter) {
		this.setLayout(new BorderLayout());
		this.filter = filter;
		
		vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
		
		Platform.runLater(new Runnable() { 
            @Override
            public void run() {
            	StringBuilder htmlCodeBuilder = new StringBuilder();
            	
            	htmlCodeBuilder.append(Utils.HTML_TOP_PART);
            	System.out.println(filter.getProperties());
            	for(FilterChecker checker : Utils.getFiltersCheckers()){
            		int columnsCount = 0;
            		
            		htmlCodeBuilder.append("<h4>" + checker.getCheckerName() + "</h1>");
            		htmlCodeBuilder.append("<table class=\"striped highlight\">");
            			htmlCodeBuilder.append("<thead>");
		            		htmlCodeBuilder.append("<tr>");
			            		for(FilterProperty fp : filter.getProperties()){
				            		if(isPropertyConcernsThisChecker(fp, checker)){
				            			if(fp.getPropertyName().contains("1")){
				            				htmlCodeBuilder.append("<td>");
				            					htmlCodeBuilder.append(fp.getPropertyName());
						            		htmlCodeBuilder.append("</td>");
						            		
											columnsCount++;
				            			}
				            		}
				            	}
		            		htmlCodeBuilder.append("</tr>");
	            		htmlCodeBuilder.append("</thead>");
	            		htmlCodeBuilder.append("<tbody>");
	            			int currentColumn = 0;
	            		
		            		for(FilterProperty fp : filter.getProperties()){
			            		if(isPropertyConcernsThisChecker(fp, checker)){
			            			if(currentColumn == 0){
										htmlCodeBuilder.append("<tr>");
			            			}
			            			
			            			htmlCodeBuilder.append("<td>");
	            					htmlCodeBuilder.append(fp.getPropertyValue());
	            					htmlCodeBuilder.append("</td>");
			            			
	            					currentColumn++;
	            					
			            			if(currentColumn  == columnsCount){
										htmlCodeBuilder.append("</tr>");
										currentColumn = 0;
			            			}
			            		}
			            	}
	            		htmlCodeBuilder.append("</tbody>");
	            	htmlCodeBuilder.append("</table><br><br>");
            	}
            	
            	htmlCodeBuilder.append(Utils.HTML_BOTTOM_PART);
            	
				String htmlCode = htmlCodeBuilder.toString();
				setupHtmlView(htmlCode);
            }
            
            private void setupHtmlView(String generatedHtmlCode){   
                final WebView webView = new WebView();
                final WebEngine webEngine = webView.getEngine();
                webEngine.loadContent(generatedHtmlCode);
                
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(webView);

                StackPane root = new StackPane();
            	root.getChildren().add(webView);
            	
            	Scene mainScene = new Scene(root);
        		mainPanel.setScene(mainScene);
            }


			private boolean isPropertyConcernsThisChecker(FilterProperty filterProperty, FilterChecker checker) {
				return filterProperty.getPropertyName().contains(checker.getCheckerName());
			}
		});

		this.add(mainPanel);
	}
}
