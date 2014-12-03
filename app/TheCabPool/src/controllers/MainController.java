package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import views.LoginScreen;
import views.MainMenu;
import views.OfferListScreen;
import views.OfferScreen;
import views.RegisterScreen;
import views.RequestListScreen;
import views.RequestScreen;
import views.ScanCodeScreen;
import views.SettingsScreen;

import com.groupten.thecabpool.R;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class MainController extends MainMenu implements View.OnClickListener {
	private static Context mainContext;
	public MainController(Context context){
		MainController.mainContext=context;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnRegister:
			registerClicked();
			break;
			
		case R.id.btnLogin:
			loginClicked();
			break;	

		case R.id.btnOffer:
			offerClicked();
			break;	
			
		case R.id.btnRequest:
			requestClicked();
			break;	
			
		case R.id.btnSettings:
			settingsClicked();
			break;	

		default:
			
			break;	
		}
	}

	private void registerClicked() {
    	Intent intent = new Intent(mainContext, RegisterScreen.class);
    	mainContext.startActivity(intent);
	}
	
	private void loginClicked() {
		Intent intent = new Intent(mainContext, LoginScreen.class);
    	mainContext.startActivity(intent);
    	MainMenu.setMessage("");
	}
	
	private void offerClicked() {
		String username = LoginScreen.getLoginData()[0];
		if(username != null){
			if(!ScanCodeScreen.codeScanned()){
				Intent intent = new Intent(mainContext, ScanCodeScreen.class);
		    	mainContext.startActivity(intent);
			}
			else{
				Intent intent = new Intent(mainContext, OfferScreen.class);
		    	mainContext.startActivity(intent);
			}
		}
		else{
			Toast toast = Toast.makeText(mainContext, "You must be logged in to place an offer." , Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	private void requestClicked() {
		String username = LoginScreen.getLoginData()[0];
		if(username != null){
		Intent intent = new Intent(mainContext, RequestScreen.class);
    	mainContext.startActivity(intent);
		}
		else{
			Toast toast = Toast.makeText(mainContext, "You must be logged in to request a taxi." , Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	private void settingsClicked() {
		String username = LoginScreen.getLoginData()[0];
		if(username != null){
		Intent intent = new Intent(mainContext, SettingsScreen.class);
    	mainContext.startActivity(intent);
		}
		else{
			Toast toast = Toast.makeText(mainContext, "You must be logged in to have settings." , Toast.LENGTH_LONG);
			toast.show();
		}
	}

}