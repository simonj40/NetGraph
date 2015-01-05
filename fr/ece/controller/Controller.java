package fr.ece.controller;

import java.io.*;

public class Controller {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	
	public static void main(String[] args) {
		

			Traceroute tr = new Traceroute();
			tr.start();
		
		
	}
	
	
	
}
