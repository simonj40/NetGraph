/**
 * 
 */
package fr.ece.controller;

import java.io.File;
import java.io.IOException;

/**
 * Thread executing the dot program
 * @author Simon
 *
 */
public class Dot extends Thread {
	//link to the dot program according to the OS
	private String MAC_DOT = "/usr/local/bin/dot -Tpng temp -o temp.png";
	private String WIN_DOT = "c:\\Program Files\\Graphviz*\\dot.exe -Tpng temp -o temp.png";
	// private String WIN_DOT = "c:\\Program Files (x86)\\Graphviz*\\dot.exe";
	private String LINUX_DOT = "/usr/local/bin/dot -Tpng temp -o temp.png";
	private String osName;
	
	public Dot (String osName){
		this.osName = osName;
	}
	
	
	public void run() {
		//test the OS and execute the corresponding command
		if( this.osName.indexOf("win") >= 0 ){
			execDot(WIN_DOT);
		}else if(this.osName.indexOf("mac") >= 0){	
			execDot(MAC_DOT);
		}else{
			execDot(LINUX_DOT);
		}
		
		//For the file created by dot to be fully generated and accessible 
		boolean wait=true;
		while(wait){
			File f = new File("temp.png");
			if(f.exists()) wait = false;
		}
		
		
		
	}
	
	/**
	 * execute the command for dot program to be launch
	 * @param command
	 */
	public void execDot(String command){
		
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
