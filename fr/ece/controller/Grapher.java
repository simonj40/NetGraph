/**
 * 
 */
package fr.ece.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon
 *
 */
public class Grapher {

	
	private static String PATH = "temp";
	
	/**
	 * replace the existing graph text file by a new file containing the last version of the local ip link list
	 */
	public static void UpdateFile(List<String> localIpList){
		//delete the previous file if it exists
		File tempFile =  new File(PATH);
		tempFile.delete();
		//creates a new file
		File graphFile = new File(PATH);
		//open a stream to the file
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		
		try {
			fos = new FileOutputStream(graphFile);
			osw = new OutputStreamWriter(fos);
			//writes the file with the new links
			osw.write("digraph G {\n");
			System.out.println("loop content..." + localIpList.size());
			for(String link : localIpList){

				osw.write(link + ";\n");
			}
			osw.write("}");
			
			osw.close();
			fos.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
