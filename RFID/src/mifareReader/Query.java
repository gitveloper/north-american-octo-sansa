/**
 * Query.java
 * --------------------------
 * Erzeugt die kompletten Packages die vom Reader empfangen werden muessen,
 * damit eine Antwort moeglich wird.
 */
package mifareReader;

import mifareReader.misc.CRC;

public class Query {
	
	private int soh = 0x01; // SOH 
	private int address; // Adresse
	private int queryFunction; // Welche Funktion vom Reader ausgefuehrt werden soll
	private int dataLength; // Laenge des Datenarrays
	private int[] dataBytes; // Bytes im Datenarray
	private int[] crc; // CRC Sum
	
	private byte[] query;
	
	private CRC crc16;
	
	private int crc_length = 2;
	private int int_package_length = 4;
	
	private int crc_istart = 1;
	
	/*
	 * Standardkonstruktor
	 */
	public Query() {
		
	}
	
	/**
	 * Gibt ein byte Array zurueck, welches die Daten enthaelt, 
	 * die vom Reader empfangen werden sollen.
	 * Ist kein Datenarray verfuegbar oder die Laenge gleich 0,
	 * so erfolgt die Rueckgabe mit einem leeren Datenarray
	 * @param function Funktion
	 * @return
	 */
	public byte[] packageQuery(int function) {		
		return packageQuery(function, new int[0]);
	}
	
	/**
	 * Packt ein byte Array das vom Reader verarbeitet werden kann.
	 * @param function Funktion
	 * @param data Daten
	 * @return
	 */
	public byte[] packageQuery(int function, int[] data) {
		
		crc16 = new CRC();
		
		this.soh = 0x01;
		this.address = 0;
		this.queryFunction = function;
		this.dataLength = data.length;
		this.dataBytes = data;

		int package_length = int_package_length + dataLength + crc_length;		
		int[] seg = packageSegment(soh, address, queryFunction, dataLength);		
		int length = seg.length + dataLength;
		int[] seg_crc = new int[length];	
		
		// Arraycoppy: (src, srcPos, dest, destPos, length)
		System.arraycopy(seg, 0, seg_crc, 0, seg.length);
		System.arraycopy(dataBytes, 0, seg_crc, 4, dataLength);		
		int[] crc = crc16.calcCRC16(seg_crc, crc_istart, (seg_crc.length-crc_istart));		
		int[] prep = new int[package_length];
		
		// Arraycoppy: (src, srcPos, dest, destPos, length)
		System.arraycopy(seg, 0, prep, 0, 4);
		System.arraycopy(dataBytes, 0, prep, 4, dataBytes.length);
		System.arraycopy(crc, 0, prep, 4+dataBytes.length, 2);
		
		query = new byte[prep.length];
		
		for(int i=0;i<query.length;i++) {
			query[i] = (byte) prep[i];
		}
		
		return query;
	}
	
	/**
	 * Erzeugt ein Standard Package mit den grundlegenden Informationen
	 * fuer den Reader
	 * @param s SOH
	 * @param a Adresse
	 * @param f Funktion 
	 * @param l Laenge
	 * @return
	 */
	private int[] packageSegment(int s, int a, int f, int l) {
		
		int[] req = new int[4];		
		req[0] = s;
		req[1] = a;
		req[2] = f;
		req[3] = l;
		
		return req;
	}

}
