package filterscheckers;

import models.Filter;
import utils.Utils;

public class HifiChecker extends FiltersChecker {

	public HifiChecker() {}

	@Override
	public boolean isFilterInDatabase(Filter filterToCheck) {
		String filterName = filterToCheck.getValueOfTag(Utils.filtr_wf);
		String serverUrlString = "https://hifi-filter.com/en/catalog/" + filterName + "-recherche-equivalence.html";
		
		return super.checkForFilterInFollowingServer(filterName, serverUrlString);
	}
	
	@Override
	public Filter getReplacementFor(Filter filter) {

		if(isFilterInDatabase(filter)){
			
		}
		
		return null;
	}

	

}
