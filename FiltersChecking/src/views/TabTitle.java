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

public class TabTitle extends JPanel {
	private static final long serialVersionUID = 8100956171144306740L;
	private JButton closeButton;
	private String title;
	private JTabbedPane tabbedPane;

	public TabTitle(String title, JTabbedPane tabbedPane) {
		this.title = title;
		this.tabbedPane = tabbedPane;
		
		setupCloseTabButton();
		setupTabTitle();
	}

	
	private void setupCloseTabButton() {
		closeButton = new JButton("x");
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusPainted(false);
		closeButton.setOpaque(false);
		
		addClickActionListener();
		addRolloverActionListener();
	}
	
	
	private void addClickActionListener() {
		closeButton.addActionListener(buttonClicked -> {
			int index = tabbedPane.indexOfTab(title);

			if (index != -1) {
				tabbedPane.removeTabAt(index);
				// ((JButton)buttonClicked.getSource()).removeActionListener(null);
			}
		});
	}

	
	private void addRolloverActionListener() {
		closeButton.getModel().addChangeListener(e -> {
			ButtonModel model = (ButtonModel) (e.getSource());
			setRolloverActionForButton(model);
		});
	}
	
	
	private void setRolloverActionForButton(ButtonModel model) {
		if (model.isRollover()) {
			closeButton.setForeground(Color.RED);
		} else {
			closeButton.setForeground(Color.BLACK);
		}
	}
	
	
	private void setupTabTitle() {
		JLabel titleLabel = new JLabel(title);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // distance between title label and close button
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);
		this.add(titleLabel, BorderLayout.CENTER);
		this.add(closeButton, BorderLayout.EAST);
	}
}
