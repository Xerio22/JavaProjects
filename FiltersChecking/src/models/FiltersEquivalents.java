package models;

import java.util.HashMap;
import java.util.Map;

public class FiltersReplacements {
	private Map<Filter, Filter> replacements = new HashMap<>();
	
	public void addReplacementFor(Filter base, Filter replacement) {
		replacements.put(base, replacement);
	}
}
