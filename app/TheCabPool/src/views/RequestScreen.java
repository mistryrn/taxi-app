package views;

import java.util.Calendar;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupten.thecabpool.R;

import controllers.ShareController;

public class RequestScreen extends FragmentActivity implements GoogleMap.OnMapClickListener{
	
	private GoogleMap map;
	static TextView lblStartTime;
	static TextView lblStartLocation;
	static TextView lblArrivalLocation;
	Button btnStartLocation;
	Button btnArrivalLocation;
	Button btnSubmit;
	static TimePicker tpStartTime;
	static Marker userMarker;
	private int hour;
	private int minute;

	
	
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
	}
	
	public static double[] getLocation(){
		LatLng pos = userMarker.getPosition();
		double[] location = new double[2];
		location[0] = pos.latitude;
		location[1] = pos.longitude;
		return location;
	}
	
	public static void setStartLocation(double[] pos){
		String location = "Start Location is: Lat/Long - " + pos[0] + "/" + pos[1];
		lblStartLocation.setText(location);
	}
	
	public static void setArrivalLocation(double[] pos){
		String location = "Arrival Location is: Lat/Long - " + pos[0] + "/" + pos[1];
		lblArrivalLocation.setText(location);
	}
	
	public static int[] getTime(){
		int[] time = new int[2];
		time[0] = tpStartTime.getCurrentHour();
		time[1] = tpStartTime.getCurrentMinute();
		return time;
	}



	@Override
	public void onMapClick(LatLng point) {
		userMarker.remove();
		userMarker = map.addMarker(new MarkerOptions().position(point).title("Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));	
	}

}
