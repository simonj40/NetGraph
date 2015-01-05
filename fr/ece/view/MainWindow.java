package fr.ece.view;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public MainWindow(){
		
		this.setTitle("Ma première fenêtre Java");
	    this.setSize(400, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
		
	}
	 
	

}
