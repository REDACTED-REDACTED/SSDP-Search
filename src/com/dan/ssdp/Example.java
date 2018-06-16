package com.dan.ssdp;

import java.io.IOException;
import java.util.ArrayList;

public class Example {
	
	static ArrayList<String> responses;
	
	public static void main(String[] args) {
		
		for(int i = 0; i<5;i++) {
			search();
		}
	}
	
	public static void search() {
		
		//Send Search and Receive Responses
		int mx = 2;
		String st = "ssdp:all";
		String request = SSDPSearch.makeRequest(mx, st);
		try {
			
			//Sends request and takes time stamp
			SSDPSearch.sendData(request);
			long startTime = System.currentTimeMillis();
			long endTime = startTime + 2000;
			long currentTime = startTime;
			responses = new ArrayList<>();
			
			//While Loops Runs for 2 seconds (MX value)
			while (currentTime < endTime) {
					
				try {
					//The listener will listen for 1 second before timing out
					String response = SSDPSearch.recieveData(1000);
					responses.add(response);
				}catch(IOException e) {
					System.out.println(".");
				}
				//Updates the time value
				currentTime = System.currentTimeMillis();
			}
				
		} catch(IOException e) {
				
			//Error Occurred
			e.printStackTrace();
		}
			
		//Parse Responses
		for(String response:responses) {
			SSDPResponse res = new SSDPResponse(response);
			
			//Response header values can be accessed with getters from the SSDPResponse class
			//This method is best if you are getting a header that is required in the response
			System.out.println(res.getServer());
			
			//This is another way to access header values.
			//The headers map will contain all the headers of the response
			//This method is best if you need to parse the value yourself 
			//or if the header isn't defined as a getter in the SSDPResponse class
			System.out.println(res.headers.get("location"));
		}
	}
}
