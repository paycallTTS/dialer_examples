/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paycall.examples;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.StatusLine;

/**
 *
 * @author Ido Kanner
 */
public class TTSExamples {
	public static final String USERNAME = "john";
	public static final String PASSWORD = "doe";
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     System.out.println("Executing simple dialer");
    	SimpleDialer simpleDialer = new SimpleDialer();
    	
     StatusLine result = null;
    	result = simpleDialer.postRequest(USERNAME, PASSWORD, "simple_dialer", simpleDialer.struct.toJSON());
     
     if (result == null || result.getStatusCode() != 200) {
      if (result != null) {
        System.err.printf("Unable to initiate simple campaign: %d\n%s\n", result.getStatusCode(), result.getReasonPhrase());         
       }
      return;
     }
     
     try {
       result = simpleDialer.startCampaign(USERNAME, PASSWORD, simpleDialer.uniqueID);
     } catch (UnsupportedEncodingException ex) {
       Logger.getLogger(TTSExamples.class.getName()).log(Level.SEVERE, null, ex);
     }
     if (result == null || result.getStatusCode() != 200) {
       if (result != null) {
        System.err.printf("Unable to initiate campaign: %d\n%s\n", result.getStatusCode(), result.getReasonPhrase());         
       }
       return;
     }
    }
    
}
