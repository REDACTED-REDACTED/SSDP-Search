package com.dan.ssdp;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dan
 * @version 1.0
 * @since 1.0
 * 
 * <h1>SSDPResponse</h1>
 * The SSDPResponse class allows users to parse received SSDP responses
 */

public class SSDPResponse {
	
	private final String CACHE_CONTROL_HEADER = "cache-control";
	private final String DATE_HEADER = "date";
	private final String EXT_HEADER = "ext";
	private final String LOCATION_HEADER = "location";
	private final String SERVER_HEADER = "server";
	private final String ST_HEADER = "st";
	private final String USN_HEADER = "usn";
	private final String BOOT_ID_HEADER = "bootid.upnp.org";
	private final String CONFIG_ID_HEADER = "configid.upnp.org";
	private final String SEARCH_PORT_HEADER = "searchport.upnp.org";
	
	private String cacheControl;
	private ZonedDateTime date;
	private String ext;
	private String location;
	private String server;
	private String st;
	private String usn;
	private int bootID;
	private int configID;
	private int searchPort;
	
	/**
	 * A map of all headers in the request.  The key is the header field (in all lower case) and the value is the value of the header in String form.
	 */
	public Map<String, String> headers = new HashMap<String, String>();
	
	public SSDPResponse(String response) {
		
		String[] headers = response.split("\n");
		parseHeaders(headers);
	}
	
	private void parseHeaders(String[] headers) {
		
		String header;
		String value;
		int colon;
		
		for (String line:headers) {
			colon = line.indexOf(":");
			if(colon != -1) {

				header = line.substring(0, colon).trim().toLowerCase();
				value = line.substring(colon + 1, line.length()).trim();
				identifyHeader(header, value);
				this.headers.put(header, value);
			}
		}
	}
	
	private void identifyHeader(String header, String value) {
	
		switch(header) {
		
			case CACHE_CONTROL_HEADER:
				//Cache
				this.cacheControl = value;
				break;
			case DATE_HEADER:
				//Date
				this.date = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
				break;
			case EXT_HEADER:
				//EXT
				this.ext = value;
				break;
			case LOCATION_HEADER:
				//Location
				this.location = value;
				break;
			case SERVER_HEADER:
				//Server
				this.server = value;
				break;
			case ST_HEADER:
				//ST
				this.st = value;
				break;
			case USN_HEADER:
				//USN
				this.usn = value;
				break;
			case BOOT_ID_HEADER:
				//Boot ID
				this.bootID = Integer.valueOf(value);
				break;
			case CONFIG_ID_HEADER:
				//Config ID
				this.configID = Integer.valueOf(value);
				break;
			case SEARCH_PORT_HEADER:
				//Search Port
				this.searchPort = Integer.valueOf(value);
				break;
		}
	}

	/**
	 * Returns the cache control in String form
	 * @return String - The cache control of the response
	 */
	public String getCacheControl() {
		
		return this.cacheControl;
	}
	
	/**
	 * Returns the date of the response
	 * @return ZonedDateTime - The date and time of the response returns null if not set
	 */
	public ZonedDateTime getDate() {
		
		return this.date;
	}
	
	/**
	 * Returns the EXT of the response
	 * @return String - Currently the EXT serves no purpose, however is included in responses, normally with no value
	 */
	public String getEXT() {
		
		return this.ext;
	}
	
	/**
	 * Returns the Location of the response
	 * @return String - URL to the device should be set
	 */
	public String getLocation() {
		
		return this.location;
	}
	
	/**
	 * Returns the Server of the response
	 * @return String - Server on the device should be set
	 */
	public String getServer() {
		
		return this.server;
	}
	
	/**
	 * Returns the search target of the response
	 * @return String - Search Target of the device should be set
	 */
	public String getSearchTarget() {
		
		return this.st;
	}

	/**
	 * Returns the Unique Serial Number of the device sending the response
	 * @return String - the unique serial number of the device sending the response
	 */
	public String getUniqueSerialNumber() {
		
		return this.usn;
	}

	/**
	 * Returns the Boot ID of the response
	 * @return int - the boot id of the response
	 */
	public int getBootID() {
		
		return this.bootID;
	}
	
	/**
	 * Returns the Config ID of the response
	 * @return int - the config id of the response
	 */
	public int getConfigID() {
		
		return this.configID;
	}

	/**
	 * Returns the search port the response uses
	 * @return int - the port the response uses
	 */
	public int getSearchPort() {
		
		return this.searchPort;
	}
}