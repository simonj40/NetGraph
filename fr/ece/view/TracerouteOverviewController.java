/**
 *
 */
package fr.ece.view;

import fr.ece.controller.AddShutdownHook;
import fr.ece.model.Traceroute;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Traceroute traceroute;

    private String GRAPH_PATH = "temp.png";
    // OS name
    private static String osName = System.getProperty("os.name").toLowerCase();

    @FXML
    public void traceRoute() {
        String ip = ip_input.getText();
        System.out.println("test : " + ip);

        traceroute.newTraceroute(ip);
        System.out.println("test : " + ip);
        ip_input.clear();

    }

    @FXML
    public void initialize() {
        // create and launch the ShutDown hook
        AddShutdownHook asdh = new AddShutdownHook();
        asdh.attachShutDownHook();

        traceroute = new Traceroute(osName);
        traceroute.setListener(this);
    }

    public void updateGraph() {

        InputStream is;
        try {
            is = new FileInputStream(GRAPH_PATH);
            Image image = new Image(is);
            imageview.setImage(image);
            System.out.println("Mise à jour de l'image");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TracerouteOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
