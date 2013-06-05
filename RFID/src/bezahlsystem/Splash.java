/**
 * Splash.java
 * --------------------------
 * Einfache Klasse die ein Bild auf einem JFrame anzeigt.
 */
package bezahlsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mifareReader.handler.ReaderHandler;
import datenbank.DBHandler;

public class Splash extends JPanel implements Runnable {

	private static final long serialVersionUID = 4386862497293410112L;
	private Image sImage = new ImageIcon(getClass().getResource("../resources/splash.png")).getImage();	
	private JFrame frame;

	public void run() {
		
		frame = new JFrame();		
		frame.getContentPane().add(this);		
		frame.setSize(550, 550);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.toFront();

		System.out.println("Starte Splash!");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		frame.dispose();		
		new BezahlGUI(new ReaderHandler(), new DBHandler());
	}

	public void paint(Graphics g) {
		g.drawImage(sImage, 0, 0, null);
		g.dispose();		
	}

}
