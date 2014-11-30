package AsyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import views.RequestScreen;

import controllers.SecurityController;
import controllers.ShareController;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
			if(fromClass.equals("AutoComplete")) getSearchLocation(nameValuePairs.get(0).toString());
			
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
				if(fromClass.equals("AutoComplete")){
					String[] location = message.split(" ");
					double[] pos = {Double.parseDouble(location[0]), Double.parseDouble(location[1])};
					String[] stringData = nameValuePairs.get(0).toString().split(" ");
					RequestScreen.setMarker(new LatLng(pos[0], pos[1]), stringData[0].replace("location=", "").replace(",", " "));
				}
				else ShareController.httpResponse(message);
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
		
		
		public void getSearchLocation(String itemAtPosition) {
			double[] locationArray = new double[2];
			 ArrayList<String> resultList = null;

			    HttpURLConnection conn = null;
			    StringBuilder jsonResults = new StringBuilder();
			    try {
			        StringBuilder sb = new StringBuilder();
			        sb.append("http://maps.google.com/maps/api/geocode/json?");
			        sb.append("address=" + URLEncoder.encode(itemAtPosition, "utf8"));

			        
			        Log.d("Address", sb.toString());		 

			        URL url = new URL(sb.toString());
			        conn = (HttpURLConnection) url.openConnection();
			        InputStreamReader in = new InputStreamReader(conn.getInputStream());

			        // Load the results into a StringBuilder
			        int read;
			        char[] buff = new char[1024];
			        while ((read = in.read(buff)) != -1) {
			            jsonResults.append(buff, 0, read);
			        }
			    } catch (MalformedURLException e) {
			       
			        
			    } catch (IOException e) {
			       
			      
			    } finally {
			        if (conn != null) {
			            conn.disconnect();
			        }
			    }

			    try {
			        // Create a JSON object hierarchy from the results
			        JSONObject jsonObj = new JSONObject(jsonResults.toString());
			        Log.d("jsonObj", jsonObj.toString());
			        JSONArray results = jsonObj.getJSONArray("results");
			        Log.d("results", results.toString());
			        JSONObject resultsData = results.getJSONObject(0); //resultsData
			        Log.d("resultsData", resultsData.toString());
			        JSONObject geometry = resultsData.getJSONObject("geometry");
			        Log.d("geometry", geometry.toString());
			        JSONObject location = geometry.getJSONObject("location");
			        String lat = location.getString("lat");
			        String lng = location.getString("lng");
			        locationArray[0] = Double.parseDouble(lat);
			        locationArray[1] = Double.parseDouble(lng);
			        
			        

			    } catch (JSONException e) {
			        
			    }
			 message = locationArray[0] +" "+locationArray[1];
			 Log.d("LAT:", ""+locationArray[0]);
			 Log.d("LONG:", ""+locationArray[1]);
		}
		
    	
    }