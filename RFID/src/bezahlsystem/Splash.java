package bezahlsystem;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import mifareReader.handler.ReaderHandler;
import datenbank.DBHandler;

public class Splash extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 4386862497293410112L;

	public void run(){
		setSize(550, 550);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);
		
		System.out.println("Starte Splash!");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			dispose();
		}
		dispose();
		new BezahlGUI(new ReaderHandler(), new DBHandler());
	}

	public void paint(Graphics g) {
		Image sImage = new ImageIcon(getClass().getResource("resources/splash.png")).getImage();
		g.drawImage(sImage, 0, 0, this);
	}
	
}
