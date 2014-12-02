package controllers;

import java.util.ArrayList;
import java.util.List;

import library.IntentIntegrator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupten.thecabpool.R;
import AsyncTasks.DispatcherTask;
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
import android.util.Log;
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
		DispatcherTask login = new DispatcherTask("Security", nameValuePairs);
		login.execute();
	}
	
	private void loginSubmitClicked() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "login"));
        nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("pass", LoginScreen.getPassword()));
		DispatcherTask login = new DispatcherTask("Security", nameValuePairs);
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
		DispatcherTask register = new DispatcherTask("Security", nameValuePairs);
		register.execute();
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
	
	public static void httpResponse(String response) throws JSONException{
		JSONObject wholeObject = new JSONObject(response);
		String type = wholeObject.getString("type");
		String success = wholeObject.getString("success");
		Log.d("Type", type);
		if(type.equals("loginUser")) loginUserResponse(wholeObject);
		if(type.equals("registerUser")) registerUserResponse(wholeObject);
		if(type.equals("scanCode")) scanCodeResponse(wholeObject);
	}
	
	private static void scanCodeResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
			((Activity) securityContext).finish();
			Intent intent = new Intent(securityContext, OfferScreen.class);
	    	securityContext.startActivity(intent);
	    	ScanCodeScreen.setCodeScanned(true);
		}
		else{
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private static void registerUserResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
			((Activity) securityContext).finish();
		}
		else{
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private static void loginUserResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
			LoginScreen.setLoginData(LoginScreen.getUsername(), LoginScreen.getPassword());
			((Activity) securityContext).finish();
		}
		else{
			Toast toast = Toast.makeText(securityContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	
	

}
