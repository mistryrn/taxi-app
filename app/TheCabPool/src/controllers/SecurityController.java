package controllers;

import java.util.ArrayList;
import java.util.List;

import library.IntentIntegrator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.groupten.thecabpool.R;
import AsyncTasks.SecurityTask;
import views.LoginScreen;
import views.MainMenu;
import views.OfferScreen;
import views.RegisterScreen;
import views.ScanCodeScreen;
import views.SettingsScreen;
import views.settings.BlackListScreen;
import views.settings.ChangePasswordScreen;
import views.settings.FavouriteLocationsScreen;
import views.settings.FriendsListScreen;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class SecurityController extends SettingsScreen implements View.OnClickListener{
	private static Context securityContext;
	private static int loginAttempts;
	
	public SecurityController(Context context) {
		SecurityController.securityContext=context;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLoginSubmit:
			loginSubmitClicked();
			break;
			
		case R.id.btnRegisterSubmit:
			registerSubmitClicked();
			break;
			
		case R.id.btnSubmitCode:
			scanSubmitClicked();
			break;
			
		default:
			
			break;	
		}
	}
	

	private void scanSubmitClicked() {
		String code = ScanCodeScreen.getCode();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "scan"));
		nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
		nameValuePairs.add(new BasicNameValuePair("code", code));
		SecurityTask login = new SecurityTask("ScanCode", nameValuePairs);
		login.execute();
	}
	
	private void loginSubmitClicked() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "login"));
        nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("pass", LoginScreen.getPassword()));
		SecurityTask login = new SecurityTask("Login", nameValuePairs);
		login.execute();
	}
	
	private void registerSubmitClicked() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "register"));
        nameValuePairs.add(new BasicNameValuePair("username", RegisterScreen.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("pass", RegisterScreen.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("date", RegisterScreen.getDateOfBirth()));
        nameValuePairs.add(new BasicNameValuePair("sex", RegisterScreen.getSex()));
        nameValuePairs.add(new BasicNameValuePair("number", RegisterScreen.getPhoneNumber())); 
		SecurityTask register = new SecurityTask("Register", nameValuePairs);
		register.execute();
	}
	
	public static void httpResponse(String response){
		if(response.equals("Scan Successful")){
			scanSuccess(response);
		}
		else if(response.equals("Login Successful")){
			LoginScreen.setLoginData(LoginScreen.getUsername(), LoginScreen.getPassword());
			((Activity) securityContext).finish();
			response = response.concat(" - Welcome back! " + LoginScreen.getLoginData()[0]);
		}
		else if(response.equals("Registration Successful")){
			((Activity) securityContext).finish();
			response = response.concat(" - You may now login ");
		}
		else if(response.equals("Error: Login Unsuccessful")){
			loginAttempts++;
			if(loginAttempts>3){
				response = response.concat(" - to create a new account you must register ");
			}
			
		}
		
		Toast toast = Toast.makeText(securityContext, response , Toast.LENGTH_LONG);
		toast.show();
	}
	
	private static void scanSuccess(String response){
		ScanCodeScreen.setCode(response + ", you (" + LoginScreen.getLoginData()[0] + ") are in registered Taxi #" + ScanCodeScreen.getCode().charAt(7)) ;
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent intent = new Intent(securityContext, OfferScreen.class);
			    	securityContext.startActivity(intent);
					((Activity) securityContext).finish();
				}
			}
		};
		timer.start();
	}
	

}
