package utils;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class XmlStructureProvider {
//	private JTextField rootTagName = new JTextField();
	private JTextField elementTagName = new JTextField();
	private JTextField oemNumberTagName = new JTextField();
	private final JComponent[] inputs = new JComponent[] {
//	        new JLabel("Root's tag name"),
//	        rootTagName,
	        new JLabel("Element's tag name"),
	        elementTagName,
	        new JLabel("Oem number's tag name"),
	        oemNumberTagName
	};

	public void getXmlStructureUsingDialog() {
		int result = JOptionPane.showConfirmDialog(null, inputs, "Podaj strukture pliku XML", JOptionPane.PLAIN_MESSAGE);
		    	
    	if (result != JOptionPane.OK_OPTION || isAnyFieldEmpty()) {
    		JOptionPane.showMessageDialog(null, "Wprowadzono bledne dane", "Blad!", JOptionPane.WARNING_MESSAGE);
    	}
	}
	
    private boolean isAnyFieldEmpty() {
		return /*isEmpty(getRootTagName()) ||*/ isEmpty(getElementTagName()) || isEmpty(getOemNumberTagName());
	}


	private boolean isEmpty(String tagName) {
		return tagName.equals("") || tagName == null || tagName.trim().equals("");
	}


//	public String getRootTagName() {
//		return rootTagName.getText();
//	}

	public String getElementTagName() {
		return elementTagName.getText();
	}

	public String getOemNumberTagName() {
		return oemNumberTagName.getText();
	}
}