package controllers;

import java.util.List;
import java.util.Observable;
import javax.swing.ListModel;
import javax.swing.SwingWorker;
import filterscheckers.FilterChecker;
import filtersreaders.FiltersReader;
import filtersreaders.FiltersReaderFromListModel;
import models.Filter;
import models.FilterEquivalents;
import utils.Utils;
import views.ConnectionInformationView;

public class FiltersCheckingManager extends Observable {
	private ConnectionObserver connectionObserver;
	private FiltersReader filtersReader;
	
	public FiltersCheckingManager(ListModel<Filter> filtersListModel, ConnectionInformationView infoView) {
		/* Create FilterReader */
		filtersReader = new FiltersReaderFromListModel(filtersListModel);	
		
		/* Prepare Observer for connections */
		this.connectionObserver = new ConnectionObserver(filtersListModel, infoView);
	}
	
	
	public void startProcessing() {
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
	}


	private void notifyFilterChecked(Filter filter) {
		setChanged();
		notifyObservers(filter);
	}

	
	private void findFilterEquivalentsFromEveryServer(Filter filter) {
		for(FilterChecker checker : Utils.getFiltersCheckers()) {
			
			checker.putObserver(connectionObserver);
			
			// TODO this try catch is only for testing purposes
			try{
				FilterEquivalents newEquivalents = checker.getEquivalentsFor(filter);
				filter.addEquivalents(newEquivalents);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
}

