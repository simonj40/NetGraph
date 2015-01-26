/**
 *
 */
package fr.ece.controller;

import java.io.File;

/**
 * @author Simon
 *
 */
public class AddShutdownHook {

    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            //Code to be executed just before the program quit
            public void run() {
                //delete image and text files containing the graph description
                try {
                    File file = new File("temp");
                    File file2 = new File("temp.png");
                    file.delete();
                    file2.delete();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
