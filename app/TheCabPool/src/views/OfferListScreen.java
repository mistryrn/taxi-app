package views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class OfferListScreen extends FragmentActivity{
	
		private Button btnAccept;
		private Button btnDecline;
		private ListView lstRequests;
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_offer_list_screen);
	    
	    btnAccept = (Button) findViewById(R.id.btnOfferListScreenAccept);
	    btnDecline = (Button) findViewById(R.id.btnOfferListScreenDecline);
	    lstRequests = (ListView) findViewById(R.id.lstOfferListScreenRequestsList);
	    
	    
	   
		 
	  }
	  
	
	  
	  
}
