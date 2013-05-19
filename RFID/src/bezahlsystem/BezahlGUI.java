package bezahlsystem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
	
	private JPanel panel;
	private JButton bget_credit, badd_credit, bpay;
	private JTextField tcredit, tcredit_add, tpay, tfirst_name, tlast_name ;
	private JLabel lsuccess_msg, lfirst_name, llast_name;
	private JMenuBar menuBar;
	private JMenu admin;
	private JMenuItem new_db, set_db, open_admin,open_db;
	private JTextArea textarea;
	private JScrollPane scrollpane;
	
	private int b_height = 20;
	private int b_width = 100;
	
	private ReaderHandler handler;
	private DBHandler db;
	
	private int nameIndex = 1;
	private int vornameIndex = 2;
	private int strIndex = 3;
	private int kundennrIndex = 4;
	private int guthabenIndex = 5;
	private int transIndex = 6;
	
	
	public BezahlGUI(){
		
	}
	public BezahlGUI(ReaderHandler rh, DBHandler database){
		createGUI(rh, database);
	}

	private void createGUI(ReaderHandler rh, DBHandler database) {
		
		this.handler = rh;
		this.db = database;
		
		panel = new JPanel();
		panel.setLayout(null);
		this.getContentPane().add(panel);
	
		
		//Buttons erstellen
		bget_credit = new JButton("Get Credit");
		bget_credit.setBounds(20, 50, b_width, b_height);
		panel.add(bget_credit);
		
		badd_credit = new JButton("Add Credit");
		badd_credit.setBounds(300, 50, b_width, b_height);
		panel.add(badd_credit);
		
		bpay = new JButton("Pay");
		bpay.setBounds(20, 80, b_width, b_height);
		panel.add(bpay);
		
		
		//Textfelder erstellen
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
		tfirst_name.setBounds(140, 20, b_width+20, b_height);
		panel.add(tfirst_name);
		
		tlast_name = new JTextField("<Last Name>");
		tlast_name.setEditable(false);
		tlast_name.setBounds(420, 20, b_width+20, b_height);
		panel.add(tlast_name);
		
		
		//Labels erstellen
		lsuccess_msg = new JLabel("<success msg>");
		lsuccess_msg.setForeground(Color.GREEN);
		lsuccess_msg.setBounds(300, 80, b_width*2, b_height);
		panel.add(lsuccess_msg);
		
		lfirst_name = new JLabel("First Name");
		lfirst_name.setBounds(20, 20, b_width, b_height);
		panel.add(lfirst_name);
		
		llast_name = new JLabel("Last Name");
		llast_name.setBounds(300, 20, b_width, b_height);
		panel.add(llast_name);
		
		//Textfenster hinzuf�gen
		panel.add(addTextArea());
		
		
		//MenuBar erstellen
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		
		//MenuItems erstellen
		new_db = new JMenuItem("New DB", KeyEvent.VK_N);
		menu.add(new_db);
		set_db = new JMenuItem("Set DB", KeyEvent.VK_S);
		menu.add(set_db);
		
		admin = new JMenu("admin");
		menuBar.add(admin);
		open_admin = new JMenuItem("open Admin", KeyEvent.VK_A);
		admin.add(open_admin);
		open_db = new JMenuItem("open DB", KeyEvent.VK_D);
		admin.add(open_db);
		
		//Ausf�hren bei Knopfdruck: "New DB"
		 new_db.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) {
			   textarea.append("new_db pressed");
			  
			  }
		 	}	  
		 );
		//Ausf�hren bei Knopfdruck: "open admin"
		 open_admin.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) {
				 
				  login login= new login();
				  
				}
			 }	  
		);
			
		bget_credit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				textarea.append("retrieving data from card"+"\n");

					if (handler.isConnected()) {
						handler.antiCollision(CodeCommands.ANTI_COLLISION.getCode());

						if (handler.snAvailable()) {
							handler.selectCard(CodeCommands.Select_Card.getCode());
						}
						handler.authenticateKey(
							CodeCommands.AUTHENTICATE_WITH_KEY.getCode(),
							0x60, "FFFFFFFFFFFF",
							33);
						
						handler.readBlock(CodeCommands.READ_BLOCK.getCode(),nameIndex);						
						tfirst_name.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
						
						handler.readBlock(CodeCommands.READ_BLOCK.getCode(),vornameIndex);						
						tlast_name.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
						
						handler.readBlock(CodeCommands.READ_BLOCK.getCode(),guthabenIndex);						
						tcredit.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
					
						textarea.append("retrieving data complete");
					}else{
						textarea.append("no handler connected"+"\n");
					}
					
								
			}
		});
			 
		
		
		
		
		
		this.setTitle("RFID Reader (Mifare Classic 4k)");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(600, 400);
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
	
	public static void main(String []args){
		BezahlGUI gui = new BezahlGUI();
	}

}
class login extends JFrame {
	
	private JButton login;
	private JTextField login_text;
	private JPanel panel;
	
	private int b_height = 20;
	private int b_width = 100;
	private String pw = "admin";
	
	
	public login(){
		init();
	}
	
	public void init(){
		
		panel = new JPanel();
		panel.setLayout(null);
		this.getContentPane().add(panel);
		
		 login = new JButton("login");
		 login.setBounds(20, 20, b_width, b_height);
		 panel.add(login);
		   
		 login_text = new JTextField();
		 login_text.setBounds(140, 20, b_width, b_height);
		 login_text.setEditable(true);
		 login_text.setCaretPosition(0);
		 panel.add(login_text);
		
		 login.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) {
				  
				  if(pw.equals(login_text.getText())){
					  ReaderGUI readgui = new ReaderGUI(new ReaderHandler());
					close();
				  }
				  else{
					close();  
				  }
				  
				}
			 	}	  
			 );
		 this.setTitle("RFID Reader (Mifare Classic 4k)");
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			this.setSize(300, 100);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
		
	}
	public void close(){
		this.dispose();
	}
	

}
