package controllers;

import views.LoginScreen;
import views.MainMenu;
import views.OfferScreen;
import views.RegisterScreen;
import views.RequestScreen;
import views.SettingsScreen;

import com.groupten.thecabpool.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

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
			
		case R.id.btnExit:
			exitClicked();
			break;
			
		default:
			super.errorMessage();
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
	}
	
	private void offerClicked() {
		Intent intent = new Intent(mainContext, OfferScreen.class);
    	mainContext.startActivity(intent);
	}
	
	private void requestClicked() {
		Intent intent = new Intent(mainContext, RequestScreen.class);
    	mainContext.startActivity(intent);
	}
	
	private void settingsClicked() {
		Intent intent = new Intent(mainContext, SettingsScreen.class);
    	mainContext.startActivity(intent);
	}

	private void exitClicked(){
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
