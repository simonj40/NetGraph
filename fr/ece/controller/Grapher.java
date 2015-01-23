/**
 * 
 */
package ece.controller;

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

	
	private String PATH = "temp";
	//list containing all the ip links since the first traceroute or the last reset
	private List<String> localIpList;

	
	public Grapher(){
		localIpList = new ArrayList<String>();
	}
	
	/**
	 * Go through the new ip link list in parameter and add them to the local ip link list if they don't exist in it
	 * @param list
	 * 		List containing the IP links of the new traceroute
	 */
	public void draw(List<String> list){


		//adds new ip link to local list
		for(String link : list){	
			if(!existInList(link)){
				//add to local list
				localIpList.add(link);
			}
		}
		System.out.println("list size..." + list);
		
		//update graph text file with new local ip links list
		UpdateFile();
		
	}
	
	/**
	 * replace the existing graph text file by a new file containing the last version of the local ip link list
	 */
	public void UpdateFile(){
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
	/**
	 * test if the ip link already exists in the local list
	 * @param link
	 * @return
	 * 		true if already exists
	 * 		false otherwise
	 */
	public boolean existInList(String link){
		
		for(String localLink : localIpList){
			if(localLink.equals(link)) return true;
		}
		return false;
		
	}
	
}
