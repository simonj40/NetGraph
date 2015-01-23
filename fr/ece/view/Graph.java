/**
 * 
 */
package ece.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Panel containing the graph
 * @author Simon
 *
 */
public class Graph extends JPanel{

    public Graph () {
    	//set background color to white
    	this.setBackground(Color.WHITE);
    }

    protected void paintComponent(Graphics g) {
    	//Open a stream to the image
    	Image image=null;
    	try {

            image = ImageIO.read( new File("temp.png") );
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        } catch (Exception e) {

        }

        
    }
}
