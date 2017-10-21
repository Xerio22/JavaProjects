package views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CloseAllButtonTab extends JPanel {
	private static final long serialVersionUID = -5284682455386043379L;

	public CloseAllButtonTab(JTabbedPane tabbedPane) {
		JButton closeButton = new JButton("x");
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusPainted(false);
		closeButton.setOpaque(false);
		closeButton.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) (e.getSource());
				if (model.isRollover()) {
					closeButton.setForeground(Color.RED);
				} else {
					closeButton.setForeground(Color.BLACK);
				}
			}

		});

		closeButton.addActionListener(buttonClicked -> {
			int tabsCount = tabbedPane.getTabCount();
			for (int i = 2; i < tabsCount; i++) {
				tabbedPane.removeTabAt(2);
			}
		});

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);
		this.add(closeButton, BorderLayout.CENTER);
	}
}
