package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controllers.FilterDataChecker;
import models.Filter;
import models.FiltersListModel;

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
		everythingPanel.add(startingScreen);
		
		
		/* Create and show frame */
		createAndSetupFrame(everythingPanel);
		frame.setVisible(true);
	}

	private JFrame createAndSetupFrame(JPanel mainPanel) {
		frame = new JFrame("FiltersChecking");
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		return frame;
	}
	

}


