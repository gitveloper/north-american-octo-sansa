/**
 * Main.java
 * --------------------------
 * Startet das Programm
 */
package mifareReader;

import mifareReader.handler.ReaderHandler;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new ReaderGUI(new ReaderHandler());

		
	}

}
