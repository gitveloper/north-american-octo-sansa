package mifareReader.handler;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import mifareReader.Device;
import mifareReader.Query;


public class ReaderHandler {

	private Device device;
	private Query query;

	private ReportHandler rh;
	
	private String default_port = "COM3";

	private String key_a = "FFFFFFFFFFFF";
	private String key_b = "FFFFFFFFFFFF";

	private String[] mifare_blocks_1k = new String[] { "Block 0", "Block 1",
			"Block 2" };
	private String[] mifare_blocks_4k = new String[] { "Block 0", "Block 1",
			"Block 2", "Block 3", "Block 4", "Block 5", "Block 6", "Block 7",
			"Block 8", "Block 9", "Block 10", "Block 11", "Block 12",
			"Block 13", "Block 14", "Block 15" };
	private String[] mifare_sectors = new String[] { "Sector 0", "Sector 1",
			"Sector 2", "Sector 3", "Sector 4", "Sector 5", "Sector 6",
			"Sector 7", "Sector 8", "Sector 9", "Sector 10", "Sector 11",
			"Sector 12", "Sector 13", "Sector 14", "Sector 15", "Sector 16",
			"Sector 17", "Sector 18", "Sector 19", "Sector 20", "Sector 21",
			"Sector 22", "Sector 23", "Sector 24", "Sector 25", "Sector 26",
			"Sector 27", "Sector 28", "Sector 29", "Sector 30", "Sector 31",
			"Sector 32", "Sector 33", "Sector 34", "Sector 35", "Sector 36",
			"Sector 37", "Sector 38", "Sector 39" };

	public ReaderHandler() {
		device = new Device();
		query = new Query();
	}
	
	// INIT

	public void start(String port) {
		device.connect(port);
		rh = device.getReportHandler();
	}

	public void stop() {
		device.disconnect();
	}

	
	// GNet/Mifare Commands
	
	/**
	 * 
	 * @param code
	 */
	public void polling(int code) {
		device.query(query.packageQuery(code));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 */
	public void getVersion(int code) {
		System.out.println("CODE_COMMAND: "+code);
		device.query(query.packageQuery(code));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 */
	public void request(int code) {
		device.query(query.packageQuery(code));
		device.report(code);
	}
	
	/**
	 * 
	 * @param code
	 */
	public void antiCollision(int code) {
		device.query(query.packageQuery(code));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 */
	public void selectCard(int code) {
		if (rh.snAvailable()) {
			device.query(query.packageQuery(code, rh.getSerialNumer()));
			device.report(code);
		} else {
			System.out.println("no serialnnumber");
		}
	}

	/**
	 * 
	 * @param code
	 * @param key_type
	 * @param key
	 * @param sector
	 */
	public void authenticateKey(int code, int key_type, String key, int sector) {
		device.query(query.packageQuery(code, prepareKeyAuth(key_type, key, sector)));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 * @param block
	 */
	public void readBlock(int code, int block) {
		int[] b = new int[1];
		b[0] = block;
		device.query(query.packageQuery(code, b));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 * @param block
	 * @param input
	 */
	public void writeBlock(int code, int block, String input) {
		device.query(query.packageQuery(code, prepareData(input, block)));
		device.report(code);
	}

	/**
	 * 
	 * @param code
	 */
	public void reset(int code) {
		device.query(query.packageQuery(code));
		device.report(code);
		rh.setSnAvailable(false);
	}
	
	// PACKAGE DATA
	
	public int[] prepareKeyAuth(int type, String k, int sector) {
		int[] key_buffer = new int[k.length() / 2];
		int index = 0;
		for (int i = 0; i < k.length(); i += 2) {
			key_buffer[index] = Integer.parseInt(k.substring(i, i + 2), 16);
			index++;
		}
		int[] prep = new int[key_buffer.length + 2];
		prep[0] = type;
		prep[1] = sector;
		System.arraycopy(key_buffer, 0, prep, 2, key_buffer.length);

		return prep;
	}
	
	public int[] prepareData(String w, int b) {

		int[] input_buffer = new int[16];

		int l = w.length() * 2;

		int hexl = 32;

		String hex = "";
		int length;
		if(w.length() != 0) {
			hex = String.format("%0" + l + "x", new BigInteger(w.getBytes(/* YOUR_CHARSET? */)));
			length = hex.length();
		} else {
			length = 0;
		}
		
		int index = 0;
		for (int i = 0; i < hexl; i += 2) {
			if (i < length) {
				input_buffer[index] = Integer.parseInt(hex.substring(i, i + 2),
						16);
			} else {
				input_buffer[index] = 0;
			}
			index++;
		}
		int[] prep = new int[input_buffer.length + 1];
		prep[0] = b;
		System.arraycopy(input_buffer, 0, prep, 1, input_buffer.length);

		return prep;
	}
	
	public String showSerialnumber() {
		int[] snarray = rh.getSerialNumer();
		String sn = "";
		for (int i = 0; i < snarray.length; i++) {
			sn += Integer.toHexString(snarray[i]);
		}
		
		long sn_long = Long.parseLong(sn, 16);
		
		return ""+sn_long;
	}
	
	public String showSerialPortInfo() {
		return device.getSerialPortInfo();
	}
	
	public String showAvailablePorts() {
		
		List<String> ports = device.getAvailablePorts();		
		String aPorts = "";		
		for(int i=0;i<ports.size();i++) {			
			if(ports.size()-1 == i) {
				aPorts += ports.get(i);
			} else {
				aPorts += ports.get(i) + ", ";
			}
		}		
		//aPorts = (String) aPorts.subSequence(0, aPorts.length()-2);
		if(aPorts.length() == 0) {
			return "none";
		}
		
		return aPorts;
	}
	
	public String showLongValue(byte[] data) {
		
		return "";
	}

	// GET/SET
	
	public String getDefaultPort() {
		return default_port;
	}

	public String getKeyA() {
		return key_a;
	}

	public String getKeyB() {
		return key_b;
	}

	public String getBlockContent() {
		return rh.getBlockContent();
	}

	public String getFirmwareVersion() {
		return rh.getFirmwareVersion();
	}

	public int[] getCardClass() {
		return rh.getCardClass();
	}

	public String[] getMifareBlocks1k() {
		return mifare_blocks_1k;
	}
	
	public String[] getMirfareBlocks4k() {
		return mifare_blocks_4k;
	}
	
	public String[] getMifareSectors() {
		return mifare_sectors;
	}	
	
	// BOOLS
	
	public boolean isConnected() {
		return device.isConnected();
	}
	
	public boolean cardDetected() {
		return rh.cardDetected();
	}

	public boolean snAvailable() {
		return rh.snAvailable();
	}
	
	public boolean ccAvailable() {
		return rh.ccAvailable();
	}

}
