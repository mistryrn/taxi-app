package views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import library.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.groupten.thecabpool.R;

import controllers.SecurityController;
import controllers.ShareController;

public class RequestListScreen extends FragmentActivity{
	
	static JSONObject[] offersList = new JSONObject[15];
	private int selectedOffer;
	private ListView mainListView ;
	private ListView lstDetails;
	private ArrayAdapter<String> listAdapter ;
	private ArrayAdapter<String> detailAdapter ;
	private GoogleMap map;
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_register_list_screen);
	    
	    // Find the ListView resource. 
	    mainListView = (ListView) findViewById( R.id.list);
	    lstDetails = (ListView) findViewById( R.id.lstDetails);

	    // Create and populate a List of planet names.
	     
	    ArrayList<String> planetList = new ArrayList<String>();
	    ArrayList<String> planetList2 = new ArrayList<String>();
	    
	    // Create ArrayAdapter using the planet list.
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
	    detailAdapter = new ArrayAdapter<String>(this, R.layout.simplerow2, planetList2);
	    
	    // Add more planets. If you passed a String[] instead of a List<String> 
	    // into the ArrayAdapter constructor, you must not add more items. 
	    // Otherwise an exception will occur.
	    StringBuffer offer = new StringBuffer();
	    try{
	    for(int i = 1; i<15; i++){
	    	if(offersList[i]!=null){
	    	offer.append("Offer #"+i+": ");
	    	offer.append(" user->" + offersList[i].getString("username"));
	    	offer.append(" cost->" + directCost(i));
	    	
	    listAdapter.add(offer.toString());
	    	}
	    i++;
	    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    detailAdapter.add("Select an offer to view more details.");
	    
	    // Set the ArrayAdapter as the ListView's adapter.
	    mainListView.setAdapter( listAdapter );     
	    lstDetails.setAdapter(detailAdapter);
	    mainListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	int i = position+1;
            	selectedOffer = i;
                detailAdapter.clear();
                detailAdapter.add("Offer #"+i+": ");
                try{
                detailAdapter.add(" user->" + offersList[i].getString("username"));
                detailAdapter.add(" startTime->" + offersList[i].getString("startTime"));
                detailAdapter.add(" startLocation->" + offersList[i].getString("startLocation"));
                detailAdapter.add(" currentLocation->" + offersList[i].getString("currentLocation"));
                detailAdapter.add(" arrivalLocation->" + offersList[i].getString("arrivalDestination"));
                detailAdapter.add(" preferredAgeStart->" + offersList[i].getString("preferredAgeStart"));
                detailAdapter.add(" preferredAgeEnd->" + offersList[i].getString("preferredAgeEnd"));
                detailAdapter.add(" preferredSex->" + offersList[i].getString("preferredSex"));
                }catch(Exception e){
                	e.printStackTrace();
                }
    	    
                
            }
        });
	  }
	  
	  public static void setOffers(JSONObject[] offers){
		  offersList = offers;
	  }
	  
	  public String directCost(int selectedOffer) throws JSONException{
		  String cost = null;
		  double[] reqStart = ShareController.getStart();
		  double reqStartLat = reqStart[0];
		  double reqStartLong = reqStart[1];
		  LatLng rStart = new LatLng(reqStartLat, reqStartLong);
		  
		  double[] reqEnd = ShareController.getFinal();
		  double reqEndLat = reqEnd[0];
		  double reqEndLong = reqEnd[1];
		  LatLng rEnd = new LatLng(reqEndLat, reqEndLong);
		  
		  String[] offerCur =  offersList[selectedOffer].getString("currentLocation").split(" ");
		  double offerCurLat = Double.parseDouble(offerCur[0]);
		  double offerCurLong = Double.parseDouble(offerCur[1]);
		  LatLng oCurrent = new LatLng(offerCurLat, offerCurLong);
		  
		  String[] offerEnd =  offersList[selectedOffer].getString("arrivalDestination").split(" ");
		  double offerEndLat = Double.parseDouble(offerEnd[0]);
		  double offerEndLong = Double.parseDouble(offerEnd[1]);
		  LatLng oEnd = new LatLng(offerEndLat, offerEndLong);
		  
		  double pickupDistance = SphericalUtil.computeDistanceBetween(rStart, oCurrent); //in meters
		  double dropDistance = SphericalUtil.computeDistanceBetween(oEnd, rEnd); //in meters
		  
		  double charge = pickupDistance*0.0015 + dropDistance*0.0015 ; //0.0015  dollars per meter
		  
		  DecimalFormat df = new DecimalFormat("#.00"); 
		  
		 cost = "$"+df.format(charge);
		
		  
		  return cost;
	  }
	  
	  
}
