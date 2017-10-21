package views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabTitle extends JPanel{
	private static final long serialVersionUID = 8100956171144306740L;
	private JButton closeButton;
	
	public TabTitle(String title, JTabbedPane tabbedPane) {
		JLabel titleLabel = new JLabel(title);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		closeButton = new JButton("x");
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBorderPainted(false); 
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusPainted(false); 
		closeButton.setOpaque(false);
		closeButton.getModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel)(e.getSource());
				 if (model.isRollover()) {
					 closeButton.setForeground(Color.RED);
			        } else {
			        	closeButton.setForeground(Color.BLACK);
			        }
			}
			
		});
		
		closeButton.addActionListener(buttonClicked -> {
			int selected = tabbedPane.getSelectedIndex();
	        if (selected != -1) {
	            tabbedPane.removeTabAt(selected);
//	            ((JButton)buttonClicked.getSource()).removeActionListener(null);
	        }
		});
		
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);
		this.add(titleLabel,BorderLayout.CENTER);
		this.add(closeButton,BorderLayout.EAST);
	}
}
