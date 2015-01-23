package fr.ece.controller;

import java.util.List;

import fr.ece.model.Traceroute;
import fr.ece.view.MainWindow;


public class Controller {
	
	// OS name
	private static String osName = System.getProperty("os.name").toLowerCase();
	
	private static MainWindow window;
	private static Grapher grapher;
	private static Traceroute traceroute;
	
	public static void main(String[] args) {
		// create and launch the ShutDown hook
		AddShutdownHook asdh = new AddShutdownHook();
		asdh.attachShutDownHook();
		
		traceroute = new Traceroute(osName);
		
		window = new MainWindow(traceroute);

	}
	
	/**
	 * method called by the class traceroute once the process is done
	 * Treat the new traceroute IP link list
	 * @param list
	 */
	public static synchronized void drawTraceroute(){
		
		//add new ip link to the local ip list and generate the new graph file
		Grapher.UpdateFile(traceroute.getLocalIpList());
		//call dot program on the graph file
		Dot dot = new Dot(osName);
		dot.start();
		
		try {
			//wait for dot process to be done
			dot.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Update the view
		window.loadGraph();
		
	}


	
	
	
	
	
	
	
	
	
}
