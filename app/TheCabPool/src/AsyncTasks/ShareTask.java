package AsyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import controllers.SecurityController;
import controllers.ShareController;

import android.os.AsyncTask;
import android.util.Log;

public class ShareTask extends AsyncTask<String, Void, String> {
		private int responseCode;
	    private String message;
	    private String fromClass;
	    private List nameValuePairs;
	    
	    public ShareTask(String fromClass, List nameValuePairs){
	    	this.fromClass = fromClass;
	    	this.nameValuePairs = nameValuePairs;
	    }
	    
	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			message="";
			encrypt();
		}
	    
		private void encrypt() {
			//insert encryption code here (must encrypt all nameValuePairs)
		}

		@Override
		protected String doInBackground(String... params) {
			if(fromClass.equals("Request")) postRequestData();
			if(fromClass.equals("Offer")) postOfferData();
			if(fromClass.equals("Update")) postUpdateOfferData();
			
			return null;
		}

		private void postUpdateOfferData() {
			 // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://arche.comeze.com/cabpool/dispatcher.php");
		    
		    try {
		        // Add your data
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity responseEntity = response.getEntity();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(),"UTF-8"));
		        responseCode = response.getStatusLine().getStatusCode();
		        
		        if(responseCode==200){
					message="Update Successful";
		        }
		        else{
		        	message="Error: Update failed";
		        }
	            
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
			
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				ShareController.httpResponse(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		public void postOfferData() {
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://arche.comeze.com/cabpool/dispatcher.php");
		    
		    try {
		        // Add your data
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity responseEntity = response.getEntity();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(),"UTF-8"));
		        responseCode = response.getStatusLine().getStatusCode();
		        
		        if(responseCode==200){
					message="Offer Successful";
		        }
		        else{
		        	message="Error: Offer failed";
		        }
	            
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		} 

		public void postRequestData() {
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://arche.comeze.com/cabpool/dispatcher.php");
		    
		    try {
		        // Add your data
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity responseEntity = response.getEntity();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(),"UTF-8"));
		        StringBuffer sb = new StringBuffer("");
		        String line = "";
		        while ((line = reader.readLine()) != null) {
		        	if(line.length()!=0 && line.charAt(0)=='<') break;
		            sb.append(line + "\n");
		        }
		
		        responseCode = response.getStatusLine().getStatusCode();
		        
		        if(responseCode==200){
		        	message = sb.toString();
		        }
		        else{
		        	message="Error: Offers not found";
		        }
	            
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		} 
    	
    }