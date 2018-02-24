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
import utils.HtmlCheckedFilterViewBuilder;
import utils.HtmlCodeBuilder;
import utils.Utils;

public class CheckedFilterTab implements Tabbed {
	private JPanel panel = new JPanel();
	private final VBox vbox = new VBox();
	
	public CheckedFilterTab(Filter filter) {
		panel.setLayout(new BorderLayout());
		
		vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
		
		Platform.runLater(new Runnable() {
			private JFXPanel mainPanel = new JFXPanel();
			
            @Override
            public void run() {
            	HtmlCodeBuilder htmlCodeBuilder = new HtmlCheckedFilterViewBuilder();
            	htmlCodeBuilder.insertStylesheet("https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css");
            	htmlCodeBuilder.insertScript("https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js");
            	
            	htmlCodeBuilder.provideRequiredObject(filter);
            	
            	for(FilterChecker checker : Utils.getFiltersCheckers()){
            		htmlCodeBuilder.provideRequiredObject(checker);
            		htmlCodeBuilder.createTable();
            	}
            	
				String htmlCode = htmlCodeBuilder.getFullHtmlCode();
				setupHtmlView(htmlCode);
				
				panel.add(mainPanel);
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
		});
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
