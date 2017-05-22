/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser;


import java.io.*;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tyaa.vkparser.modules.facades.ComradFinder;
import org.tyaa.vkparser.modules.facades.ModelBuilder;

/**
 *
 * @author Юрий
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    
    private static volatile boolean cmLoading = false;
    
    public static void main(String[] args)
    {
        //buildModel();
        //findByModel();
        BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));
        out.println("Comrad finder console v1.00");
        
        out.println();
        out.print("Enter group id: ");
        String groupIdString = "";
        try {
            groupIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        cmLoading = true;
        try {
            loading("Model creating...");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        ModelBuilder.buildModel(groupIdString);
        cmLoading = false;
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Enter country id: ");
        String countryIdString = "";
        try {
            countryIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        out.print("Enter city id: ");
        String cityIdString = "";
        try {
            cityIdString = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        
        cmLoading = true;
        try {
            loading("Searching...");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        ComradFinder.findByModel(countryIdString, cityIdString);
        cmLoading = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println();
        out.println("Complete");
        
        try {
            bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static synchronized void loading(String msg) throws IOException, InterruptedException {
        
        System.out.println(msg);
        
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.write("\r|".getBytes());
                    while(cmLoading) {
                        System.out.write("-".getBytes());
                        Thread.sleep(1000);
                    }
                    System.out.write("| Done \r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }
}
