/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paycall.examples;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.UUID;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;

import org.json.JSONObject;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Ido Kanner
 */
public class SimpleDialer {
	
	class SimpleDialerStruct {

		public String uniqueID;
		public int dialAt;
		public boolean fromCampaign;
		public String legA;
		public String cidA;
		public String dtmfA;
		public boolean tts;
		public String sound;
		public String gender;
		public int answerDelay;
		public int ringTimeout;
		public int maxRetries;
		public long retryWaitTime;

		/*
		 * do not call to a destination after a message was provided public
		 * String leg_b; public String cid_b; public String dtmf_b;
		 */
		public SimpleDialerStruct(String uniqueID) {
			this.uniqueID = uniqueID;
			this.dialAt = 0;
			this.fromCampaign = false;
			this.legA = "97237173333";
			this.cidA = "037173333";
			this.dtmfA = "W1";
			this.tts = true;
			this.sound = "Hello world";
			this.gender = "female";
			this.answerDelay = 2;
			this.ringTimeout = 15;
			this.maxRetries = 3;
			this.retryWaitTime = 120;
		}

		public String toJSON() {
			JSONObject myJSON = new JSONObject().put("uniqueID", this.uniqueID).put("dial_at", this.dialAt)
					.put("from_campaign", this.fromCampaign).put("lag_a", this.legA).put("cid_a", this.cidA)
					.put("dtmf_a", this.dtmfA).put("tts", this.tts).put("sound", this.sound).put("gender", this.gender)
					.put("answer_delay", this.answerDelay).put("ring_timeout", this.ringTimeout)
					.put("max_retries", this.maxRetries).put("retry_wait_time", this.retryWaitTime);

			return myJSON.toString();
		}
	}

	private final String BASIC_ADDRESS = "http://api.dialer.co.il/v2/calls/%s";
			
	public final String uniqueID;
	public SimpleDialerStruct struct;
	
	public SimpleDialer() {
        this.uniqueID = UUID.randomUUID().toString();
        this.struct = new SimpleDialerStruct(this.uniqueID);
    }

	/**
     *
     * @param username
     * @param password
     * @param action
     * @param data
   * @return 
     */
    public StatusLine postRequest(String username, String password, String action, String data) {
        
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("api.dialer.co.il", 80),
                new UsernamePasswordCredentials(username, password));
        
        CloseableHttpClient httpclient = HttpClients.custom()
        		.setDefaultCredentialsProvider(credsProvider)
        		.build();
        try {
        	HttpPost request = new HttpPost(String.format(BASIC_ADDRESS, action));
        	if (data != null && data.isEmpty() == false) {
        		request.addHeader("Content-Type", "application/json");
        		request.setEntity(new StringEntity(data));
        	}
        	
        	try (CloseableHttpResponse response = httpclient.execute(request)) {
                return response.getStatusLine();
            }
        } catch (IOException | ParseException e) {
        } finally {
        	try {
				httpclient.close();
			} catch (IOException e) {
				// do nothing
			}
        }

        return null;
    }
    
    public StatusLine startCampaign(String username, String password, String id) throws UnsupportedEncodingException {
      String encodedId = URLEncoder.encode(id,"UTF-8");
      return this.postRequest(username, password, "start_campaign/" + encodedId, null);
    }

}
