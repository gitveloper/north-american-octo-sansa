package mifareReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import mifareReader.handler.DataStreamHandler;
import mifareReader.handler.ReportHandler;


import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Device {

	private final int SERIAL_PORT_TIMEOUT = 500;

	private SerialPort serialPort;
	private CommPortIdentifier cpid;

	private final int baudrate = 19200;
	private final int dataBits = SerialPort.DATABITS_8;
	private final int stopBits = SerialPort.STOPBITS_1;
	private final int parity = SerialPort.PARITY_NONE;
	private final int flowcontrolMode = SerialPort.FLOWCONTROL_NONE;
//	private final boolean rts = false;
//	private final boolean dtr = false;
//	private final byte eof = 0x1a;

	private InputStream in;
	private OutputStream out;

	private DataStreamHandler dsh;
	public ReportHandler rh;
	private Report r;

	private List<String> availablePorts;

	private boolean connected = false;	
	private boolean notify = true;
	private boolean data_stream_ready = false;
	private boolean report_handler_ready = false;
	private boolean transmitting = false;
	
	private String owner = "Reader";

	public Device() {
		availablePorts = scanAvailablePorts();
	}

	// CONNECTION

	/**
	 * 
	 * @param port
	 */
	public void connect(String port) {
		try {
			if (availablePorts.contains(port) && !connected) {
				cpid = CommPortIdentifier.getPortIdentifier(port);

				serialPort = (SerialPort) cpid.open(owner, SERIAL_PORT_TIMEOUT);
				serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
				serialPort.setFlowControlMode(flowcontrolMode);
				serialPort.notifyOnDataAvailable(notify);
				
				serialPort.addEventListener(new ListenOnPort());

				setDataStreamHandler();
				setReportHandler();

				connected = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	public void disconnect() {
		serialPort.removeEventListener();
		serialPort.close();
		cpid = null;

		connected = false;
		transmitting = false;
		data_stream_ready = false;
		report_handler_ready = false;
	}

	/**
	 * 
	 * @return
	 */
	private List<String> scanAvailablePorts() {
		availablePorts = new LinkedList<String>();

		CommPortIdentifier serialPortId;
		Enumeration enumComm;

		enumComm = CommPortIdentifier.getPortIdentifiers();
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				availablePorts.add(serialPortId.getName());
			}
		}

		return availablePorts;
	}
	
	/*
	 * 
	 */
	private void setDataStreamHandler() {
		try {
			if (!data_stream_ready) {
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				dsh = new DataStreamHandler(new BufferedInputStream(in), new BufferedOutputStream(out));
				data_stream_ready = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	private void setReportHandler() {
		if (!report_handler_ready) {
			rh = new ReportHandler();
			report_handler_ready = true;
		}
	}

	// DATA

	/**
	 * 
	 * @param query
	 */
	public void query(byte[] query) {
		try {
			
			System.out.println("Device.query() "+Arrays.toString(query));
			
			dsh.request(query);
			transmitting = true;
			listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param code
	 */
	public void report(int code) {
		try {
			rh.createReadableReport(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	public void listen() {
		long start = System.currentTimeMillis();
		while (transmitting) {
			System.out.print("");
		}
		long end = System.currentTimeMillis();
		System.out.println("runtime: " + (end - start));
	}

	/*
	 * 
	 */
	public void dataStreamAvailable() {
		try {
			r = null;
			transmitting = true;
			while (serialPort.getInputStream().available() > 0) {
				r = dsh.respond();
			}
			if (r != null) {
				rh.addReport(r);
				transmitting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// GET/SET

	// TODO
	public ReportHandler getReportHandler() {
		return rh;
	}

	// ENDE

	public List<String> getAvailablePorts() {
		return availablePorts;
	}
	
	public String getSerialPortInfo() {
		return "Connecting to port: " + serialPort.getName();
	}

//	public void setTransmitting(boolean t) {
//		transmitting = t;
//	}

	// BOOLS
	
	public boolean isConnected() {
		return connected;
	}

	public boolean isReady() {
		return data_stream_ready;
	}

	public boolean isReporting() {
		return report_handler_ready;
	}
	
	public boolean isTransmitting() {
		return transmitting;
	}

	// SERIALPORT EVENT LISTENER
	
	class ListenOnPort implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					dataStreamAvailable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}