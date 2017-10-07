package main;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ResultsPanel extends JPanel {
	private ResultArea[] resultAreas;
	private int numberOfResultAreas;
	
	public ResultsPanel(int numberOfResultAreas){
		this.setLayout(new BorderLayout());
		this.numberOfResultAreas = numberOfResultAreas;
		
		createResultAreas();
		
		SplitCreator splitCreator = new SplitCreator(resultAreas);
		JSplitPane finalSplitPane = splitCreator.getFinalSplitPane();
	}

	private void createResultAreas() {
		resultAreas = new ResultArea[numberOfResultAreas];
		for(int i = 0; i < numberOfResultAreas; i++) {
			resultAreas[i] = new ResultArea();
		}
	}

	
}
