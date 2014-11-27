package views;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupten.thecabpool.R;

import controllers.ShareController;

public class OfferScreen extends FragmentActivity implements GoogleMap.OnMapClickListener{

	private GoogleMap map;
	static TextView lblArrivalLocation;
	static EditText txtYoung;
	static EditText txtOld;
	static RadioGroup rgGender;
	static RadioButton rbEither, rbMale, rbFemale;
	Button btnArrivalLocation;
	Button btnSubmit;
	static Marker userMarker;
	static double[] currentLocation = new double[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_screen);
		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		final Criteria criteria = new Criteria();
		final String provider = service.getBestProvider(criteria, false);
		final Location location = service.getLastKnownLocation(provider);
		final LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
		userMarker = map.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
		map.setOnMapClickListener(this);
		
		lblArrivalLocation = (TextView) findViewById(R.id.lblOfferScreenArrivalLocation);
		txtYoung = (EditText) findViewById(R.id.txtOfferScreenYoung);
		txtOld = (EditText) findViewById(R.id.txtOfferScreenOld);
		rbEither = (RadioButton) findViewById(R.id.rbEither);
		rbMale = (RadioButton) findViewById(R.id.rbMale);
		rbFemale = (RadioButton) findViewById(R.id.rbFemale);
		btnArrivalLocation = (Button) findViewById(R.id.btnOfferScreenArrivalLocation);
		btnSubmit = (Button) findViewById(R.id.btnOfferScreenSubmit);
		
		ShareController controller = new ShareController(this);
		
		btnArrivalLocation.setOnClickListener(controller);
		btnSubmit.setOnClickListener(controller);
		
		Thread timer = new Thread(){
			public void run(){
				while(true){
				try{
					sleep(5000);
					LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					String provider = service.getBestProvider(criteria, false);
					Location location = service.getLastKnownLocation(provider);
					LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
					currentLocation[0] = userLocation.latitude;
					currentLocation[1] = userLocation.longitude;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}
			};
			timer.start();
	}
	
	public static String getYoungAge(){
		return txtYoung.getText().toString();
	}
	
	public static String getOldAge(){
		return txtOld.getText().toString();
	}
	
	public static String getGender(){
		String gender = "Either";
		if(rbEither.isChecked()) gender="Either";
		else if(rbMale.isChecked()) gender="Male";
		else if(rbFemale.isChecked()) gender="Female";
		return gender;
	}
	
	public static double[] getLocation(){
		LatLng pos = userMarker.getPosition();
		double[] location = new double[2];
		location[0] = pos.latitude;
		location[1] = pos.longitude;
		return location;
	}

	public static void setArrivalLocation(double[] pos){
		String location = "Arrival Location is: Lat/Long - " + pos[0] + "/" + pos[1];
		lblArrivalLocation.setText(location);
	}
	
	@Override
	public void onMapClick(LatLng point) {
		userMarker.remove();
		userMarker = map.addMarker(new MarkerOptions().position(point).title("Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
	}

	public static double[] getCurrentLocation() {
		return currentLocation;
	}

}