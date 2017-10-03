package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

public class FileLoader {
	private String defaultPath;
	
	public FileLoader(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	public File loadFile(){
		JFileChooser fileChooser = new JFileChooser();
		
		File selectedFile = null;
		
		Path inputPath = Paths.get("", defaultPath);

		if(Files.exists(inputPath)){
			selectedFile = new File(inputPath.toString());
		}
		else {
			int returnValue = fileChooser.showOpenDialog(null);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
			}
		}
		
		return selectedFile;
	}
}
