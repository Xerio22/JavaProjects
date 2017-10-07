package viewscomponents;

import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

public class ResultArea extends JTextPane implements Splittable {
	private Style style;
	private StyledDocument styledDocument;
	
	public ResultArea() {
		// Forbid editing
		this.setEditable(false);
		
		// Add style and create document to control font colors
		style = this.addStyle("style", null);
		styledDocument = this.getStyledDocument();
	}
}
