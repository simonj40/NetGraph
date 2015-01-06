package fr.ece.controller;

import java.util.List;

import javax.swing.JFrame;


//JUNG import
public class Controller {

	private static String osName = System.getProperty("os.name").toLowerCase();
	private static String address = "google.fr";
	private static JFrame frame;

	
	
	
	public static void main(String[] args) {

		frame = new JFrame("Traceroute");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //draw();
	    //drawInitialize;
		
		Traceroute tr = new Traceroute(osName, address);
		tr.start();
		Traceroute tr2 = new Traceroute(osName, "facebook.com");
		tr2.start();
		
		
	}
	
	
	
	
	public static synchronized void draw(List<String> list){

		
		
		//write new ip links in file
		//add new ip to local list
		
		//call dot on file
		//refresh view
		
		
		
	}
	
}
