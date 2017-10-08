package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainView {
	private JFrame frame;
	
	
	public MainView(){
		createAndShowGUI();
	}
	
	private void createAndShowGUI() {
		JPanel everythingPanel = new JPanel(new BorderLayout());
		
		/* Create tab container for presenting data using tabs */
		JTabbedPane tabsPanel = new JTabbedPane();
		
		
		/* Create panel with starting screen and it's components */
		JPanel startingScreen = new StartScreen();
		
		
		/* Add first tab with starting panel */ 
		tabsPanel.addTab("Start", startingScreen);
		

		/* Put everything into "the mainest" panel */
		everythingPanel.add(tabsPanel);
		
		
		/* Create and show frame */
		createAndSetupFrame(everythingPanel);
		frame.setVisible(true);
	}

	private JFrame createAndSetupFrame(JPanel mainPanel) {
		frame = new JFrame("FiltersChecking");
		frame.setMinimumSize(new Dimension(1000, 600));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		return frame;
	}
	

}


