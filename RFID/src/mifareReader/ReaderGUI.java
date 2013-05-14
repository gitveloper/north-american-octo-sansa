package mifareReader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import mifareReader.handler.ReaderHandler;
import mifareReader.misc.CodeCommands;

public class ReaderGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int MAX_DATA_LENGTH = 16;

	private JPanel panel;
	
	private JLabel statusbar;
	private JLabel key_limit;
	
	private JTextField serialnumber;
	private JTextField keyfield;
	private JTextField inputfield;
	
	private JComboBox<String> keys;
	private JComboBox<String> sectors;
	private JComboBox<String> blocks;
	private JComboBox<String> style;
	
	private JTextArea log;	
	private JScrollPane logscroll;
	
	private JButton connect;
	private JButton disconnect;
	private JButton getVersion;
	private JButton polling;
	private JButton request;
	private JButton anticoll;
	private JButton selcard;
	private JButton reset;
	private JButton exit;
	private JButton authkey;
	private JButton write;
	private JButton read;

	private ReaderHandler handler;

	public ReaderGUI() {
		initUI();
	}

	public ReaderGUI(ReaderHandler rh) {
		this.handler = rh;
		initUI();
	}

	public void initUI() {
		serialnumber = new JTextField(20);
		keyfield = new JTextField(20);
		inputfield = new JTextField(20);
		statusbar = new JLabel(" ");
		key_limit = new JLabel();
		
		panel = new JPanel();

		this.getContentPane().add(panel);		

		statusbar.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		// CONNECT
		connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-connect");
				if (!handler.isConnected()) {
					String port = JOptionPane.showInputDialog(null,
							"Available ports: " + handler.showAvailablePorts(),
							handler.getDefaultPort());
					if (port != null) {
						if (handler.showAvailablePorts().contains(port)) {
							handler.start(port);
							handler.reset(CodeCommands.RESET.getCode());
							
							setComponentsEnabled(true);

							log.append(handler.showSerialPortInfo() + "\n");
							log.append("Connected...\n");

							statusbar.setText("Connected");
						} else {
							log.append("Port [" + port + "] not available!\n");
						}
					}
				} else {
					log.append("Already connected!\n");
				}
			}
		});
		panel.add(connect);

		// DISCONNECT
		disconnect = new JButton("Disconnect");
		disconnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-disconnect");
				if (handler.isConnected()) {
					handler.stop();
					
					setComponentsEnabled(false);

					serialnumber.setText("<no serial number>");
					log.append("Disconnected...\n");

					statusbar.setText("Not connected");
				} else {
					log.append("Not connected!\n");
				}
			}
		});
		panel.add(disconnect);

		// GET VERSION
		getVersion = new JButton("Get Version");
		getVersion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-get-version");
				if (handler.isConnected()) {
					handler.getVersion(CodeCommands.GET_VERSION.getCode());
					log.append(handler.getFirmwareVersion() + "\n");
				}
			}
		});
		panel.add(getVersion);

		// POLLING
		polling = new JButton("Polling");
		polling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-polling");
				if (handler.isConnected()) {
					handler.polling(CodeCommands.POLLING.getCode());
				}
			}
		});
		panel.add(polling);

		// REQUEST
		request = new JButton("Request");
		request.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-request");
				System.out.println(handler.cardDetected());
				if (handler.isConnected() && handler.cardDetected()) {
					handler.request(CodeCommands.REQUEST.getCode());
					log.append("Card Class Type: " + "\n");
				}

			}
		});
		panel.add(request);

		// ANTI-COLLISION
		anticoll = new JButton("Anti-Collision");
		anticoll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-anti-collision");
				if (handler.isConnected()) {
					handler.antiCollision(CodeCommands.ANTI_COLLISION.getCode());

					if (handler.cardDetected()) {
						serialnumber.setText((handler.showSerialnumber())
								.toUpperCase());

						log.append("Card with S/N("
								+ (handler.showSerialnumber()).toUpperCase()
								+ ") selected!\n");
					} else {
						serialnumber.setText("<no serial number>");
						log.append("No tag found!\n");
					}
				}
			}
		});
		panel.add(anticoll);

		// SELECT CARD
		selcard = new JButton("Select Card");
		selcard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-reset");
				if (handler.isConnected()) {
					if (handler.snAvailable()) {
						handler.selectCard(CodeCommands.Select_Card.getCode());
					} else {
						log.append("No serial number!\n");
					}
				}
			}
		});
		panel.add(selcard);

		// RESET
		reset = new JButton("Reset Card");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-reset-card");
				if (handler.isConnected()) {
					handler.reset(CodeCommands.RESET.getCode());

					serialnumber.setText("<no serial number>");
				}
			}
		});
		panel.add(reset);

		// EXIT
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-exit");
				System.out.println("EXIT");
				System.exit(0);
			}
		});
		panel.add(exit);

		// TEXTFIELD > Serial Number
		serialnumber.setText("<no serial number>");
		serialnumber.setEditable(false);
		panel.add(serialnumber);

		// COMBOBOX > Keys
		panel.add(addKeyBox());

		// TEXTFIELD > Key
		keyfield.setText(handler.getKeyA());
		keyfield.setEditable(false);
		panel.add(keyfield);

		// COMBOBOX > Sectors
		panel.add(addSectorBox());

		// AUTHENTICATE KEY AND SECTOR
		authkey = new JButton("Auth Key");
		authkey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-reset");
				if (handler.isConnected()) {

					// KEY A
					if (keys.getSelectedIndex() == 0) {
						handler.authenticateKey(
								CodeCommands.AUTHENTICATE_WITH_KEY.getCode(),
								0x60, keyfield.getText(),
								sectors.getSelectedIndex());
					}
					// KEY B
					if (keys.getSelectedIndex() == 1) {
						handler.authenticateKey(
								CodeCommands.AUTHENTICATE_WITH_KEY.getCode(),
								0x61, keyfield.getText(),
								sectors.getSelectedIndex());
					}
				}
			}
		});
		panel.add(authkey);

		// READ
		read = new JButton("Read");
		read.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (handler.isConnected()) {
					handler.readBlock(CodeCommands.READ_BLOCK.getCode(),
							blocks.getSelectedIndex());
					
					inputfield.setText(handler.getBlockContent().replaceAll("[\u0000-\u001f]", ""));
					setKeyLimitLabelText(MAX_DATA_LENGTH - handler.getBlockContent().length());
				}
			}
		});
		panel.add(read);

		// COMBOBOX > Blocks
		panel.add(addBlockBox());

		// TEXTFIELD > Input
		inputfield.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				int len = MAX_DATA_LENGTH - inputfield.getText().length();
				setKeyLimitLabelText(len);
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		panel.add(inputfield);
		
		// LABEL > Key limit
		key_limit.setText("[16 characters left]");
		panel.add(key_limit);

		// COMBOBOX > Blocks
		panel.add(addStyleBox());

		// WRITE
		write = new JButton("Write");
		write.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("button-write");
				if (handler.isConnected()) {
					if(inputfield.getText().length() <= 16) {
						handler.writeBlock(CodeCommands.WRITE_BLOCK.getCode(),
								blocks.getSelectedIndex(), inputfield.getText());
						log.append("INDEX" + blocks.getSelectedIndex());
					} else {
						log.append("Data exceeds length of 16 characters!");
					}
				}
			}
		});
		panel.add(write);

		// TEXTAREA
		panel.add(addTextArea());

		// STATUSBAR
		statusbar.setText("Not connected");
		this.add(statusbar, BorderLayout.SOUTH);
		
		// COMPONENT BOUNDS
		panel.setLayout(null);
		setComponentBounds();
				
		// ENABLE COMPONENTS		
		setComponentsEnabled(false);

		// Frame Settings
		this.setTitle("RFID Reader (Mifare Classic 4k)");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private JComboBox<String> addKeyBox() {
		String[] kab = new String[] { "KeyA", "KeyB" };
		keys = new JComboBox<String>(kab);		
		keys.setSelectedIndex(0);
		keys.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if (keys.getSelectedIndex() == 0) {
					keyfield.setText(handler.getKeyA());
				}
				if (keys.getSelectedIndex() == 1) {
					keyfield.setText(handler.getKeyB());
				}
			}
		});
		return keys;
	}

	private JComboBox<String> addSectorBox() {
		String[] sec = handler.getMifareSectors();
		sectors = new JComboBox<String>(sec);		
		sectors.setSelectedIndex(0);
		return sectors;
	}

	private JComboBox<String> addBlockBox() {
		String[] blo;
		blo = handler.getMirfareBlocks4k();
		blocks = new JComboBox<String>(blo);		
		blocks.setSelectedIndex(0);
		return blocks;
	}

	private JComboBox<String> addStyleBox() {
		String[] sty = new String[] { "as Int", "as Hex" };
		style = new JComboBox<String>(sty);		
		style.setSelectedIndex(0);
		style.setVisible(false);
		style.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return style;
	}

	private JScrollPane addTextArea() {
		log = new JTextArea();
		logscroll = new JScrollPane();
		log.setColumns(30);
		log.setRows(8);
		log.setEditable(false);
		logscroll.getViewport().add(log);
		return logscroll;
	}
	
	private void setComponentBounds() {
		// BUTTONS
		connect.setBounds(10, 10, 120, 25);
		disconnect.setBounds(10, 40, 120, 25);
		getVersion.setBounds(10, 85, 120, 25);
		polling.setBounds(10, 130, 120, 25);
		request.setBounds(10, 160, 120, 25);
		anticoll.setBounds(10, 190, 120, 25);
		selcard.setBounds(160, 10, 100, 25);
		reset.setBounds(10, 230, 120, 25);
		exit.setBounds(10, 305, 100, 25);
		write.setBounds(160, 140, 75, 25);
		authkey.setBounds(415, 60, 100, 25);
		read.setBounds(160, 110, 75, 25);
		
		// OTHER COMPONENTS
		serialnumber.setBounds(265, 10, 180, 25);
		keyfield.setBounds(225, 60, 100, 25);		
		inputfield.setBounds(320, 110, 255, 25);		
		key_limit.setBounds(320, 140, 130, 25);
		keys.setBounds(160, 60, 60, 25);
		sectors.setBounds(330, 60, 80, 25);
		blocks.setBounds(240, 110, 75, 25);
		style.setBounds(505, 140, 70, 25);
		logscroll.setBounds(160, 190, 415, 140);
	}
	
	private void setComponentsEnabled(boolean enabled) {
		// BUTTONS
		polling.setEnabled(enabled);
		getVersion.setEnabled(enabled);
		request.setEnabled(enabled);
		anticoll.setEnabled(enabled);
		selcard.setEnabled(enabled);
		reset.setEnabled(enabled);
		authkey.setEnabled(enabled);
		write.setEnabled(enabled);
		read.setEnabled(enabled);
		
		// OTHER COMPONENTS
		logscroll.setEnabled(enabled);
		style.setEnabled(enabled);
		sectors.setEnabled(enabled);
		blocks.setEnabled(enabled);
		inputfield.setEnabled(enabled);
		serialnumber.setEnabled(enabled);
		keys.setEnabled(enabled);
		keyfield.setEnabled(enabled);
		key_limit.setEnabled(enabled);
	}
	
	private void setKeyLimitLabelText(int curr) {
		key_limit.setText("["+curr+" characters left]");
	}
}
