package views;

import java.util.ArrayList;
import java.util.List;

import library.PlacesAutoCompleteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupten.thecabpool.R;
import com.tyczj.mapnavigator.Navigator;

import controllers.ShareController;

public class OfferScreen extends FragmentActivity implements GoogleMap.OnMapClickListener{

	private static GoogleMap map;
	static TextView lblArrivalLocation;
	static EditText txtYoung;
	static EditText txtOld;
	static RadioGroup rgGender;
	static RadioButton rbEither, rbMale, rbFemale;
	Button btnArrivalLocation;
	static Button btnSubmit;
	AutoCompleteTextView txtAutoComplete;
	static Marker userMarker;
	static Marker endMarker;
	static double[] currentLocation = new double[2];
	private static Context context;
	private static Navigator nav;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_screen);
		context = this;
		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		final Criteria criteria = new Criteria();
		final String provider = service.getBestProvider(criteria, false);
		final Location location = service.getLastKnownLocation(provider);
		final LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
		userMarker = map.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
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
		
		String[] stringLocation = new String[2];
		stringLocation[0] = ""+currentLocation[0];
		stringLocation[1] = ""+currentLocation[1];
		txtAutoComplete = (AutoCompleteTextView) findViewById(R.id.txtAutoCompleteOffer);
		txtAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(stringLocation, this, R.layout.list_item_offer));
		txtAutoComplete.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
              	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            	nameValuePairs.add(new BasicNameValuePair("location", (String) adapterView.getItemAtPosition(position)));
            	DispatcherTask locate = new DispatcherTask("AutoCompleteOffer", nameValuePairs);
        		locate.execute();
            	}
			}
            );	
	}
	
	@Override
	protected void onDestroy() {
		String username = LoginScreen.getLoginData()[0];
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "removeOffer"));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		DispatcherTask removeOffer = new DispatcherTask("Share", nameValuePairs);
		//removeOffer.execute();
		super.onDestroy();
	}



	public static String getYoungAge(){
		return txtYoung.getText().toString();
	}
	
	public static String getOldAge(){
		return txtOld.getText().toString();
	}
	
	public static LatLng getEndPosition(){
		return endMarker.getPosition();
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
		LatLng point = new LatLng(pos[0],pos[1]);
		if(endMarker!=null) endMarker.remove();
		endMarker = map.addMarker(new MarkerOptions().position(point).title(userMarker.getTitle()));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14));	
		String location = "Arrival Location is: " + userMarker.getTitle();
		lblArrivalLocation.setText(location);
		LatLng curPoint = new LatLng(currentLocation[0],currentLocation[1]);
		drawPath(curPoint, point);
	}
	
	public static void drawPath(LatLng a, LatLng b){
		nav = new Navigator(map,a,b, "OfferContext");
		nav.findDirections(true);
	}
	
	public static void setMarker(LatLng point, String location){
		userMarker.remove();
		userMarker = map.addMarker(new MarkerOptions().position(point).title(location));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14));	
	}

	@Override
	public void onMapClick(LatLng point) {
		setMarker(point, "Custom Location");
	}
	
	public static void displayMessage(String msg){
		Toast toast = Toast.makeText(context, msg , Toast.LENGTH_LONG);
		toast.show();
	}

	public static double[] getCurrentLocation() {
		return currentLocation;
	}
	
	public static void submitSetText(String text){
		btnSubmit.setText(text);
	}

	public static String submitGetText(){
		return btnSubmit.getText().toString();
	}
}