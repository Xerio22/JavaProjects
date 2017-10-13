package controllers;

import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.SwingWorker;

import filterscheckers.FilterChecker;
import filtersreaders.FiltersReader;
import filtersreaders.FiltersReaderFromListModel;
import models.Filter;
import models.FilterEquivalents;
import utils.Utils;
import views.ConnectionInformationView;

public class FiltersCheckingManager {
	private ListModel<Filter> filtersListModel;
	private MyObserver obs;
	
	public FiltersCheckingManager(ListModel<Filter> filtersListModel, JTabbedPane tabsPanel, ConnectionInformationView infoView) {
		this.filtersListModel = filtersListModel;
		this.obs = new MyObserver(filtersListModel, tabsPanel, infoView);
	}
	
	
	public void startProcessing() {
		List<Filter> filtersFromInput = getFiltersFromInput();
		
		runBackgroundChecking(filtersFromInput);
	}

	
	private List<Filter> getFiltersFromInput() {
		FiltersReader filtersReader = new FiltersReaderFromListModel(filtersListModel);	
		
		return filtersReader.getFiltersAsList();
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
		}
	}


	private void findFilterEquivalentsFromEveryServer(Filter filter) {
		List<FilterChecker> c = Utils.getFiltersCheckers(obs);
		for(FilterChecker checker : c) {
			System.out.println(c);
			try{
				FilterEquivalents newEquivalents = checker.getEquivalentsFor(filter);
				filter.addEquivalentsIfTheyExist(newEquivalents);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}		
	}
}

