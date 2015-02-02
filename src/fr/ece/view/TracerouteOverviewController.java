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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private CheckBox fakerouteBox;
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
        String input = ip_input.getText();
        boolean fakeroute = fakerouteBox.isSelected();
        
        try {
			InetAddress adress = InetAddress.getByName(input);
			String ip = extractIp(adress.toString());
			if(ip != null){
				traceroute.newTraceroute( ip, fakeroute );
			}else{
				throw new Exception("error with the address");
			}

		} catch (Exception e) {
			//display prompt
			showAlert(input);
		}

    }
    
    private void showAlert(String address){
    	
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Address");
		alert.setHeaderText(null);
		alert.setContentText("The address you entered is not valid !");
		alert.showAndWait();
    	
    }
    
    
    /**
     * extract from a String line of the traceroute result the corresponding
     * ip address
     *
     * @param line
     * @return the extracted line
     */
    private String extractIp(String line) {
        if (line != null) {
            String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
            Pattern pattern = Pattern.compile(IPADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            String ip = null;
            
            if (matcher.find()) {
                ip = matcher.group();  
            }
            return ip;
        } else {
            return null;
        }
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
    	
    	traceroute.resetTracerouter();
    	progression(0);
    	imageview.setImage(null);
    	
    }

    public void progression(double prog) {
        progress_bar.setProgress(prog);

    }
    
    @FXML
    public void focusOn( ){
    	imageview.setScaleX(imageview.getScaleX()*1.2);
    	imageview.setScaleY(imageview.getScaleY()*1.2);
    	imageview.setX(0);
    	imageview.setY(0);
    }
    
    @FXML    
    public void focusOut( ){
    	
    	imageview.setScaleX(imageview.getScaleX()*0.8);
    	imageview.setScaleY(imageview.getScaleY()*0.8);
    }
    

    @FXML
    public void updateGraph() {

        InputStream is;
        try {
        	
            is = new FileInputStream(GRAPH_PATH);
            Image image = new Image(is);
            imageview.setImage(image);
            imageview.setFitHeight(scroll.getHeight());
            imageview.setFitWidth(scroll.getWidth());

        } catch (Exception ex) {
            Logger.getLogger(TracerouteOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
