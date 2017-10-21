package filterscheckers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import models.Filter;
import views.ConnectionInformationView;

public class FilterCheckerObserver implements Observer {

	private ConnectionInformationView infoView;

	public FilterCheckerObserver(ConnectionInformationView infoView) {
		this.infoView = infoView;
	}

	@Override
	public void update(Observable observable, Object arg) {
		FilterChecker checker = (FilterChecker) observable;
		String state = checker.getState();
		Filter filter = checker.getFilter();
		
		printSummary(checker, filter);
		
		switch(state){
		case FilterChecker.STATE_BLOCKED_BY_SERVER:
			printBlockedInfo();
			break;
		case FilterChecker.STATE_EQUIVALENT_FOUND:
			printEquivFound();
			break;
		case FilterChecker.STATE_EQUIVALENT_NOT_FOUND:
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
		infoView.printInfoLine("Equivalent found", Color.GREEN);
	}

	private void printBlockedInfo() {
		infoView.printInfo("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
	}
}
