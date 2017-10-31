package connectionhandlers;

import java.awt.Color;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

import views.ConnectionInformationView;

public class ConnectionObserver implements Observer {

	private ConnectionInformationView infoView;

	public ConnectionObserver(ConnectionInformationView infoView) {
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
		infoView.printInfoLine("(" + LocalTime.now() + ") Przekroczono limit czasu polaczenia z serwerem!", Color.ORANGE);
		infoView.printInfo("Proba ponownego nawiazania polaczenia nastapi za 10 sekund...", Color.ORANGE);
	}

	private String getMessage(Object arg) {
		return (String) arg;
	}

}
