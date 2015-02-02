package fr.ece.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ece.controller.Dot;
import fr.ece.view.TracerouteOverviewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class Traceroute {

    private String osName;
    private String PATH = "temp";

    private TracerouteOverviewController listener;
    
   

    //list containing all the ip links since the first traceroute or the last reset
    private List<String> localIpList;
    
    private List<Tracerouter> tracerouterList;

    public Traceroute(String osName) {
        this.osName = osName;
        localIpList = new ArrayList<String>();
        tracerouterList= new ArrayList<Tracerouter>();
    }

    public void resetTracerouter(){
    	//Cancel all current runing traceroute
    	for(Tracerouter tr : tracerouterList){
    		tr.cancel();
    	}
    	//empty tracerouteList
    	tracerouterList.removeAll(tracerouterList);
    	
    	//empty localIpList
    	localIpList.removeAll(localIpList);
    }
    
    
    /**
     * @return the localIpList
     */
    public List<String> getLocalIpList() {
        return localIpList;
    }

    public void setListener(TracerouteOverviewController listener) {
        this.listener = listener;
    }

    public void newTraceroute(String address) {

        Tracerouter tracerouter = new Tracerouter(address);
        tracerouterList.add(tracerouter);
        //set listener anc action on traceroute finishing
        tracerouter.messageProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable,
                String oldValue, String newValue) {
            	//TODO action when traceroute thread over
                listener.updateGraph();
            }

        });
        

        //set listener and action on progress update
        tracerouter.progressProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable,
					Object oldValue, Object newValue) {
				double progress = 0;
				
				for(Tracerouter tr : tracerouterList){
					progress += tr.progressProperty().doubleValue();
					//System.out.println(tr.progressProperty().doubleValue());
				}
				
				progress = progress/(double)tracerouterList.size();
				//System.out.println("TOTAL : " + progress);
				listener.progression(progress);
			}

        });
        

        (new Thread(tracerouter)).start();

    }

    public void updateFile(List<String> localIpList) {

        //delete the previous file if it exists
        File tempFile = new File(PATH);
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
            //System.out.println("loop content..." + localIpList.size());
            for (String link : localIpList) {
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
     * @param linkList
     */
    private void updateIpList(List<String> linkList) {

        //adds new ip link to local list
        for (String link : linkList) {
            if (!existInList(link)) {
                //add to local list
                localIpList.add(link);
            }
        }

    }
    
    /**
     * test if the ip link already exists in the local list
     *
     * @param link
     * @return true if already exists false otherwise
     */
    public boolean existInList(String link) {

        for (String localLink : localIpList) {
            if (localLink.equals(link)) {
                return true;
            }
        }
        return false;

    }

    public boolean imageIsReady(){
    	try {
			Image image = new Image(listener.GRAPH_PATH);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    
    /**
     * method called by the class traceroute once the process is done Treat the
     * new traceroute IP link list
     */
    public synchronized void drawTraceroute() {

        //add new ip link to the local ip list and generate the new graph file
        updateFile(localIpList);
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

    }

    //TODO add listener on tracerouter that trigger when tracerouter thread is done and lauch proper treatement 
    public class Tracerouter extends Task<Object> {

        private String address;
        private long progressMax = 6;

        private String FAKE_TRACEROUTE = "java -jar fakeroute.jar ";

        public Tracerouter(String address) {
            this.address = address;
        }

        @Override
        protected Object call() throws Exception {
            /**
             * test the OS and execute the correpsonding command
             */
        	this.updateProgress(1, progressMax);
            if (osName.indexOf("win") >= 0) {
                //System.out.println("This is Windows");
                //tracer("tracert " + this.address);
                tracer(FAKE_TRACEROUTE + this.address);
            } else {
                //System.out.println("This is not Windows");
                //tracer("traceroute -n " + this.address);
                tracer(FAKE_TRACEROUTE + this.address);
            }

            //final call of the thread, to treats the ip link list and add it to the displayed graph
            //System.out.println("list size..." + linkList);
            drawTraceroute();
            this.updateProgress(6, progressMax);
            updateMessage("Traceroute to " + this.address + "is done");
            tracerouterList.remove(this);
            return null;
        }

        /**
         * execute the traceroute command, retrieve its result and treats it to
         * get a list of ip links
         *
         * @param command
         */
        private void tracer(String command) {
            String s = null;
            List<String> ipList = new ArrayList<String>();
            List<String> linkList = new ArrayList<String>();
            long progress = 1;

            try {
                //executes the tracereoute command
                Process p = Runtime.getRuntime().exec(command);

                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

                BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                // reads the result of the command line per line
                while ((s = input.readLine()) != null) {
                    //extracts the ip address of the line and stores it in the ip list
                    //System.out.println(s);
                    String ip = extractIp(s);
                    if (ip != null) {
                    	
                        ipList.add(ip);
                        progress += 1/50;
                        this.updateProgress(1 + progress, progressMax);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //fills the ip links list with the proper links according to the ip list extracted from the traceroute result
            for (int i = 0; i < ipList.size() - 1; i++) {
                String link = "\"" + ipList.get(i) + "\" -> \"" + ipList.get(i + 1) + "\"";
                linkList.add(link);
            }
            
            this.updateProgress(3, progressMax);
            
            updateIpList(linkList);
            this.updateProgress(4, progressMax);
            //System.out.println(localIpList.size());

            
        }

        /**
         * extract from a String line of the traceroute result the corresponding
         * ip address
         *
         * @param line
         * @return the extracted line
         */
        private String extractIp(String line) {
            //System.out.println(line);

            if (line != null) {
                String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
                
                
                Pattern pattern = Pattern.compile(IPADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                String ip = "";
                while (matcher.find()) {
                    ip += matcher.group() + "\n";  
                }
                //System.out.println("IP LIGNE : " + ip);
                return ip;
            } else {
                return null;
            }
        }

    }

}
