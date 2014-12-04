package views;

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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupten.thecabpool.R;

import controllers.MainController;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	//declare buttons
	Button btnRegister;
	Button btnLogin;
	Button btnOffer;
	Button btnRequest;
	Button btnSettings;
	
	//declare editable textview
	static TextView lblMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
   
        //initialize textview
        lblMessage = (TextView) findViewById(R.id.lblMessage);
        
        //initialize buttons
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnOffer = (Button) findViewById(R.id.btnOffer);
        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        
        //initialize controller
        MainController btnController = new MainController(this);
        
        //connect controller to each button
        btnRegister.setOnClickListener(btnController);
        btnLogin.setOnClickListener(btnController);
        btnOffer.setOnClickListener(btnController);
        btnRequest.setOnClickListener(btnController);
        btnSettings.setOnClickListener(btnController);
        
        

       
    }
      
    






	public static void setMessage(String msg){
    	lblMessage.setText(msg);
    }
    
   
    
}
