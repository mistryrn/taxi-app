package controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupten.thecabpool.R;

import views.LoginScreen;
import views.MainMenu;
import views.SettingsScreen;
import views.settings.BlackListScreen;
import views.settings.ChangePasswordScreen;
import views.settings.FavouriteLocationsScreen;
import views.settings.FriendsListScreen;
import AsyncTasks.DispatcherTask;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
			
		case R.id.btnAddFriend:
			addFriendClicked();
			break;	
			
		case R.id.btnRemoveFriend:
			removeFriendClicked();
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
	private void removeFriendClicked() {
		
	}
	private void addFriendClicked() {
		
	}
	private void favouriteLocationsClicked() {
		Intent intent = new Intent(settingsContext, FavouriteLocationsScreen.class);
    	settingsContext.startActivity(intent);
	}
	private void friendlistClicked() {
		//populate friends list
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("requestType", "retrieveFriendslist"));
				nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
				DispatcherTask friendslist = new DispatcherTask("Settings", nameValuePairs);
				friendslist.execute();
				
		//populate friend request list
				List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
				nameValuePairs2.add(new BasicNameValuePair("requestType", "retrieveFriendRequests"));
				nameValuePairs2.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
				DispatcherTask requestlist = new DispatcherTask("Settings", nameValuePairs2);
				requestlist.execute();
		
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
	public static void httpResponse(String response) throws JSONException {
		JSONObject wholeObject = new JSONObject(response);
		String type = wholeObject.getString("type");
		String success = wholeObject.getString("success");
		Log.d("Type", type);
		
		if(type.equals("retrieveFriendslist")) retrieveFriendslistResponse(wholeObject);
		if(type.equals("retrieveFriendRequests")) retrieveFriendRequestsResponse(wholeObject);
		
		
		
	}
	private static void retrieveFriendRequestsResponse(JSONObject wholeObject) throws JSONException {
		JSONArray message = wholeObject.getJSONArray("message");
		ArrayList<String> requestlist = new ArrayList<String>();
		int i = 0;
		while(!message.isNull(i)){
			requestlist.add(message.getString(i));
			i++;
		}
		Log.d("Friendslist", requestlist.toString());
		FriendsListScreen.setRequestlist(requestlist);
	}
	private static void retrieveFriendslistResponse(JSONObject wholeObject) throws JSONException {
		JSONArray message = wholeObject.getJSONArray("message");
		ArrayList<String> friendslist = new ArrayList<String>();
		int i = 0;
		while(!message.isNull(i)){
			friendslist.add(message.getString(i));
			i++;
		}
		Log.d("Friendslist", friendslist.toString());
		FriendsListScreen.setFriendslist(friendslist);
	}
	

}
