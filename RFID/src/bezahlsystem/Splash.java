package bezahlsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SplashScreen;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import mifareReader.handler.ReaderHandler;
import datenbank.DBHandler;

public class Splash extends JFrame implements Runnable {

	private static final long serialVersionUID = 4386862497293410112L;
	private SplashScreen ss = SplashScreen.getSplashScreen();

	private String[] frames = new String[] { "frame0.png", "frame1.png", "frame2.png",
			"frame3.png", "frame0.png", "frame3.png" };

	private Image sImage;

	public void run() {
		
		setSize(550, 550);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));
		setVisible(true);

		System.out.println("Starte Splash!");
		
		try {			
			for (int i = 0; i < frames.length; i++) {	

				sImage = new ImageIcon(getClass().getResource("../resources/" + frames[i])).getImage();
				
				this.revalidate();
				this.repaint();
				
				System.out.println("...");				
				
				Thread.sleep(450);
				sImage = null;

			}			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dispose();
		
		new BezahlGUI(new ReaderHandler(), new DBHandler());
	}

	public void paint(Graphics g) {
		g.drawImage(sImage, 0, 0, null);
		g.dispose();
	}

}
