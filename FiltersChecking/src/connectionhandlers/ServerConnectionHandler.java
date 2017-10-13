package connectionhandlers;

import java.util.Observable;

public abstract class ServerConnectionHandler extends Observable {

	public abstract void insertFilterNameIntoUrlString(String searchedFilterOEMnumber);

	public abstract String getServerResponse() ;

	public abstract boolean checkIsAnyReplacementPresent(String serverResponse) ;
}
