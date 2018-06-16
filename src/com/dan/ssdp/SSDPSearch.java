package com.dan.ssdp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author Dan
 * @version 1.0
 * @since 1.0
 * 
 * <h1>SSDPSearch</h1>
 * The SSDPSearch class allows the user to create, send and receive SSDP MSEARCH's to find UPnP hardware devices on the same local area network.  
 */

public class SSDPSearch {

	private final static String REQUEST_HEADER = "M-SEARCH * HTTP/1.1\r\n";
	private final static String HOST_HEADER = "HOST: 239.255.255.250:1900\r\n";
	private final static String MAN_HEADER = "MAN: \"ssdp:discover\"\r\n";
	private final static String MX_HEADER = "MX: %s\r\n";
	private final static String ST_HEADER = "ST: %s\r\n";
	/**
	 * A search target that all devices that implement SSDP should respond to
	 */
	public final static String SSDP_ALL = "ssdp:all";
	private final static String USERAGNT_HEADER = "USER-AGENT: %s\r\n";
	private final static String LINE_BREAK = "\r\n";
	private final static String MSEARCH_ADDRESS = "239.255.255.250";
	private final static int MSEARCH_PORT = 1900;
	private final static int RECIEVE_LENGTH = 1024;
	
	private static DatagramPacket sendPacket;
	private static DatagramPacket recievePacket = new DatagramPacket(new byte[RECIEVE_LENGTH], RECIEVE_LENGTH);
	private static DatagramSocket socket;
	
	/**
	 * The makeRequest method allows the user to create a search request with all required fields
	 * @param mx The MX of the search is the maximum wait time (in seconds) that the program should wait for responses
	 * @param st The search target of the search must be in a set format as outlined in page 31 of <a href="http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf">The UPnP Device Architecture</a>
	 * @return String - Returns a formatted SSDP MSEARCH Request with MX and ST of parameters
	 */
	public static String makeRequest(int mx, String st) {
		
		//Creates The Search Request
		String request = 
				REQUEST_HEADER + 
				HOST_HEADER + 
				MAN_HEADER + 
				String.format(MX_HEADER.toString(), mx) + 
				String.format(ST_HEADER, st) +
				LINE_BREAK;
		return request;
	}
	
	/**
	 * The makeRequest method allows the user to create a search request with the optional useragent field
	 * @param mx The MX of the search is the maximum wait time (in seconds) that the program should wait for responses
	 * @param st The search target of the search must be in a set format as outlined in page 31 of <a href="http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf">The UPnP Device Architecture</a>
	 * @param useragent The useragent of the search in format OS/version UPnP/1.1 product/version
	 * @return String - Returns a formatted SSDP MSEARCH Request with MX and ST of parameters
	 */
	public static String makeRequest(int mx, String st, String useragent) {
		
		//Creates The Search Request with useragent
		String request = 
				REQUEST_HEADER + 
				HOST_HEADER + 
				MAN_HEADER + 
				String.format(MX_HEADER.toString(), mx) + 
				String.format(ST_HEADER, st) +
				String.format(USERAGNT_HEADER, useragent) +
				LINE_BREAK;
		return request;
	}
	
	/**
	 * The makeRequest method allows the user to create a search request which includes any additional header fields that the user wishes
	 * @param mx The MX of the search is the maximum wait time (in seconds) that the program should wait for responses
	 * @param st The search target of the search must be in a set format as outlined in page 31 of <a href="http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf">The UPnP Device Architecture</a>
	 * @param additional Any additional headers that the user wants added to the request they should be in the form [header]: [value] without linebreaks as these are added automatically
	 * @return String - Returns a formatted SSDP MSEARCH Request with MX and ST of parameters
	 */
	public static String makeRequest(int mx, String st, String[] additional) {
		
		//Creates The Custom Search Request
		StringBuilder builder = new StringBuilder();
		for(String header:additional) {
			
			builder.append(header).append(LINE_BREAK);
		}
		
		String additionalHeaders = builder.toString();
		
		String request = 
				REQUEST_HEADER + 
				HOST_HEADER + 
				MAN_HEADER + 
				String.format(MX_HEADER.toString(), mx) + 
				String.format(ST_HEADER, st) +
				additionalHeaders +
				LINE_BREAK;
		return request;
	}
	
	/**
	 * Sends the given SSDP MSEARCH request to 239.255.255.250:1900 to look for devices
	 * @param request The SSDP MSEARCH request to be sent
	 * @throws UnknownHostException Caused by a malformed URL
	 * @throws SocketException Caused by a network failure
	 * @throws IOException Caused by a network failure
	 */
	public static void sendData(String request) throws UnknownHostException, SocketException, IOException{
		
		sendPacket = new DatagramPacket(request.getBytes(), request.length(), InetAddress.getByName(MSEARCH_ADDRESS), MSEARCH_PORT);
		socket = new DatagramSocket();
		socket.send(sendPacket);
	}
	
	/**
	 * Receives any sent SSDP replies 
	 * @param timeout The time in ms until the socket will timeout at which point an exception will be thrown
	 * @return String - The SSDP Response 
	 * @throws IOException Caused by network error or socket timeout
	 */
	public static String recieveData(int timeout) throws IOException {
		
		socket.setSoTimeout(timeout);
		socket.receive(recievePacket);
		String responce = new String(recievePacket.getData());
		return responce;
	}
}