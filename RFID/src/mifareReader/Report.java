package mifareReader;

import java.util.Arrays;

public class Report {
	
	private int soh;
	private int address;
	private int queryFunction;
	private int dataLength;
	private int[] dataBytes;
	private int[] crc;
	
	private int code_buffer;
	
	public Report() {
		
	}
	
	/**
	 * 
	 * @param s
	 * @param a
	 * @param f
	 * @param l
	 * @param d
	 * @param c
	 */
	public Report(int s, int a, int f, int l, int[] d, int[] c) {
		this.soh = s;
		this.address = a;
		this.queryFunction = f;
		this.dataLength = l;
		this.dataBytes = d;
		this.crc = c;
	}
	
	/**
	 * 
	 * @param s
	 * @param a
	 * @param f
	 * @param l
	 * @param d
	 * @param c
	 * @param code
	 */
	public Report(int s, int a, int f, int l, int[] d, int[] c, int code) {
		this.soh = s;
		this.address = a;
		this.queryFunction = f;
		this.dataLength = l;
		this.dataBytes = d;
		this.crc = c;
		this.code_buffer = code;
	}

	public int getSoh() {
		return soh;
	}

	public int getAddress() {
		return address;
	}

	public int getQueryFunction() {
		return queryFunction;
	}

	public int getDataLength() {
		return dataLength;
	}

	public int[] getDataBytes() {
		return dataBytes;
	}

	public int[] getCrc() {
		return crc;
	}

	public int getCode_buffer() {
		return code_buffer;
	}

	public void setSoh(int soh) {
		this.soh = soh;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public void setQueryFunction(int queryFunction) {
		this.queryFunction = queryFunction;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public void setDataBytes(int[] dataBytes) {
		this.dataBytes = dataBytes;
	}

	public void setCrc(int[] crc) {
		this.crc = crc;
	}

	public void setCode_buffer(int code_buffer) {
		this.code_buffer = code_buffer;
	}

	@Override
	public String toString() {
		return "Report [soh=" + soh + ", address=" + address
				+ ", queryFunction=" + queryFunction + ", dataLength="
				+ dataLength + ", dataBytes=" + Arrays.toString(dataBytes)
				+ ", crc=" + Arrays.toString(crc) + ", code_buffer="
				+ code_buffer + "]";
	}

	
}
