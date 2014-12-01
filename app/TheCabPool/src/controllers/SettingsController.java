package controllers;

import com.groupten.thecabpool.R;

import views.MainMenu;
import views.SettingsScreen;
import views.settings.BlackListScreen;
import views.settings.ChangePasswordScreen;
import views.settings.FavouriteLocationsScreen;
import views.settings.FriendsListScreen;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class SettingsController extends SettingsScreen implements View.OnClickListener{
	private static Context settingsContext;
	
	public SettingsController(Context context) {
		SettingsController.settingsContext=context;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnFavouriteLocations:
			favouriteLocationsClicked();
			break;
			
		case R.id.btnFriendslist:
			friendlistClicked();
			break;	

		case R.id.btnBlacklist:
			blacklistClicked();
			break;	
			
		case R.id.btnChangePassword:
			changePasswordClicked();
			break;	
			
			
		default:
			
			break;	
		}
	}
	private void favouriteLocationsClicked() {
		Intent intent = new Intent(settingsContext, FavouriteLocationsScreen.class);
    	settingsContext.startActivity(intent);
	}
	private void friendlistClicked() {
		Intent intent = new Intent(settingsContext, FriendsListScreen.class);
    	settingsContext.startActivity(intent);
	}
	private void blacklistClicked() {
		Intent intent = new Intent(settingsContext, BlackListScreen.class);
    	settingsContext.startActivity(intent);
	}
	private void changePasswordClicked() {
		Intent intent = new Intent(settingsContext, ChangePasswordScreen.class);
    	settingsContext.startActivity(intent);
	}
	public static void httpResponse(String response) {
		Toast toast = Toast.makeText(settingsContext, response, Toast.LENGTH_LONG);
		toast.show();
		
	}
	

}
