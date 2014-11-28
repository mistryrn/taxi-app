package views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import library.PlacesAutoCompleteAdapter;
import AsyncTasks.ShareTask;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupten.thecabpool.R;

import controllers.ShareController;

public class RequestScreen extends FragmentActivity implements GoogleMap.OnMapClickListener{
	
	private static GoogleMap map;
	static TextView lblStartTime;
	static TextView lblStartLocation;
	static TextView lblArrivalLocation;
	Button btnStartLocation;
	Button btnArrivalLocation;
	Button btnSubmit;
	AutoCompleteTextView txtAutoComplete;
	static TimePicker tpStartTime;
	static Marker userMarker;
	static Marker startMarker;
	static Marker endMarker;
	private int hour;
	private int minute;
	private static ArrayAdapter listAdapter;
	private static String[] currentLocation = new String[2];

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_screen);

		
	    

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		
		
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, false);
		Location location = service.getLastKnownLocation(provider);
		LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
		userMarker = map.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
		map.setOnMapClickListener(this);
		
		currentLocation[0] = ""+userLocation.latitude;
		currentLocation[1] = ""+userLocation.longitude;
		
		tpStartTime = (TimePicker) findViewById(R.id.tpRequestScreenStartTime);
		
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		
		tpStartTime.setCurrentHour(hour);
		tpStartTime.setCurrentMinute(minute);
		
		ShareController controller = new ShareController(this);
		
		//views
		lblStartLocation = (TextView) findViewById(R.id.lblRequestScreenStartLocation);
		lblArrivalLocation = (TextView) findViewById(R.id.lblRequestScreenArrivalDestination);
		
		//buttons
		btnStartLocation = (Button) findViewById(R.id.btnRequestScreenStartLocation);
		btnArrivalLocation = (Button) findViewById(R.id.btnRequestScreenArrivalDestination);
		btnSubmit = (Button) findViewById(R.id.btnRequestScreenSubmit);
		
		btnStartLocation.setOnClickListener(controller);
		btnArrivalLocation.setOnClickListener(controller);
		btnSubmit.setOnClickListener(controller);
		
		txtAutoComplete = (AutoCompleteTextView) findViewById(R.id.txtAutoComplete);
		txtAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
		txtAutoComplete.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            	nameValuePairs.add(new BasicNameValuePair("location", (String) adapterView.getItemAtPosition(position)));
            	ShareTask locate = new ShareTask("AutoComplete", nameValuePairs);
        		locate.execute();
            	}

			
			}
            );

	}
	
	public static double[] getMarkedLocation(){
		LatLng pos = userMarker.getPosition();
		double[] location = new double[2];
		location[0] = pos.latitude;
		location[1] = pos.longitude;
		return location;
	}
	
	public static double[] getStartLocation(){
		LatLng pos = startMarker.getPosition();
		double[] location = new double[2];
		location[0] = pos.latitude;
		location[1] = pos.longitude;
		return location;
	}
	public static double[] getEndLocation(){
		LatLng pos = endMarker.getPosition();
		double[] location = new double[2];
		location[0] = pos.latitude;
		location[1] = pos.longitude;
		return location;
	}
	
	public static void setStartLocation(double[] pos){
		LatLng point = new LatLng(pos[0],pos[1]);
		if(startMarker!=null) startMarker.remove();
		startMarker = map.addMarker(new MarkerOptions().position(point).title(userMarker.getTitle()));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));	
		String location = "Start Location is: " + userMarker.getTitle();
		lblStartLocation.setText(location);
	}
	
	public static void setArrivalLocation(double[] pos){
		LatLng point = new LatLng(pos[0],pos[1]);
		if(endMarker!=null) endMarker.remove();
		endMarker = map.addMarker(new MarkerOptions().position(point).title(userMarker.getTitle()));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));	
		String location = "Arrival Location is: " + userMarker.getTitle();
		lblArrivalLocation.setText(location);
	}
	
	public static int[] getTime(){
		int[] time = new int[2];
		time[0] = tpStartTime.getCurrentHour();
		time[1] = tpStartTime.getCurrentMinute();
		return time;
	}

	public static void setMarker(LatLng point, String location){
		userMarker.remove();
		userMarker = map.addMarker(new MarkerOptions().position(point).title(location));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));	
	}

	@Override
	public void onMapClick(LatLng point) {
		setMarker(point, "Custom Location");
	}
	
	public static void setSuggestions(){
		
	}
	
	public static String[] requestLocation(){
		Log.d("Current Lat", currentLocation[0]);
		Log.d("Current Long", currentLocation[1]);
		return currentLocation;
	}

}
