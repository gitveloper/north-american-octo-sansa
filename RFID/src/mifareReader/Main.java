package mifareReader;

import mifareReader.handler.ReaderHandler;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 *  CHECK OS.ARCH
		 */
		
		new ReaderGUI(new ReaderHandler());

		
	}

}
