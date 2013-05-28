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
	private SplashScreen ss = SplashScreen.getSplashScreen();	
	private int frames = 4;

	public void run(){
		
		
//		setBackground(new Color(0, 0, 0, 0));
		setSize(550, 550);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setBackground(new Color(1.0f,1.0f,1.0f,0f));
		setVisible(true);
		
		System.out.println("Starte Splash!");
		dispose();
		new BezahlGUI(new ReaderHandler(), new DBHandler());
	}

	public void paint(Graphics g) {		
		for(int i=0;i<=frames;i++) {
			Image sImage = new ImageIcon(getClass().getResource("../resources/frame"+i+".png")).getImage();
			g.drawImage(sImage, 0, 0, this);
			g.dispose();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
