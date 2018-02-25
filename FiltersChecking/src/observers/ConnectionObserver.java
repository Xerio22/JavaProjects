package observers;

import java.awt.Color;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

import connectionhandlers.ServerConnectionHandler;
import views.ConnectionInformationView;

public class ConnectionObserver implements Observer {

	private ConnectionInformationView infoView;
	private int triesCount = 1;

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
		triesCount = 0;
		infoView.printInfoLine("Connected");
	}

	private void printConnectingInfo() {
		infoView.printInfoLine("Connecting");
	}

	private void printReconnectInfo() {
		infoView.printInfoLine("(" + LocalTime.now() + ") Przekroczono limit czasu połączenia z serwerem!", Color.ORANGE);
		infoView.printInfo("Próba ponownego nawiązania połączenia nastąpi za 10 sekund..." + 
						   "(" + triesCount++ + "/" + ServerConnectionHandler.RECONNECT_TRIES + ")\n\n", Color.ORANGE);
	}

	private String getMessage(Object arg) {
		return (String) arg;
	}

}
