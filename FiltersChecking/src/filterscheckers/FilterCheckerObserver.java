package filterscheckers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;

import connectionhandlers.ServerConnectionHandler;
import models.Filter;
import models.ObjectWithMessage;
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
		FilterChecker checker = (FilterChecker) observable;
		ObjectWithMessage<Filter> owm = (ObjectWithMessage<Filter>) arg;
		String message = owm.getMessage();
		Filter filter = owm.getObj();
		
		printSummary(checker, filter);
		
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

	private void printSummary(FilterChecker checker, Filter filter) {
		printLine("Wyszukiwarka: " + checker.getCheckerName());
		printLine("Numer filtra: " + filter.getOemNumber());
		print("Wynik wyszukiwania: ");
	}

	private void printLine(String msg) {
		infoView.printInfoLine(msg);
	}

	private void print(String msg) {
		infoView.printInfo(msg);
	}
	
	private void printEquivNotFound() {
		infoView.printInfoLine("Equivalent not found");
	}

	private void printEquivFound() {
		infoView.printInfoLine("Equivalent found");
	}

	private void printBlockedInfo() {
		infoView.printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
	}
}
