package views.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import views.LoginScreen;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.groupten.thecabpool.R;

import controllers.SettingsController;

public class FriendsListScreen extends Activity{
	
	private ListView lstFriendsList;
	private EditText txtUserFriend;
	private Button btnAddFriend;
	private Button btnRemoveFriend;
	
	private ListView lstRequestList;
	private EditText txtUserRequest;
	private Button btnAcceptRequest;
	private Button btnDeclineRequest;
	
	private static ArrayAdapter<String> friendAdapter;
	private static ArrayAdapter<String> requestAdapter;
	
	private static ArrayList<String> friendslist = new ArrayList<String>();
	private static ArrayList<String> requestlist = new ArrayList<String>();
	
	private TextView lblMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendslist_screen);
		
		lstFriendsList = (ListView) findViewById(R.id.lstFriendsList);
		friendAdapter = new ArrayAdapter<String>(this, R.layout.friendslist, new ArrayList<String>());
		lstFriendsList.setAdapter(friendAdapter);
		 new Timer().schedule(new TimerTask() {
		        @Override
		        public void run() {
		            runOnUiThread(new Runnable() {
		                public void run() {
		                	Log.d("Friendslist Screen", friendslist.toString());
		                	friendAdapter.clear();
		            		for(int i = 0; i<friendslist.size(); i++){    			
		            			friendAdapter.add(friendslist.get(i));
		            		}
		            		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            		nameValuePairs.add(new BasicNameValuePair("requestType", "retrieveFriendslist"));
		            		nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
		            		DispatcherTask friendslist = new DispatcherTask("Settings", nameValuePairs);
		            		friendslist.execute();
		            		DispatcherTask refreshlist = new DispatcherTask("Settings", nameValuePairs);
		            		refreshlist.execute();
		                }
		            });
		        }
		    }, 2000, 10000);
		
		txtUserFriend = (EditText) findViewById(R.id.txtUserFriend);
		btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
		btnRemoveFriend =  (Button) findViewById(R.id.btnRemoveFriend);
		
		lstRequestList = (ListView) findViewById(R.id.lstRequestList);
		requestAdapter = new ArrayAdapter<String>(this, R.layout.friendrequests, new ArrayList<String>());
		lstRequestList.setAdapter(requestAdapter);
		 new Timer().schedule(new TimerTask() {
		        @Override
		        public void run() {
		            runOnUiThread(new Runnable() {
		                public void run() {
		                	Log.d("Friendslist Screen", requestlist.toString());
		                	requestAdapter.clear();
		            		for(int i = 0; i<requestlist.size(); i++){    			
		            			requestAdapter.add(requestlist.get(i));
		            		}
		    				List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
		    				nameValuePairs2.add(new BasicNameValuePair("requestType", "retrieveFriendRequests"));
		    				nameValuePairs2.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
		    				DispatcherTask requestlist = new DispatcherTask("Settings", nameValuePairs2);
		    				requestlist.execute();
		                }
		            });
		        }
		    }, 2000, 10000);
		
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

	public static void setFriendslist(ArrayList<String> newFriendslist) {
		friendslist = newFriendslist;
	}

	public static void setRequestlist(ArrayList<String> newRequestlist) {
		requestlist = newRequestlist;
	}

}