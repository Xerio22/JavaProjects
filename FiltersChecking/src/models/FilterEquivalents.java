package models;

import java.util.ArrayList;
import java.util.List;

public class FilterEquivalents {
	private List<Filter> equivalents = new ArrayList<>();
	
	public void addEquivalent(Filter equivalent) {
		equivalents.add(equivalent);
	}
}
