/**
 *
 */
package fr.ece.view;

import fr.ece.controller.AddShutdownHook;
import fr.ece.model.Traceroute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

/**
 * @author Simon
 *
 */
public class TracerouteOverviewController {

    @FXML
    private Button traceroute_button;
    @FXML
    private Button reset;
    @FXML
    private TextField ip_input;
    @FXML
    private ProgressBar progress_bar;
    @FXML
    private ImageView imageview;
    
    @FXML 
    private ScrollPane scroll;

    private Traceroute traceroute;

    public String GRAPH_PATH = "temp.png";
    // OS name
    private static String osName = System.getProperty("os.name").toLowerCase();

    @FXML
    public void traceRoute() {
        String ip = ip_input.getText();
        System.out.println("test : " + ip);

        traceroute.newTraceroute(ip);

    }

    @FXML
    public void initialize() {
        // create and launch the ShutDown hook
        progress_bar.setProgress(0);

        AddShutdownHook asdh = new AddShutdownHook();
        asdh.attachShutDownHook();

        traceroute = new Traceroute(osName);
        traceroute.setListener(this);
    }

    public void reset() {
        progress_bar.setProgress(0);
        ip_input.clear();
        InputStream clear;
        imageview.setImage(null);
        System.out.println("Reset");

    }

    public void progression(double prog) {
        progress_bar.setProgress(prog);

    }
    @FXML
    public void focusOn( ){
    	imageview.setScaleX(imageview.getScaleX()*1.1);
    	imageview.setScaleY(imageview.getScaleY()*1.1);
    }
    @FXML    
    public void focusOut( ){
    	imageview.setScaleX(imageview.getScaleX()*0.9);
    	imageview.setScaleY(imageview.getScaleY()*0.9);
    }
    

    @FXML
    public void updateGraph() {

        InputStream is;
        try {
        	
            is = new FileInputStream(GRAPH_PATH);
            Image image = new Image(is);
            imageview.setImage(image);
            progress_bar.setProgress(1);
            System.out.println("Mise à jour de l'image");


        } catch (Exception ex) {
            Logger.getLogger(TracerouteOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
