package ece.controller;

import java.util.List;
import ece.view.MainWindow;


public class Controller {
	
	// OS name
	private static String osName = System.getProperty("os.name").toLowerCase();
	
	private static MainWindow window;
	private static Grapher grapher;
	
	public static void main(String[] args) {
		// create and launch the ShutDown hook
		AddShutdownHook asdh = new AddShutdownHook();
		asdh.attachShutDownHook();
		
		grapher = new Grapher();
		
		window = new MainWindow();

	}
	
	/**
	 * method called by the class traceroute once the process is done
	 * Treat the new traceroute IP link list
	 * @param list
	 */
	public static synchronized void drawTraceroute(List<String> list){
		
		//add new ip link to the local ip list and generate the new graph file
		grapher.draw(list);
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
