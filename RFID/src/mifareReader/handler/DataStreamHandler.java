/**
 * DataStreamHandler.java
 * --------------------------
 * Schreibt und sendet Daten.
 */
package mifareReader.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import mifareReader.Report;

public class DataStreamHandler {
	
	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	private int code_buffer = -1;
	
	public DataStreamHandler() {
		
	}
	
	public DataStreamHandler(BufferedInputStream in, BufferedOutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	/**
	 * Schreibt Daten in den Stream
	 * @param query Byte array
	 */
	public void request(byte[] query) {
		try {		
			code_buffer = query[2];
			
			out.write(query);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Report respond() {
		try {
			int soh = in.read();
			
			if(soh != -1) {
				
				int address = in.read();
				int queryFunction = in.read();
				int dataLength = in.read();
				int[] dataBytes = new int[dataLength];
				
				for(int i=0;i<dataBytes.length;i++) {
					dataBytes[i] = in.read();
				}
				
				int[] crc = new int[2];
				crc[0] = in.read();
				crc[1] = in.read();
				
//				Report r = new Report(soh, address, queryFunction, dataLength, dataBytes, crc);
				// Erzeut ein Report Objekt mit den relevaten Daten.
				Report r = new Report(soh, address, queryFunction, dataLength, dataBytes, crc, code_buffer);
				
//				System.out.println(r.toString());
				
				code_buffer = -1;
				
				return r;				
			}
			
			System.out.println("no response");
		} catch (Exception e) {			
			e.printStackTrace();			
		}
		return null;
	}

}
