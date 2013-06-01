package bezahlsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SplashScreen;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mifareReader.handler.ReaderHandler;
import datenbank.DBHandler;

public class Splash extends JPanel implements Runnable {

	private static final long serialVersionUID = 4386862497293410112L;
	private SplashScreen ss = SplashScreen.getSplashScreen();

	private String[] frames = new String[] { "frame0.png", "frame1.png", "frame2.png",
			"frame3.png", "frame0.png", "frame3.png" };

	private Image sImage;
	
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
			for (int i = 0; i < frames.length; i++) {	

				sImage = new ImageIcon(getClass().getResource("../resources/" + frames[i])).getImage();				
				
				System.out.println("...");
				
				repaint();
				
				sImage.flush();
				
				Thread.sleep(450);
			}			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
