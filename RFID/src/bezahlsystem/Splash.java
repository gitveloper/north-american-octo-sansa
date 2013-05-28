package bezahlsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JWindow;

import mifareReader.handler.ReaderHandler;
import datenbank.DBHandler;

public class Splash extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 4386862497293410112L;
	
	SplashScreen ss = SplashScreen.getSplashScreen();

	public void run(){
		
		
//		setBackground(new Color(0, 0, 0, 0));
		setSize(550, 550);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setBackground(new Color(1.0f,1.0f,1.0f,0f));
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
		Image sImage = new ImageIcon(getClass().getResource("../resources/splash.png")).getImage();

		g.drawImage(sImage, 0, 0, this);
	}
	
}
