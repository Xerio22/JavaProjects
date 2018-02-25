package controllers;

import java.util.List;
import java.util.Observable;
import javax.swing.SwingWorker;

import filterscheckers.FilterChecker;
import filtersreaders.FiltersReader;
import models.Filter;
import models.FilterEquivalents;
import observers.ConnectionObserver;
import observers.FilterCheckerObserver;
import views.ConnectionInformationView;

public class FiltersCheckingManager extends Observable {
	private ConnectionObserver connectionObserver;
	private FilterCheckerObserver filterCheckerObserver;
	private FiltersReader filtersReader;
	private String state;
	public static final String STATE_FINISHED_CHECKING = "checking_finished";
	public static final String STATE_FILTER_CHECKED = "filter_checked";
	private List<FilterChecker> selectedCheckers;
	
	public FiltersCheckingManager(FiltersReader filtersReader, ConnectionInformationView infoView) {
		this.filtersReader = filtersReader;	
		
		/* Prepare Observer for connections */
		this.connectionObserver = new ConnectionObserver(infoView);
		
		/* Prepare Observer for checkers */
		this.filterCheckerObserver = new FilterCheckerObserver(infoView);
	}
	
	
	public void startProcessing(List<FilterChecker> selectedCheckers) {
		this.selectedCheckers = selectedCheckers;
		List<Filter> filtersFromInput = filtersReader.getFiltersAsList();
		
		runBackgroundChecking(filtersFromInput);
	}
	
	
	private void runBackgroundChecking(List<Filter> filtersFromInput) {
		SwingWorker<Void, Void> myWorker = new SwingWorker<Void, Void>() {
		    @Override
		    protected Void doInBackground() {
				runFiltersChecking(filtersFromInput);
				return null;
		    }
		};
		
		myWorker.execute();
	}
	

	private void runFiltersChecking(List<Filter> filtersFromInput) {
		for(Filter filter : filtersFromInput) {
			// TODO every operation in new thread
			findFilterEquivalentsFromEveryServer(filter);
			
			notifyFilterChecked(filter);
		}
		
		notifyFinishChecking();
	}


	private void findFilterEquivalentsFromEveryServer(Filter filter) {
		for(FilterChecker checker : selectedCheckers) {
			removePreviousObserversFromChecker(checker);
			putObserversToChecker(checker);
			
			FilterEquivalents equivalents = checker.getEquivalentsFor(filter);
			filter.addEquivalents(equivalents);
		}		
	}


	private void notifyFilterChecked(Filter filter) {
		setState(STATE_FILTER_CHECKED);
		setChanged();
		notifyObservers(filter);
	}

	
	private void setState(String newState) {
		this.state = newState;
	}


	private void notifyFinishChecking() {
		setState(STATE_FINISHED_CHECKING);
		setChanged();
		notifyObservers();
	}


	private void removePreviousObserversFromChecker(FilterChecker checker) {
		checker.removeAttachedObservers();
	}


	private void putObserversToChecker(FilterChecker checker) {
		checker.putObserverForFilterChecker(filterCheckerObserver);
		checker.putObserverForConnection(connectionObserver);
	}


	public String getState() {
		return state;
	}
}

