package bezahlsystem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import datenbank.DBHandler;

import mifareReader.ReaderGUI;
import mifareReader.handler.ReaderHandler;
import mifareReader.misc.CodeCommands;

public class BezahlGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7617094430784679147L;
	
	private JPanel panel;
	private JButton bget_credit, badd_credit, bpay;
	private JTextField tcredit, tcredit_add, tpay, tfirst_name, tlast_name;
	private JLabel lsuccess_msg, lfirst_name, llast_name;
	private JMenuBar menuBar;
	private JMenu admin;
	private JMenuItem new_db, set_db, open_admin, open_db;
	private JTextArea textarea;
	private JScrollPane scrollpane;

	private int b_height = 20;
	private int b_width = 100;
	
	private DecimalFormat format = new DecimalFormat("#0.00");

	public static ReaderHandler handler;
	private DBHandler db;

	private int nameIndex = 1;
	private int vornameIndex = 2;
	private int strIndex = 3;
	private int plzIndex = 4;
	private int kundennrIndex = 5;
	private int guthabenIndex = 6;
	private int transIndex = 7;

	public BezahlGUI() {

	}

	public BezahlGUI(ReaderHandler rh, DBHandler database) {
		createGUI(rh, database);
	}

	private void createGUI(ReaderHandler rh, DBHandler database) {

		this.handler = rh;
		this.db = database;
		
		final Connection conn = db.setConn();

		panel = new JPanel();
		panel.setLayout(null);
		this.getContentPane().add(panel);

		// Buttons erstellen
		bget_credit = new JButton("Get Credit");
		bget_credit.setBounds(20, 50, b_width, b_height);
		panel.add(bget_credit);

		badd_credit = new JButton("Add Credit");
		badd_credit.setBounds(300, 50, b_width, b_height);
		panel.add(badd_credit);

		bpay = new JButton("Pay");
		bpay.setBounds(20, 80, b_width, b_height);
		panel.add(bpay);

		// Textfelder erstellen
		tcredit = new JTextField("<Credit>");
		tcredit.setEditable(false);
		tcredit.setBounds(160, 50, b_width, b_height);
		panel.add(tcredit);

		tcredit_add = new JTextField("");
		tcredit_add.setEditable(true);
		tcredit_add.setBounds(440, 50, b_width, b_height);
		panel.add(tcredit_add);

		tpay = new JTextField("");
		tpay.setEditable(true);
		tpay.setBounds(160, 80, b_width, b_height);
		panel.add(tpay);

		tfirst_name = new JTextField("<First Name>");
		tfirst_name.setEditable(false);
		tfirst_name.setBounds(140, 20, b_width + 20, b_height);
		panel.add(tfirst_name);

		tlast_name = new JTextField("<Last Name>");
		tlast_name.setEditable(false);
		tlast_name.setBounds(420, 20, b_width + 20, b_height);
		panel.add(tlast_name);

		// Labels erstellen
		lsuccess_msg = new JLabel("<success msg>");
		lsuccess_msg.setForeground(Color.GREEN);
		lsuccess_msg.setBounds(300, 80, b_width * 2, b_height);
		panel.add(lsuccess_msg);

		lfirst_name = new JLabel("First Name");
		lfirst_name.setBounds(20, 20, b_width, b_height);
		panel.add(lfirst_name);

		llast_name = new JLabel("Last Name");
		llast_name.setBounds(300, 20, b_width, b_height);
		panel.add(llast_name);

		// Textfenster hinzuf�gen
		panel.add(addTextArea());

		// MenuBar erstellen
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);

		// MenuItems erstellen
		new_db = new JMenuItem("New DB", KeyEvent.VK_N);
		menu.add(new_db);
		set_db = new JMenuItem("Set DB", KeyEvent.VK_S);
		menu.add(set_db);

		admin = new JMenu("Settings");
		menuBar.add(admin);
		open_admin = new JMenuItem("Open Admin", KeyEvent.VK_A);
		admin.add(open_admin);
		open_db = new JMenuItem("Open DB", KeyEvent.VK_D);
		admin.add(open_db);

		// Ausf�hren bei Knopfdruck: "New DB"
		new_db.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textarea.append("new_db pressed");

			}
		});
		// Ausf�hren bei Knopfdruck: "open admin"
		open_admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
			}
		});
		

		bget_credit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				// TODO Auto-generated method stub
				if (handler.isConnected()) {
					handler.antiCollision(CodeCommands.ANTI_COLLISION.getCode());
					if (handler.cardDetected()) {
						if (handler.snAvailable()) {
							handler.selectCard(CodeCommands.Select_Card
									.getCode());
							
							//authentifizieren der des Sektors der Karte
							handler.authenticateKey(
									CodeCommands.AUTHENTICATE_WITH_KEY
											.getCode(), 0x60, "FFFFFFFFFFFF", 33);
							
							//lesen des Names aus der Karte, Name in Textfield schreiben und ausgeben
							handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
									nameIndex);
							tfirst_name.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
							textarea.append("Name: "+tfirst_name.getText()+"\n");

							//Lesen des Vornamen aus der Karte, Vorname in Textfield schreiben und ausgeben
							handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
									vornameIndex);
							tlast_name.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
							textarea.append("Vorname: "+tlast_name.getText()+"\n");
							
							//Lesen der Kundennummer aus der Karte und in einen int umwandeln
							handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
									kundennrIndex);
							
							String nr = handler.getBlockContent().replaceAll("[\u0000-\u001f]", "");
							int kundenNr = Integer.valueOf(nr);
							
							//Guthaben per Kundennummer aus der Datenbank holen
							double guthaben = db.getGuthaben(conn, kundenNr);
							
							//Lesen des Guthaben aus der Karte und in double umwandeln
							handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
									6);							
							double guthaben_karte = Double.valueOf(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
							
							//Guthaben vergleich von Karte und Datenbank(Datenbank liefert Ist-bzw Soll-Wert
							//wenn Guthaben gleich ist wird fortgefahren, wenn nicht wird Vorgang abgebrochen
							if(guthaben == guthaben_karte){
								textarea.append("Guthaben: "+guthaben+"\n");
								tcredit.setText(formatGuthaben(guthaben));
								lsuccess_msg.setText("Kartendaten OK"); lsuccess_msg.setForeground(Color.GREEN);
								
							}else{
								textarea.append("Das Guthaben auf der Karte stimmt nicht mit der Datenbank �berein"+"\n");
								textarea.append("Guthaben: "+guthaben+"\n");
								textarea.append("Guthaben Karte: "+guthaben_karte+"\n");
								textarea.append("Bitte verwahren sie die Karte"+"\n");
								lsuccess_msg.setText("Guthaben error der Karte");
								lsuccess_msg.setForeground(Color.RED);
								handler.reset(CodeCommands.RESET.getCode());
							}
							
							
							System.out.println(guthaben);
							
						}
					}
				}
			}
		});
		
		badd_credit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (handler.isConnected()) {
					if(tcredit_add.getText().length() <= 16) {
						
						//Lesen der Kundennummer aus der Karte und in einen int umwandeln
						handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
									kundennrIndex);
							
						String nr = handler.getBlockContent().replaceAll("[\u0000-\u001f]", "");
						int kundenNr = Integer.valueOf(nr);
						
						
						
						//Neues Guthaben aus aktuellem Guthaben und zu erh�hendem Guthaben errechnen
						double guthaben = db.getGuthaben(conn, kundenNr);
						double add = Double.valueOf(tcredit_add.getText().replace("-", ""))+guthaben;
						String neu = formatGuthaben(add);
						
						//Neues Guthaben auf die Karte schreiben
						handler.writeBlock(CodeCommands.WRITE_BLOCK.getCode(),
								guthabenIndex, neu);
						
						//Neues Guthaben in die Datenbank schreiben
						db.addGuthaben(conn, Double.valueOf(tcredit_add.getText()), kundenNr);
						
						//Aktuelle Zeit
						long now = System.currentTimeMillis();				       
				        				        
				        //Zeit in die Datenbank schreiben
				        db.setTime(conn, now, kundenNr);
				        
				        //Zeit in auf die Karte schreiben
				        String time = String.valueOf(now);				        
				        handler.writeBlock(CodeCommands.WRITE_BLOCK.getCode(),
								transIndex, time);        
				        
						
						textarea.append(tcredit_add.getText()+" wurde auf die Karte gebucht"+"\n");
						lsuccess_msg.setText("Buchung erfolgreich"); lsuccess_msg.setForeground(Color.GREEN);
						
						tcredit_add.setText("");
						tcredit.setText(neu);
						
					} 
				}
				
			}
		});
		
		
		bpay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (handler.isConnected()) {
					
					//Lesen der Kundennummer aus der Karte und in einen int umwandeln
					handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
							kundennrIndex);
					String nr = handler.getBlockContent().replaceAll("[\u0000-\u001f]", "");
					int kundenNr = Integer.valueOf(nr);
					
					//Guthaben per Kundennummer aus der Datenbank holen
					double guthaben = db.getGuthaben(conn, kundenNr);					
					
					//Zu zahlender Wert aus Textfield lesen und in double umwandeln
					double pay = Double.valueOf(tpay.getText().replace("-", ""));
					
					//Vergleich ob Zu zahlender Wert gr��er als Aktuelles Guthaben ist,
					//wenn ja wird der Vorgang abgebrochen
					if(pay > guthaben){
						textarea.append("Das Guthaben auf der Karte reicht nicht aus"+"\n");
						textarea.append("Guthaben: "+guthaben+"\n");
						textarea.append("Preis: "+pay+"\n");
						textarea.append("Das Guthaben muss aufgeladen werden"+"\n");
						lsuccess_msg.setText("Guthaben error der Karte");
						lsuccess_msg.setForeground(Color.RED);
					}else{
						
						//Neues Guthaben errechnen und auf die Karte schreiben
						guthaben = guthaben - pay;
						String neu = formatGuthaben(guthaben);
						handler.writeBlock(CodeCommands.WRITE_BLOCK.getCode(),
								guthabenIndex, neu);
						//Neues Guthaben in die Datenbank schreiben
						db.setGuthaben(conn, guthaben, kundenNr);
						
						//Aktuelle Zeit
						long now = System.currentTimeMillis();      
				        				     
						//Aktuelle Zeit in die Datenbank schreiben
				        db.setTime(conn, now, kundenNr);
				        
				        //Aktuelle Zeit auf die Karte schreiben
				        String time = String.valueOf(now);				        
				        handler.writeBlock(CodeCommands.WRITE_BLOCK.getCode(),
								transIndex, time);
				        
				        textarea.append(tpay.getText()+" wurde von der Karte gebucht");
						lsuccess_msg.setText("Buchung erfolgreich"); lsuccess_msg.setForeground(Color.GREEN);
						
						tpay.setText("");
						tcredit.setText(neu);
						
					}
					
				}
				
			}
		});		

		this.setTitle("RFIDPAY");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JScrollPane addTextArea() {
		textarea = new JTextArea();
		scrollpane = new JScrollPane();

		textarea.setColumns(40);
		textarea.setRows(10);
		textarea.setEditable(false);

		scrollpane.getViewport().add(textarea);

		scrollpane.setBounds(20, 120, 500, 200);
		return scrollpane;
	}
	
	private String formatGuthaben(double guthaben) {		
		String formatted = format.format(guthaben).replace(",", ".");
		return formatted;		
	}
}

//Extra Klasse zum einloggen in die Admin GUI
class Login extends JFrame {

	private JButton login;
	private JTextField login_text;
	private JPanel panel;

	private int b_height = 20;
	private int b_width = 100;
	private String pw = "admin";

	public Login() {
		init();
	}

	public void init() {

		panel = new JPanel();
		panel.setLayout(null);
		this.getContentPane().add(panel);

		login = new JButton("Login");
		login.setBounds(20, 20, b_width, b_height);
		panel.add(login);

		login_text = new JTextField("admin");
		login_text.setBounds(140, 20, b_width, b_height);
		login_text.setEditable(true);
		login_text.setCaretPosition(0);
		panel.add(login_text);

		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (pw.equals(login_text.getText())) {
					ReaderGUI readgui = new ReaderGUI(BezahlGUI.handler);
					close();
				} else {
					close();
				}

			}
		});
		this.setTitle("Settings");
		this.setSize(250, 100);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(300, 100);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	public void close() {
		this.dispose();
	}

}
