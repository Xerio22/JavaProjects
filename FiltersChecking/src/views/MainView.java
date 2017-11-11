package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class MainView {
	private JFrame frame;
	private JPanel everythingPanel = new JPanel(new BorderLayout());
	private StartScreen startingScreen;
	
	
	public MainView(){
		createAndShowGUI();
	}
	
	
	private void createAndShowGUI() {
		/* Create tab container for presenting data using tabs */
		JTabbedPane tabsPanel = new JTabbedPane();
		
		/* Create panel with starting screen and it's components */
		startingScreen = new StartScreen(tabsPanel);
		
		/* Add first tab with starting panel */
		tabsPanel.addTab(" ", null);
		tabsPanel.setEnabledAt(0, false);
		tabsPanel.setTabComponentAt(0, new CloseAllButtonTab(tabsPanel));
		tabsPanel.addTab("Start", startingScreen);
		tabsPanel.setSelectedIndex(1);
		
		/* Put everything into "the mainest" panel */
		everythingPanel.add(tabsPanel);
		
		/* Add quick shortcut to run filters checking */
		addKeyStrokeToWindowForQuickStart();
		
		/* Create and show frame */
		createAndSetupFrame();
		frame.setVisible(true);
	}
	

	private void addKeyStrokeToWindowForQuickStart() {
		Action startAction = new AbstractAction(){
			private static final long serialVersionUID = -7167492528994048612L;

			@Override
			public void actionPerformed(ActionEvent e) {
				startingScreen.getStartProcessingButton().doClick();
			}
		};
		
		everythingPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 
						KeyEvent.CTRL_DOWN_MASK
				), "cmd");
		everythingPanel.getActionMap().put("cmd", startAction);
	}

	
	private JFrame createAndSetupFrame() {
		frame = new JFrame("FiltersChecking");
		frame.setMinimumSize(new Dimension(1100, 600));
		frame.setPreferredSize(new Dimension(1100, 600));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(everythingPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		return frame;
	}
}


