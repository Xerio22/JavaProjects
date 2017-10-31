package utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlTxtFilter extends FileFilter {
	@Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(file);
        if (extension != null) {
            if (extension.equals(Utils.xml) || extension.equals(Utils.txt)) {
            	return true;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "XML and txt files";
    }
}
