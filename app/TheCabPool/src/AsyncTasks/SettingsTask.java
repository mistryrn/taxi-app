package AsyncTasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

class SettingsTask extends AsyncTask<String, Void, String> {
    String message;
    
    @Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
    
	@Override
	protected String doInBackground(String... params) {
		InputStream inputStream = null;
		String URL = params[0];
	    try {
	        // create HttpClient
	        HttpClient httpclient = new DefaultHttpClient();
	        // make GET request to the given URL
	        HttpResponse httpResponse = httpclient.execute(new HttpGet(URL));
	        // receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();

	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

	        message=in.readLine();
	    }
	    catch(Exception e){
	    	message = e.toString();
	    }
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
    	
    }