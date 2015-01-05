package fr.ece.controller;

import java.io.*;

public class Controller {

	private static String osName = System.getProperty("os.name").toLowerCase();
	private static String address = "google.fr";

	
	public static void main(String[] args) {

			Traceroute tr = new Traceroute(osName, address);
			tr.start();
		
	}
	
	
	
}
