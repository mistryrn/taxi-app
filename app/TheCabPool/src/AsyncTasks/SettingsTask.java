package AsyncTasks;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import controllers.SecurityController;
import controllers.SettingsController;

import android.os.AsyncTask;
import android.util.Log;

public class SettingsTask extends AsyncTask<String, Void, String> {
    private int responseCode;
    private String message;
    private String fromClass;
    private List nameValuePairs;
    
    public SettingsTask(List nameValuePairs){
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
		postData();	
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		SettingsController.httpResponse(message);	
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
	        responseCode = response.getStatusLine().getStatusCode();
	        StringBuffer sb = new StringBuffer("");
	        String line = "";
	        while ((line = reader.readLine()) != null) {
	        	if(line.length()!=0 && line.charAt(0)=='<') break;
	            sb.append(line + "\n");
	        }

	        message= sb.toString();
            
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	} 
    	
    }