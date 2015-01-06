package fr.ece.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
			tracer("traceroute -n " + this.address);
		}
		
		
		
	}

	private void tracer(String command){
		String s = null;
		List<String> list = new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while ((s = input.readLine()) != null) {
				System.out.println(s);
				String ip = extractIp(s);
				if(ip!=null) list.add(ip);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Controller.draw(list);
	}
	
	private String extractIp(String line){
		
		List<String> list = new ArrayList<String>();
		
		String[] str = line.split("\\s+");
		
		for(int i=0;i<str.length;i++){
			String temp = str[i].replace("\\s+", "");
			//System.out.print(temp+",");
			if(!temp.isEmpty()) list.add(temp);
		}
		
		if(list.get(1).length()>6) return list.get(1);
		return null;
		
	}
	
	
}
