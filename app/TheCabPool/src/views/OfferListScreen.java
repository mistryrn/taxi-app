package views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.groupten.thecabpool.R;
import com.tyczj.mapnavigator.Navigator;

import controllers.SecurityController;
import controllers.ShareController;

public class OfferListScreen extends FragmentActivity{
	
		private Button btnAccept;
		private Button btnDecline;
		private ListView lstRequests;
		private ArrayAdapter<String> listAdapter;
		private static JSONObject[] requestList = new JSONObject[10];
		private String previousRequest;
		private TextView lblSelectedChoice;
		private static String selectedUser;
		private static boolean refreshFlag;
		private static String cost;
		
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_offer_list_screen);
	    
	    lblSelectedChoice = (TextView) findViewById(R.id.lblOfferListSelectedChoice);
	    btnAccept = (Button) findViewById(R.id.btnOfferListScreenAccept);
	    btnDecline = (Button) findViewById(R.id.btnOfferListScreenDecline);
	    ShareController controller = new ShareController(this);
	    btnAccept.setOnClickListener(controller);
	    btnDecline.setOnClickListener(controller);
	    
	    lstRequests = (ListView) findViewById(R.id.lstOfferListScreenRequestsList);
	    listAdapter = new ArrayAdapter<String>(this, R.layout.list_item_offer, new ArrayList<String>());
	    lstRequests.setAdapter(listAdapter);
	    lstRequests.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    refreshFlag = true;
	    new Timer().schedule(new TimerTask() {
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable() {
	                public void run() {
	                	listAdapter.clear();
	                	StringBuffer request = new StringBuffer();
	                	try{
	                		for(int i=0; i<10; i++){
	                			if(requestList[i] != null){
	                				request.append("Username/Rating: ");
	                				request.append(requestList[i].getString("username")+"/"+requestList[i].getString("rating"));
	                				request.append(", "+requestList[i].getString("cost"));
	                				request.append(", "+requestList[i].getString("duration"));
	                				

	                				listAdapter.add(request.toString());
	                			}
	                			request.delete(0, request.length());
	                			}
	                	    }catch(Exception e){
	                	    	e.printStackTrace();
	                	    }
	                	if(refreshFlag){
	                	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    				nameValuePairs.add(new BasicNameValuePair("requestType", "retrieveRequests"));
	    				nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
	    				DispatcherTask retrieveRequests = new DispatcherTask("Share", nameValuePairs);
	    				retrieveRequests.execute();
	                	}
	                }
	            });
	        }
	    }, 3000, 5000);
	    
	    
	    lstRequests.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	if(previousRequest!=null) listAdapter.remove(previousRequest);
            	String request = listAdapter.getItem(position);
            	lblSelectedChoice.setText("Selected: " + request);
            	try {
					selectedUser = requestList[position].getString("username");
					cost = requestList[position].getString("cost");
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        });
	
	  }
	  
	  @Override
		protected void onDestroy() {
			String username = LoginScreen.getLoginData()[0];
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("requestType", "removeOffer"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			DispatcherTask removeOffer = new DispatcherTask("Share", nameValuePairs);
			removeOffer.execute();
			List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
			nameValuePairs2.add(new BasicNameValuePair("requestType", "clearInTaxi"));
			nameValuePairs2.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
			DispatcherTask check = new DispatcherTask("Share", nameValuePairs2);
			check.execute();
			super.onDestroy();
		}

	public static void setRequests(JSONObject[] newRequestList) {
		requestList = newRequestList;
	}
	  
	
	public static String getSelectedUser(){
		return selectedUser;
	}
	
	public static void setRefreshFlag(boolean b){
		refreshFlag = b;
	}
	
	public static String getCost(){
		return cost;
	}
	  
	  
}
