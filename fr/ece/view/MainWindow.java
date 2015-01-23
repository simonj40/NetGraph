package fr.ece.view;
import fr.ece.controller.Controller;
import fr.ece.model.Traceroute;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



/**
 * main window of the program
 * @author Simon
 *
 */
public class MainWindow extends JFrame implements ActionListener {
	
	private JButton button; 
	private Graph graph;
	private JTextField ip;

	private String osName = System.getProperty("os.name").toLowerCase();
	Traceroute traceroute;

	public MainWindow(Traceroute traceroute){
		this.traceroute =  traceroute;
		
		//set window properties
		this.setTitle("Traceroute");
	    this.setMinimumSize(new Dimension(500,500));
	    this.setLocationRelativeTo(null);
	    this.setLayout(new BorderLayout());
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //define button and text field
	    button =  new JButton("Trace");
	    ip =  new JTextField(20);
	    //add action listener to the button
	    button.addActionListener(this);
	   
	    // creates a new panel for the text field and the button
	    JPanel menu = new JPanel();
	    // add the elements to the panel
	    menu.setLayout(new BorderLayout());
	    menu.add(ip, BorderLayout.WEST);
	    menu.add(button,BorderLayout.EAST);
	    
	    //add the panel to the window
	    this.getContentPane().add(menu, BorderLayout.NORTH);
	    // new graph panel
	    graph = new Graph();
	    // add the graph panel to the window
	    getContentPane().add(graph, BorderLayout.CENTER);
	    // mke the window visible
	    this.setVisible(true);

	}
	
	/**
	 * reload the image of the panel displaying the graph
	 */
	public void loadGraph(){
		
		graph.repaint();
		graph.validate();

		
	}

	/**
	 * button's action listener
	 * start a traceroute to the address specified in the text field when pressed
	 */
	public void actionPerformed(ActionEvent e) {
		//retrieve the content of the text field
		String address = ip.getText();
		//launch a new traceroute 
		traceroute.newTraceroute(address);
	}
	 
	

}
