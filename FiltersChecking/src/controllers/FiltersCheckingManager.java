package controllers;

import java.util.List;
import java.util.Observable;
import javax.swing.ListModel;
import javax.swing.SwingWorker;

import connectionhandlers.ConnectionObserver;
import filterscheckers.FilterChecker;
import filterscheckers.FilterCheckerObserver;
import filtersreaders.FiltersReader;
import filtersreaders.FiltersReaderFromListModel;
import models.Filter;
import models.FilterEquivalents;
import utils.Utils;
import views.ConnectionInformationView;

public class FiltersCheckingManager extends Observable implements Runnable{
	private ConnectionObserver connectionObserver;
	private FilterCheckerObserver filterCheckerObserver;
	private FiltersReader filtersReader;
	private String state;
	public static final String STATE_FINISHED_CHECKING = "checking_finished";
	public static final String STATE_FILTER_CHECKED = "filter_checked";
	List<Filter> filtersFromInput;Thread t;
	public FiltersCheckingManager(FiltersReader filtersReader, ConnectionInformationView infoView) {
		/* Create FilterReader */
		this.filtersReader = filtersReader;	
		
		/* Prepare Observer for connections */
		this.connectionObserver = new ConnectionObserver(infoView);
		
		/* Prepare Observer for checkers */
		this.filterCheckerObserver = new FilterCheckerObserver(infoView);
	}
	
	
	public void startProcessing() {
		this.filtersFromInput = filtersReader.getFiltersAsList();
		
		t = new Thread(this);
		t.start();
	}
	
	
	@Override
	public void run() {
		runFiltersChecking(filtersFromInput);
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
		for(FilterChecker checker : Utils.getFiltersCheckers()) {
			
			removePreviousObserversFromChecker(checker);
			putObserversToChecker(checker);
			
			// TODO this try catch is only for testing purposes
			try{	
				FilterEquivalents newEquivalents = checker.getEquivalentsFor(filter);
				if(checker.getState().equals(FilterChecker.STATE_BAD_OEM_TAG)){
					t.interrupt();
					t.stop();
				}
				filter.addEquivalents(newEquivalents);
			}
			catch(Exception e){
				e.printStackTrace();
			}
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

