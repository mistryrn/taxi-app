package AsyncTasks;

import java.io.BufferedReader;
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

import controllers.SecurityController;

import android.os.AsyncTask;

public class SecurityTask extends AsyncTask<String, Void, String> {
    private int responseCode;
    private String message;
    private String fromClass;
    private List nameValuePairs;
    
    public SecurityTask(String fromClass, List nameValuePairs){
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
		if(fromClass.equals("Register")) postRegisterData();
		if(fromClass.equals("Login")) postLoginData();
		if(fromClass.equals("ScanCode")) postScanCodeData();
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		SecurityController.httpResponse(message);	
	}
	
	private void postScanCodeData() {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://arche.comeze.com/cabpool/dispatcher.php");
	    
	    try {
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity responseEntity = response.getEntity();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(),"UTF-8"));
	        responseCode = response.getStatusLine().getStatusCode(); 
	        if(responseCode==200){
				message="Scan Successful";
	        }
	        else{
	        	message="Error: Scan Unsuccessful";
	        }        
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }
	}
	
	private void postLoginData() {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://arche.comeze.com/cabpool/dispatcher.php");
	    
	    try {
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity responseEntity = response.getEntity();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(),"UTF-8"));
	        responseCode = response.getStatusLine().getStatusCode(); 
	        if(responseCode==200){
				message="Login Successful";
	        }
	        else{
	        	message="Error: Login Unsuccessful";
	        }
	       
	        
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }
	} 
    	
	
	
	public void postRegisterData() {
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
				message="Registration Successful";
	        }
	        else{
	        	message="Error: Username taken";
	        }
            
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	} 
    	
    }