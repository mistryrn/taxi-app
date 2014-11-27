package views.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.groupten.thecabpool.R;

import controllers.SettingsController;

public class FriendsListScreen extends Activity{
	
	ListView lstFriendsList;
	EditText txtUserFriend;
	Button btnAddFriend;
	Button btnRemoveFriend;
	
	ListView lstRequestList;
	EditText txtUserRequest;
	Button btnAcceptRequest;
	Button btnDeclineRequest;
	
	TextView lblMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendslist_screen);
		
		lstFriendsList = (ListView) findViewById(R.id.lstFriendsList);
		txtUserFriend = (EditText) findViewById(R.id.txtUserFriend);
		btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
		btnRemoveFriend =  (Button) findViewById(R.id.btnRemoveFriend);
		
		lstRequestList = (ListView) findViewById(R.id.lstRequestList);
		txtUserRequest = (EditText) findViewById(R.id.txtUserRequest);
		btnAcceptRequest = (Button) findViewById(R.id.btnAcceptRequest);
		btnDeclineRequest =  (Button) findViewById(R.id.btnDeclineRequest);
		
		lblMessage = (TextView) findViewById(R.id.lblFriendsListMessage);
		
		SettingsController controller = new SettingsController(this);
		
		btnAddFriend.setOnClickListener(controller);
		btnRemoveFriend.setOnClickListener(controller);
		
		btnAcceptRequest.setOnClickListener(controller);
		btnDeclineRequest.setOnClickListener(controller);
	}
	
	public void displayMessage(String msg){
		lblMessage.setText(msg);
	}
	
	public String getUserFriend(){
		return txtUserFriend.getText().toString();
	}
	
	public String getUserRequest(){
		return txtUserRequest.getText().toString();
	}

}