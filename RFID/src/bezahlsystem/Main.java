package bezahlsystem;

import datenbank.DBHandler;
import mifareReader.handler.ReaderHandler;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BezahlGUI(new ReaderHandler(), new DBHandler());
	}

}
