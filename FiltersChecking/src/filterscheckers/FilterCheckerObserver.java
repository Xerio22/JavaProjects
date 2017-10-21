package filterscheckers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import views.ConnectionInformationView;

public class FilterCheckerObserver implements Observer {

	private ListModel<Filter> filtersListModel;
	private ConnectionInformationView infoView;

	public FilterCheckerObserver(ListModel<Filter> filtersListModel, ConnectionInformationView infoView) {
		this.filtersListModel = filtersListModel;
		this.infoView = infoView;
	}

	@Override
	public void update(Observable observable, Object arg) {
		String message = getMessage(arg);
		
		switch(message){
		case FilterChecker.BLOCKED_BY_SERVER_MESSAGE:
			printBlockedInfo();
			break;
		case FilterChecker.EQUIVALENT_FOUND_MESSAGE:
			printEquivFound();
			break;
		case FilterChecker.EQUIVALENT_NOT_FOUND_MESSAGE:
			printEquivNotFound();
			break;
		}
	}

	private void printEquivNotFound() {
		infoView.printInfo("Equivalent not found");
	}

	private void printEquivFound() {
		infoView.printInfo("Equivalent found");
	}

	private void printBlockedInfo() {
		infoView.printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
	}

	private String getMessage(Object arg) {
		return (String) arg;
	}

}
