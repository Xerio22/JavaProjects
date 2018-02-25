package observers;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import filterscheckers.FilterChecker;
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
		case FilterChecker.STATE_SERVER_NOT_RESPONDING:
			printServerNotResponding(filter.getOemNumber());
			break;
		case FilterChecker.STATE_BLOCKED_BY_SERVER:
			printBlockedInfo();
			break;
		case FilterChecker.STATE_EQUIVALENT_FOUND:
			printEquivFound();
			break;
		case FilterChecker.STATE_EQUIVALENT_NOT_FOUND:
			printEquivNotFound();
			break;
		case FilterChecker.STATE_BAD_OEM_TAG:
			printBadOemTag();
			break;
		}
	}

	private void printSummary(FilterChecker checker, Filter filter) {
		infoView.printInfoLine("Wyszukiwarka: " + checker.getCheckerName());
		infoView.printInfoLine("Numer filtra: " + filter.getOemNumber());
		infoView.printInfo("Wynik wyszukiwania: ");
	}
	
	private void printServerNotResponding(String oemNumber) {
		infoView.printInfoLine("Serwer nie odpowiada. Nie mozna pobrac danych dla filtra " + oemNumber, Color.RED);
	}
	
	private void printBlockedInfo() {
		infoView.printInfoLine("Serwer zablokowal polaczenie. Nie mozna pobrac danych.", Color.RED);
	}
	
	private void printEquivFound() {
		infoView.printInfoLine("Equivalent found", Color.GREEN);
	}

	private void printEquivNotFound() {
		infoView.printInfoLine("Equivalent not found", Color.RED);
	}
	
	private void printBadOemTag() {
		infoView.printInfoLine("Podano nieprawidlowy znacznik xml dla numerow oem", Color.RED);
	}
}
