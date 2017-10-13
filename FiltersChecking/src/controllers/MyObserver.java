package controllers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;

import filterscheckers.ServerConnectionHandler;
import models.Filter;
import views.ConnectionInformationView;

public class MyObserver implements Observer {

	private ListModel<Filter> filtersListModel;
	private JTabbedPane tabsPanel;
	private ConnectionInformationView infoView;

	public MyObserver(ListModel<Filter> filtersListModel, JTabbedPane tabsPanel, ConnectionInformationView infoView) {
		this.filtersListModel = filtersListModel;
		this.tabsPanel = tabsPanel;
		this.infoView = infoView;
	}

	@Override
	public void update(Observable o, Object arg) {
		String message = getMessage(arg);
		
		switch(message){
		case "Connecting":
			printConnectingInfo();
			break;
		case "Connected":
			printConnectedInfo();
			break;
		case "Reconnect":
			printReconnectInfo();
			break;
		case "Blocked":
			printBlockedInfo();
			break;
		case "URL_error":
			printURLerror();
			break;
		case "Equiv_found":
			printEquivFound();
			break;
		case "Equiv_not_found":
			printEquivNotFound();
			break;
		}
	}

	private void printURLerror() {
		infoView.printInfo("Something went wrong with URL");
	}

	private void printEquivNotFound() {
		infoView.printInfo("Equivalent not found");
	}

	private void printEquivFound() {
		infoView.printInfo("Equivalent found");
	}

	private void printConnectedInfo() {
		infoView.printInfo("Connected");
		tabsPanel.addTab("new", new JPanel());
	}

	private void printBlockedInfo() {
		infoView.printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
	}

	private void printConnectingInfo() {
		infoView.printInfo("Connecting");
	}

	private void printReconnectInfo() {
		infoView.printInfo("Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
		infoView.printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
	}

	private String getMessage(Object arg) {
		return (String) arg;
	}

}
