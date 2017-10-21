package connectionhandlers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;

import filterscheckers.FilterChecker;
import models.Filter;
import views.ConnectionInformationView;

public class ConnectionObserver implements Observer {

	private ListModel<Filter> filtersListModel;
	private ConnectionInformationView infoView;

	public ConnectionObserver(ListModel<Filter> filtersListModel, ConnectionInformationView infoView) {
		this.filtersListModel = filtersListModel;
		this.infoView = infoView;
	}

	@Override
	public void update(Observable observable, Object arg) {
		String message = getMessage(arg);
		
		switch(message){
		case ServerConnectionHandler.CONNECTING_MESSAGE:
			printConnectingInfo();
			break;
		case ServerConnectionHandler.CONNECTED_MESSAGE:
			printConnectedInfo();
			break;
		case ServerConnectionHandler.RECONNECT_MESSAGE:
			printReconnectInfo();
			break;
		case ServerConnectionHandler.URL_ERROR:
			printURLerror();
			break;
		}
	}

	private void printURLerror() {
		infoView.printInfoLine("Something went wrong with URL");
	}

	private void printConnectedInfo() {
		infoView.printInfoLine("Connected");
	}

	private void printConnectingInfo() {
		infoView.printInfoLine("Connecting");
	}

	private void printReconnectInfo() {
		infoView.printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
		infoView.printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
	}

	private String getMessage(Object arg) {
		return (String) arg;
	}

}
