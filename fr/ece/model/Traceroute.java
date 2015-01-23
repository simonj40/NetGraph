package fr.ece.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ece.controller.Controller;

public class Traceroute {

	private static String osName;
	
	//list containing all the ip links since the first traceroute or the last reset
		private List<String> localIpList;

	public Traceroute(String osName){
		this.osName = osName;
		localIpList = new ArrayList<String>();

	}

	
	/**
	 * @return the localIpList
	 */
	public List<String> getLocalIpList() {
		return localIpList;
	}


	

	public void newTraceroute(String address){
		
		Tracerouter tr = new Tracerouter(osName,address );
		tr.run();
	}
	
	
	
	
	
	//TODO add listener on tracerouter that trigger when tracerouter thread is done and lauch proper treatement 
	
	public class Tracerouter extends Thread{
		
		private String osName;
		private String address;
		
		private String FAKE_TRACEROUTE = "java -jar fakeroute.jar ";
		
		
		public Tracerouter(String osName, String address){
			this.osName = osName;
			this.address = address;
		}
		
		public void run() {
			/**
			 * test the OS and execute the correpsonding command
			 */
			if( this.osName.indexOf("win") >= 0 ){
				System.out.println("This is Windows");
				//tracer("tracert " + this.address);
				tracer(FAKE_TRACEROUTE + this.address);
			}else{
				System.out.println("This is not Windows");
				//tracer("traceroute -n " + this.address);
				tracer(FAKE_TRACEROUTE + this.address);
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
			
			updateIpList(linkList);
			
			System.out.println(localIpList.size());
			
			//final call of the thread, to treats the ip link list and add it to the displayed graph
			//System.out.println("list size..." + linkList);
			Controller.drawTraceroute();
		}
		
		/**
		 * extract from a String line of the traceroute result the corresponding ip address
		 * @param line
		 * @return the extracted line
		 */
		private String extractIp(String line){
			
			System.out.println(line);
			
			if (line != null) {
				String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
				Pattern pattern = Pattern.compile(IPADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(line);
				String ip = null;
				while (matcher.find()) {
					ip = matcher.group() + "\n";
					System.out.println(ip);
				}
				
				return ip;
			}else{
				return null;
			}
		}

		/**
		 * @param linkList
		 */
		private void updateIpList(List<String> linkList) {
			//adds new ip link to local list
			for(String link : linkList){	
				if(!existInList(link)){
					//add to local list
					localIpList.add(link);
				}
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
	
}
