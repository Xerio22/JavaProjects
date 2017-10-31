package filtersreaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Filter;

public class FiltersReaderFromTxt implements FiltersReader {
	private File file;

	public FiltersReaderFromTxt(File file) {
		this.file = file;
	}

	@Override
	public List<Filter> getFiltersAsList() {
		List<Filter> list = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String oemNumber = null;

			while ((oemNumber = br.readLine()) != null) {

				Filter newFilter = Filter.createFilterUsingOEMnumber(oemNumber);
				list.add(newFilter);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

}
