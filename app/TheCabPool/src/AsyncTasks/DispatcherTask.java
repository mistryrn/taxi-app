package AsyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import library.MCrypt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import views.LoginScreen;
import views.OfferScreen;
import views.RequestScreen;

import android.os.AsyncTask;
import android.util.Log;

import controllers.SecurityController;
import controllers.SettingsController;
import controllers.ShareController;

public class DispatcherTask extends AsyncTask<String, Void, String> {
	    private String message;
	    private List nameValuePairs;
	    private String fromClass;
	    	    
	    public DispatcherTask(String fromClass, List nameValuePairs){
	    	this.nameValuePairs = nameValuePairs;
	    	this.fromClass = fromClass;
	    }
	    
	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			message="";
			encrypt();
		}
	    
		private void encrypt() {

			List<NameValuePair> nameValuePairsEncrypted = new ArrayList<NameValuePair>(2);
			//insert encryption code here (must encrypt all nameValuePairs)
			for(int i = 0; i<nameValuePairs.size(); i++){
				String nameValue = nameValuePairs.get(i).toString();
				String[] nameValueArray = nameValue.split("=");
				String value = nameValueArray[1];
				StringBuilder newValue = new StringBuilder();
				MCrypt mcrypt = new MCrypt();
				String encrypted = "";
				/* Encrypt */
				try {
					encrypted = MCrypt.bytesToHex( mcrypt.encrypt(value) );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				value = encrypted;
				Log.d("nameValuePairs", value);
				BasicNameValuePair encryptedPair = new BasicNameValuePair(nameValueArray[0], value);
				nameValuePairsEncrypted.add(encryptedPair);
			}
			nameValuePairs = nameValuePairsEncrypted;
		
		}

		@Override
		protected String doInBackground(String... params) {
			if(fromClass.equals("AutoCompleteRequest") || fromClass.equals("AutoCompleteOffer"))  getSearchLocation(nameValuePairs.get(0).toString());
			else postData();
			return null;
		}
		
		private void decrypt(){
			MCrypt mcrypt = new MCrypt();
			/* Decrypt */
			try {
				message = new String( mcrypt.decrypt( message ) );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			decrypt();
			
			try {
				if(fromClass.equals("AutoCompleteRequest") || fromClass.equals("AutoCompleteOffer")){
					String[] location = message.split(" ");
					double[] pos = {Double.parseDouble(location[0]), Double.parseDouble(location[1])};
					String[] stringData = nameValuePairs.get(0).toString().split(" ");
					if(fromClass.equals("AutoCompleteRequest")) RequestScreen.setMarker(new LatLng(pos[0], pos[1]), stringData[0].replace("location=", "").replace(",", " "));
					if(fromClass.equals("AutoCompleteOffer")) OfferScreen.setMarker(new LatLng(pos[0], pos[1]), stringData[0].replace("location=", "").replace(",", " "));
				}
				else{
					if(fromClass.equals("Security")) SecurityController.httpResponse(message);
					else if(fromClass.equals("Share")) ShareController.httpResponse(message);
					else if(fromClass.equals("Settings")) SettingsController.httpResponse(message);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}
		
		public void postData() {
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

		        message = sb.toString();
	            
		        
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