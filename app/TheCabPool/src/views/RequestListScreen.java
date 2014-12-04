package views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import library.SphericalUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class RequestListScreen extends FragmentActivity{
	
	static JSONObject[] offersList = new JSONObject[15];
	private ListView mainListView;
	private ListView lstDetails;
	private ArrayAdapter<String> listAdapter;
	private ArrayAdapter<String> detailAdapter;
	private static ArrayList<Navigator[]> tripNavigators;
	private static GoogleMap map;
	private static String[] offer = new String[3];
	private Button btnAccept;
	private static LatLng acceptedOfferPos;
	private static String cost;
	private static String otherUser;
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_register_list_screen);
	    // Find the ListView resource. 
	    mainListView = (ListView) findViewById( R.id.list);
	    lstDetails = (ListView) findViewById( R.id.lstDetails);
	    btnAccept = (Button) findViewById(R.id.btnRequestListAcceptOffer);
	    ShareController controller = new ShareController(this);
	    btnAccept.setOnClickListener(controller);
	    

	    

	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, new ArrayList<String>());
	    detailAdapter = new ArrayAdapter<String>(this, R.layout.simplerow2, new ArrayList<String>());
	    
	    new Timer().schedule(new TimerTask() {

	        @Override
	        public void run() {
	            runOnUiThread(new Runnable() {
	            	StringBuffer offer = new StringBuffer();
	                public void run() {
	                	
	                	try{
	                		filterList();
	                		listAdapter.clear();
	                	
	                	    for(int i = 1; i<15; i++){
	                	    	
	                	    	if(offersList[i]!=null){		
	                	    		
	                	    		offer = new StringBuffer();
		                	    	offer.append("Offer #"+i+": ");
		                	    	offer.append(" User: " + offersList[i].getString("username"));
		                	    	offer.append(", Cost: " + cost(i));
		                	    	offer.append(", Duration: " + duration(i));
		                	    	listAdapter.add(offer.toString());
	                	    		
	                	    	}
	                	    }
	                		
	                	    }catch(Exception e){
	                	    	e.printStackTrace();
	                	    }
	                	
	                	updateLocation();
	                	    
	                }
					
	            });
	        }
	    }, 3000, 3000);
	   
	    
	    detailAdapter.add("Select an offer to view more details.");
	    
	    // Set the ArrayAdapter as the ListView's adapter.
	    mainListView.setAdapter( listAdapter );     
	    lstDetails.setAdapter(detailAdapter);
	    mainListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	int i = position+1;
            	int selectedOffer = i;
                detailAdapter.clear();
                detailAdapter.add("Offer #"+i+": ");
                try{
                offer[0] = offersList[i].getString("username");
                otherUser = offersList[i].getString("username");
                detailAdapter.add(" Offering user: " + offersList[i].getString("username"));
                offer[1] = cost(i);
                detailAdapter.add(" Your total cost->" + cost(i));
                cost = cost(i);
                offer[2] = duration(i);
                detailAdapter.add(" Your total trip duration->" + duration(i));
                detailAdapter.add(" User's Rating->" + offersList[i].getString("rating")+"/10");
                detailAdapter.add(" offerStartTime->" + offersList[i].getString("startTime"));
                //detailAdapter.add(" offerStartLocation->" + offersList[i].getString("startLocation"));
                //detailAdapter.add(" offerCurrentLocation->" + offersList[i].getString("currentLocation"));
                String[] pos = offersList[i].getString("currentLocation").split(" ");
                String lat = pos[0];
                String lon = pos[1];
                LatLng p = new LatLng(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
                acceptedOfferPos = p;
                //detailAdapter.add(" offerArrivalLocation->" + offersList[i].getString("arrivalDestination"));
                detailAdapter.add(" offerPreferredAgeStart->" + offersList[i].getString("preferredAgeStart"));
                detailAdapter.add(" offerPreferredAgeEnd->" + offersList[i].getString("preferredAgeEnd"));
                detailAdapter.add(" offerPreferredSex->" + offersList[i].getString("preferredSex"));
                
                }catch(Exception e){
                	e.printStackTrace();
                }
    	    
                
            }
        });
	    
	  }
	  
	  public static void setOffers(JSONObject[] offers, GoogleMap map2, ArrayList<Navigator[]> tripNavigators2){
		  offersList = offers;
		  map = map2;
		  tripNavigators=tripNavigators2;
	  }
	  
	 public static String duration(int selectedOffer){
		 String duration = null;
		 if(tripNavigators!=null){
		 Navigator[] trips = tripNavigators.get(selectedOffer-1);
		  Navigator pickupTrip = trips[0];
		  Navigator middleTrip = trips[1];
		  Navigator dropoffTrip = trips[2];
		  double time = pickupTrip.getDuration().get(0) + middleTrip.getDuration().get(0) + dropoffTrip.getDuration().get(0);
		  double minutes = (time/60)/6; 
		  duration = ""+Math.round(minutes)+" minutes";
		  }
		 
		  return duration;
	 }
	  
	  public static String cost(int selectedOffer) throws JSONException{	  
		  String cost = null;
		  if(tripNavigators!=null){
		  Navigator[] trips = tripNavigators.get(selectedOffer-1);
		  Navigator pickupTrip = trips[0];
		  Navigator middleTrip = trips[1];
		  Navigator dropoffTrip = trips[2];
		  Log.d("pickupTrip", ""+pickupTrip.getDistance());
		  double pickupDistance = (Double) pickupTrip.getDistance().get(0);
		  double middleDistance = (Double) middleTrip.getDistance().get(0);
		  double dropoffDistance = (Double) dropoffTrip.getDistance().get(0);
		  double subcharge = 0.0002;
		  
		  //subcharge per dollars per meter
		  //in the middle, where the taxi is shared the requester pays a third of the cost
		  double charge = 3 + pickupDistance*subcharge + middleDistance*subcharge/(3) + dropoffDistance*subcharge; 
		  
		  DecimalFormat df = new DecimalFormat("#.00"); 
		  
		  cost = "$"+df.format(charge);
		  }

		  return cost;
	  }
	  
	  public static void filterList(){
		  try{
			JSONObject[] newOffersList = new JSONObject[15];
			ArrayList<Navigator[]> newNavigators = new ArrayList<Navigator[]>();
			int counter = 1;
			for(int i = 1; i<15; i++){
				Log.d("OffersList["+i+"]", ""+offersList[i]);
				if(offersList[i]!=null){
					int requesterAge = Integer.parseInt(LoginScreen.getLoginData()[3]);
					int ageLow = Integer.parseInt(offersList[i].getString("preferredAgeStart"));
			  		int ageHigh = Integer.parseInt(offersList[i].getString("preferredAgeEnd")); 
			  		boolean isGender = offersList[i].getString("preferredSex").equals(LoginScreen.getLoginData()[2]);
			  		boolean isEither = offersList[i].getString("preferredSex").equals("Either");
			  		boolean isValid = (isGender || isEither) && requesterAge<=ageHigh && requesterAge>=ageLow;
			  		Log.d("isValid " + i, ""+isValid);
			  		if(isValid){
			  			newOffersList[counter] = offersList[i];
			  			newNavigators.add(tripNavigators.get(i));
			  			counter++;
			  			}
					}
				Log.d("newOffersList["+(counter-1)+"]", ""+newOffersList[counter-1]);
				}
			offersList = newOffersList;
			tripNavigators = newNavigators;
			
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	  
	  public static String[] getOfferUser(){
		  return offer;
	  }
	  
	  public void updateLocation(){
		double[] currentLocation = new double[2];
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, false);
		Location location = service.getLastKnownLocation(provider);
		LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
		currentLocation[0] = userLocation.latitude;
		currentLocation[1] = userLocation.longitude;
      	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
  		nameValuePairs.add(new BasicNameValuePair("requestType", "setLocation"));
  		nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
  		nameValuePairs.add(new BasicNameValuePair("location", currentLocation[0] + " " + currentLocation[1]));
  		DispatcherTask setLocation = new DispatcherTask("Share", nameValuePairs);
  		setLocation.execute();
	  }
	  
	  public static LatLng getPos(){
		  return acceptedOfferPos;
	  }
	  
	  public static String getCost(){
		  return cost;
	  }
	  
	  public static String getOtherUser(){
		  return otherUser;
	  }
	  
}
