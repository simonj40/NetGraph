package fr.ece.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Traceroute extends Thread {

	
	String traceroute ="";
	
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public void run() {
		
		String s = null;
		
		
		
		if( OS.indexOf("win") >= 0 ){
			System.out.println("This is Windows");
		}else{
			System.out.println("This is not Windows");
			
			try {
				Process p = Runtime.getRuntime().exec("traceroute google.fr");
				
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		 
		        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				
		        while ((s = input.readLine()) != null) {
		        	System.out.println(s);
		        	traceroute += s;
		        }
		        System.out.println("RESPONSE");
		        System.out.println(traceroute);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
}
