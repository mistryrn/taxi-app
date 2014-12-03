package views;

import java.util.ArrayList;
import java.util.List;

import library.PlacesAutoCompleteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.google.android.gms.maps.model.Polyline;
import com.groupten.thecabpool.R;
import com.tyczj.mapnavigator.Navigator;

import controllers.ShareController;

public class YourTripScreen extends FragmentActivity implements GoogleMap.OnMapClickListener{

	private static GoogleMap map;
	private static Navigator nav;
	private static LatLng otherPos, finalPos;
	private boolean tripFlag;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_trip);	
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tripMap)).getMap();
		
	
					
					LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					String provider = service.getBestProvider(criteria, false);
					Location location = service.getLastKnownLocation(provider);
					LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
			
					Marker currentPosition = map.addMarker(new MarkerOptions().position(userLocation).title("You"));
					Marker otherUserPosition = map.addMarker(new MarkerOptions().position(otherPos).title("Other User"));
					Marker finalPosition = map.addMarker(new MarkerOptions().position(finalPos).title("Destination"));
					drawPath(currentPosition.getPosition(),otherUserPosition.getPosition());
					drawPath(currentPosition.getPosition(), finalPosition.getPosition());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(otherPos, 14));
			
			
			tripFlag=true;
			Thread timer = new Thread(){
				public void run(){
					
					try{
						sleep(10000);
					
						Intent intent = new Intent(YourTripScreen.this, DisplayFareScreen.class);
						if(tripFlag)YourTripScreen.this.startActivity(intent);
						tripFlag=false;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			
				};
				timer.start();
	}


	@Override
	public void onMapClick(LatLng arg0) {
		
	}
	
	public static Marker setCurrentPosition(LatLng pos){
		Log.d("Current Position", pos.toString());
		final Marker currentPosition = map.addMarker(new MarkerOptions().position(pos).title("You"));
		return currentPosition;
	}
	
	public static Marker setOtherUserPosition(LatLng pos){
		Log.d("Other Position", pos.toString());
		final Marker otherUserPosition = map.addMarker(new MarkerOptions().position(pos).title("Other User"));
		return otherUserPosition;
	}
	
	public static Marker setFinalPosition(LatLng pos){
		Log.d("Final Position", pos.toString());
		final Marker finalPosition = map.addMarker(new MarkerOptions().position(pos).title("Destination"));
		return finalPosition;
	}
	
	public static void drawPath(LatLng a, LatLng b){
		nav = new Navigator(map,a,b, "OfferContext");
		nav.findDirections(true);
	}
	
	public static void drawPath(Marker currentM, Marker otherM, Marker finalM){
		if(nav != null){
		ArrayList<Polyline> polylines = nav.getPathLines();
		for(Polyline line : polylines)
		{
		    line.remove();
		}
		}
		drawPath(currentM.getPosition(),otherM.getPosition());
		drawPath(currentM.getPosition(), finalM.getPosition());
	}
	
	public static void setOtherPos(LatLng point){
		otherPos = point;
	}
	
	public static void setFinalPos(LatLng point){
		finalPos = point;
	}
	
	
}