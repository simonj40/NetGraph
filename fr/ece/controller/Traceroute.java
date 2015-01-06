package fr.ece.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Traceroute extends Thread {

	
	private static String osName;
	private static String address;

	public Traceroute(String osName, String address){
		this.osName = osName;
		this.address = address;

	}
	
	public void run() {
		/**
		 * test the OS and execute the correpsonding command
		 */
		if( this.osName.indexOf("win") >= 0 ){
			System.out.println("This is Windows");
			tracer("tracert " + this.address);
		}else{
			System.out.println("This is not Windows");
			tracer("traceroute -n " + this.address);
		}
		
	}

	/**
	 * execute the traceroute command, retrieve its result and treats it to get a list of ip links
	 * 
	 * @param command
	 */
	private void tracer(String command){
		String s = null;
		List<String> ipList = new ArrayList<String>();
		List<String> linkList = new ArrayList<String>();
		
		try {
			//executes the tracereoute command
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			// reads the result of the command line per line
			while ((s = input.readLine()) != null) {
				//displays the line
				System.out.println(s);
				//extracts the ip address of the line and stores it in the ip list
				String ip = extractIp(s);
				if(ip!=null) ipList.add(ip);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		//fills the ip links list with the proper links according to the ip list extracted from the traceroute result
		for(int i=0;i<ipList.size()-1;i++){
			String link = "\""+ ipList.get(i) + "\" -> \"" + ipList.get(i+1) + "\"";
			linkList.add(link);
		}
		
		//final call of the thread, to treats the ip link list and add it to the displayed graph
		Controller.drawTraceroute(linkList);
	}
	
	/**
	 * extract from a String line of the traceroute result the corresponding ip address
	 * @param line
	 * @return the extracted line
	 */
	private String extractIp(String line){
		
		List<String> list = new ArrayList<String>();
		//split the line according to the delimiter " " (space) 
		String[] str = line.split("\\s+");

		for(int i=0;i<str.length;i++){
			//remove all the spaces from the splited part
			String temp = str[i].replace("\\s+", "");
			//test if its not empty and add it to a list
			if(!temp.isEmpty()) list.add(temp);
		}
		
		// test if the position corresponding to the ip address fits the ip format and retrieve return the ip address
		if(list.get(1).length()>6) return list.get(1);
		return null;
		
	}
	
	
}
