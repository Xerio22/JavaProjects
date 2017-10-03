package views;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ConnectionInformationView extends JTextPane {
	private static final long serialVersionUID = -7854154878864973825L;
	private Style infoStyle;
	private StyledDocument infoDoc;
	
	public ConnectionInformationView() {
		this.setEditable(false);
		
		// Add style and create document to control font colors of infoTextPane
		infoStyle = this.addStyle("infoStyle", null);
		infoDoc = this.getStyledDocument();
		
		// Make text area keep scrolling down as more text is added
		DefaultCaret infoCaret = (DefaultCaret)this.getCaret();
		infoCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);		

		// Create margins
		this.setMargin(new Insets(5, 5, 5, 5));
	}
	
	public void printInfo(String info) {
		try {
			infoDoc.insertString(infoDoc.getLength(), info, infoStyle);
			infoDoc.insertString(infoDoc.getLength(), "\n", infoStyle);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} 
	}
	
	public void printInfo(String info, Color color) {
        StyleConstants.setForeground(infoStyle, color);
        this.printInfo(info);
        StyleConstants.setForeground(infoStyle, Color.BLACK);
	}
}
