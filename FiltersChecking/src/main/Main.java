package main;

import javax.swing.SwingUtilities;

import controllers.FilterDataChecker;
import views.MainView;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainView view = new MainView();
			}
		});
	}
}
