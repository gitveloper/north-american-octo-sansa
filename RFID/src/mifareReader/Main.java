package mifareReader;

import mifareReader.handler.ReaderHandler;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		if(System.getProperty("os.arch").contains("64")) {
			System.setProperty( "java.library.path", "RFID/_rxtx" );
			System.out.println("64 bit " + System.getProperty("os.arch"));
		} else {
			
		}
		
		
//		new ReaderGUI(new ReaderHandler());
	}

}
