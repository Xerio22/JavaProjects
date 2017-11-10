package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import filterscheckers.*;

public class Utils {

	public static String newLine = System.getProperty("line.separator");
	
    public final static String xml = "xml";
    public final static String txt = "txt";
    
    // USERS HOME
	private final static String userHomeFolder = System.getProperty("user.home");
	
	// APP FOLDER
	public final static String configFolderName = "FiltersFiltering";
	public final static String configFolderPath = userHomeFolder + "\\" + configFolderName;
	
	// CONFIG FILE
	public final static String configFileName = "config.txt";
    public final static String configFilePath = configFolderPath + "\\" + configFileName;
    
    // RESULTS FILES
    public final static String xmlFileName = "results.xml";
    public final static String txtFileName = "results.txt";
    public final static String xmlFilePath = configFolderPath + "/" + xmlFileName;
    public final static String txtFilePath = configFolderPath + "/" + txtFileName;
    
    // PROPERTIES NAMES
    public final static String filtr_wf = "filtr_wf";
    public final static String obcy_skrot = "obcy_skrot";

	public static final String default_input_path = "zamienniki_wf.xml";

	public static int reconnect_time = 10000;
	public static int numberOfCheckedFilters = 0;
	public static int numberOfIterationsBefereSaving = 50;

	public static final String DEFAULT_OEM_NAME = "Default";
	public static final String OEM_NUMBER_TAG_NAME = "OEM_Number";
	
	public static final FilterChecker[] checkers = {
			// url
			new HifiChecker()
//			new DonaldsonChecker(),
//			new WixChecker(),
			// js
//			new BaldwinChecker(),
//			new MannChecker()
//			new CumminsChecker()
		};
    
	public static final List<FilterChecker> getFiltersCheckers(){ 
		return new ArrayList<>(Arrays.asList(checkers));
	}

    /*
     * Get the extension of a file.
     */  
    public static String getExtension(File file) {
        String extension = null;
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 &&  dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex+1).toLowerCase();
        }
        return extension;
    }
}