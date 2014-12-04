package views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.groupten.thecabpool.R;

import controllers.SettingsController;

public class SettingsScreen extends Activity{
	//declare buttons
	Button btnFavouriteLocations;
	Button btnFriendslist;
	Button btnBlacklist;
	Button btnChangePassword;
	Button btnDeleteAccount;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);
		
		//initialize buttons
		btnFavouriteLocations = (Button) findViewById(R.id.btnFavouriteLocations);
		btnFriendslist = (Button) findViewById(R.id.btnFriendslist);
		btnBlacklist = (Button) findViewById(R.id.btnBlacklist);
		btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
		btnDeleteAccount = (Button) findViewById(R.id.btnSettingsScreenDeleteAccount);
		
		//initialize controller
		SettingsController controller = new SettingsController(this);
		
		//connect controller to each button
		btnFavouriteLocations.setOnClickListener(controller);
		btnFriendslist.setOnClickListener(controller);
		btnBlacklist.setOnClickListener(controller);
		btnChangePassword.setOnClickListener(controller);
		btnDeleteAccount.setOnClickListener(controller);
	}
	
}