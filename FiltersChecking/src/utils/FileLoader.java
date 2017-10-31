package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileLoader {
	private String defaultPath;
	private FileFilter fileFilter;
	
	public FileLoader(FileFilter fileFilter) {
		this(fileFilter, "-");
	}
	
	public FileLoader(FileFilter fileFilter, String defaultPath) {
		this.fileFilter = fileFilter;
		this.defaultPath = defaultPath;
	}

	public File loadFile(){
		File selectedFile = null;
		
		Path inputPath = Paths.get("", defaultPath);

		if(!defaultPath.equals("-") && Files.exists(inputPath)){
			selectedFile = new File(inputPath.toString());
		}
		else {
			JFileChooser fileChooser = new JFileChooser();
			// Configure file chooser
			fileChooser.setFileFilter(fileFilter);
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			int returnValue = fileChooser.showOpenDialog(null);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
			}
		}
		
		return selectedFile;
	}
}
