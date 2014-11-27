package views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.groupten.thecabpool.R;

import controllers.SecurityController;
import controllers.ShareController;

public class RequestListScreen extends Activity{
	
	static String offersList[][] = new String [15][15];
	
	private ListView mainListView ;
	private ArrayAdapter<String> listAdapter ;
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_register_list_screen);
	    
	    // Find the ListView resource. 
	    mainListView = (ListView) findViewById( R.id.list);

	    // Create and populate a List of planet names.
	     
	    ArrayList<String> planetList = new ArrayList<String>();
	    
	    // Create ArrayAdapter using the planet list.
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
	    
	    // Add more planets. If you passed a String[] instead of a List<String> 
	    // into the ArrayAdapter constructor, you must not add more items. 
	    // Otherwise an exception will occur.
	    String offer;
	    int i = 0;
	    for(String[] offerItem: offersList){
	    offer = "Offer #"+i+": " + offerItem[1] 
	    		+ " " + offerItem[2] 
	    		+ " " + offerItem[3] 
	    		+ " " + offerItem[4] 
	    		+ " " + offerItem[5]
	    		+ " " + offerItem[6] 
	    		+ " " + offerItem[7] 
	    		+ " " + offerItem[8] 
	    		+ " " + offerItem[9] 
	    		+ " " + offerItem[10];
	    listAdapter.add(offer);
	    i++;
	    }
	    
	    
	    // Set the ArrayAdapter as the ListView's adapter.
	    mainListView.setAdapter( listAdapter );      
	  }
	  
	  public static void setOffers(String[][] offers){
		  offersList = offers;
	  }
}
