/**
 * ReportHandler.java
 * --------------------------
 * Verarbeitet Reports.
 */
package mifareReader.handler;

import java.util.Arrays;
import java.util.LinkedList;

import mifareReader.Report;
import mifareReader.misc.CodeCommands;


public class ReportHandler {

	private LinkedList<Report> reports;

	private Report r;

	private int[] ccBytes;					// Card Class bytes
	private int[] snBytes;					// Serial Number bytes
	private int[] keys;						// Keys

	private String firmware_version;		// Enthaelt die Firmware Version des Lesegeraetes
	private String block_content; 			// Enthaelt die Daten aus dem gelesenen Block als String

	private boolean reset_required = false;	// Reset notwendig nach Anti-Collision
	
	private boolean sn_available = false;	// Serial Number verfuegbar?
	private boolean cc_available = false;	// Card Class verfuegbar?
	
	private boolean card_detected = false;	// Karte verfuegbar?

	public ReportHandler() {
		this.reports = new LinkedList<>();
		
		// Initialisierung
		this.ccBytes = null;
		this.snBytes = null;
		this.keys = null;
	}

	public ReportHandler(LinkedList<Report> r) {
		this.reports = r;
	}

	// REPORTS
	
	/**
	 * Fuegt Report zur ReportListe hinzu
	 * @param r
	 */
	public void addReport(Report r) {
		addReport(r, false);
	}

	/**
	 * Fuegt Report zur ReportListe hinzu
	 * @param r
	 * @param reset_required
	 */
	public void addReport(Report r, boolean reset_required) {
		reports.add(r);
		this.reset_required = reset_required;
	}
	
	/**
	 * Verarbeitet Reports anhand der QueryCodes
	 * @param code QueryCode
	 */
	public void createReadableReport(int code) {
		
		for(int i=0;i<reports.size();i++) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NEW METHOD CALL");			
			System.out.println("card boolean: "+card_detected);			
			System.out.println("report at index: "+reports.get(i).toString());
			
			int function = reports.get(i).getQueryFunction();
			
			System.out.println(">>>>>>>>>>> FUNCTION: " + function);
			
			if(function == CodeCommands.EVN.getCode()) {
				System.out.println("EVN: " + CodeCommands.EVN.getCode());				
				System.out.println("CARD_DETECTED: "+CodeCommands.CARD_DETECTED.getCode());
				System.out.println("CARD_REMOVED: "+CodeCommands.CARD_REMOVED.getCode());
				
				if(reports.get(i).getDataBytes()[0] == CodeCommands.CARD_DETECTED.getCode()) {
					System.out.println("#CARD_DETECTED");
					card_detected = true;					
				}
				if(reports.get(i).getDataBytes()[0] == CodeCommands.CARD_REMOVED.getCode()) {
					System.out.println("#CARD_REMOVED");
					card_detected = false;
				}
			}
			
			if(function == CodeCommands.NAK.getCode()) {
				System.out.println("NAK: " + CodeCommands.NAK.getCode());				
				System.out.println("NO_TAG: "+CodeCommands.NO_TAG_ERROR.getCode());
				
				System.out.println("enum lookup >> " + CodeCommands.NAK.getCodeByInt(reports.get(i).getDataBytes()[0]));
				
				if(reports.get(i).getDataBytes()[0] == CodeCommands.NO_TAG_ERROR.getCode()) {
					System.out.println("#NO_TAG");
					card_detected = false;
				}
				
				
			}

			if(function == CodeCommands.ACK.getCode()) {
				System.out.println("ACK");
				if(reports.get(i).getCode_buffer() == CodeCommands.GET_VERSION.getCode()) {
					firmware_version = hexToString(reports.get(i).getDataBytes());
				}
			}
			
			if(card_detected) {				
				if(function == CodeCommands.ACK.getCode()) {
					System.out.println("ACK");
					if(reports.get(i).getCode_buffer() == CodeCommands.REQUEST.getCode()) {
						ccBytes = reports.get(i).getDataBytes();
					}
					if(reports.get(i).getCode_buffer() == CodeCommands.ANTI_COLLISION.getCode()) {
						snBytes = reports.get(i).getDataBytes();
						sn_available = true;
					}
					if(reports.get(i).getCode_buffer() == CodeCommands.READ_BLOCK.getCode()) {
						block_content = hexToString(reports.get(i).getDataBytes()).replaceAll("[\u0000]", ""); // Entfernt Steuerbefehl (SOH) mit regex
					}
				}
			} else {
//				card_detected = false;
//				snBytes = new int[]{-1};
//				ccBytes = new int[]{-1};
			}			
		}
		reports.clear();
		
	}
	
	/**
	 * Wandelt HEX Werte in lesbaren String um
	 * @param dataBytes
	 * @return
	 */
	private String hexToString(int[] dataBytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dataBytes.length; i++) {
			sb.append((char) dataBytes[i]);
		}
		return sb.toString();
	}

	// GET/SET	

	public LinkedList<Report> getReports() {
		return reports;
	}

	public void setReports(LinkedList<Report> r) {
		this.reports = r;
	}
	
	public int[] getSerialNumer() {
		return snBytes;
	}
	
	public void setSnAvailable(boolean sn) {
		sn_available = sn;
	}
	
	public int[] getCardClass() {
		return ccBytes;
	}
	
	public void setCcAvailable(boolean cc) {
		cc_available = cc;
	}
	
	public int[] getKeys() {
		return keys;
	}
	
	public String getFirmwareVersion() {
		return firmware_version;
	}
	
	public String getBlockContent() {
		return block_content;
	}
	
	// BOOLS

	public boolean snAvailable() {
		return sn_available;
	}
	
	public boolean ccAvailable() {
		return cc_available;
	}

	public boolean cardDetected() {
		return card_detected;
	}

}
