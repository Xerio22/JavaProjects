package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

public class SplitCreator {
	private Splittable[] splittables;
	private int numberOfResultAreas;
	
	public SplitCreator(Splittable[] splittables) {
		this.splittables = splittables;
		this.numberOfResultAreas = splittables.length;
	}

	public JSplitPane getFinalSplitPane() {
		JSplitPane finalPane = getPane();
	
		return finalPane;
	}
	
	private JSplitPane getPane(){
		
		JSplitPane sp = new JSplitPane();
		
		JScrollPane scp = new JScrollPane((ResultArea)splittables[0]);
		sp.setLeftComponent(scp);
		
		return sp;
	}
	
	private boolean isEven(int number) {
		return (number & 1) == 0;
	}
}
