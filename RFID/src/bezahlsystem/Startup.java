package bezahlsystem;

import javax.swing.SwingUtilities;

public class Startup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		//System.setProperty("proxyPort", "80");
        		//System.setProperty("proxyHost", "proxy.hft-stuttgart.de");
            	Thread splashThread = new Thread(new Splash());
            	splashThread.start();
            }
        });
	}

}
