package ece.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Traceroute extends Thread {

	
	String traceroute ="";
	
	
	private static String osName;
	private static String address;

	/*
	Constructor for Traceroute class
	 */
	Traceroute(String osName, String address){
		this.osName = osName;
		this.address = address;

	}
	
	public void run() {
		
		if( this.osName.indexOf("win") >= 0 ){
			System.out.println("This is Windows");
			tracer("tracert " + this.address);
		}else{
			System.out.println("This is not Windows");
			tracer("traceroute " + this.address);
		}
		
		
	}

	private void tracer(String command){
		String s = null;
		try {
			Process p = Runtime.getRuntime().exec(command);

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
