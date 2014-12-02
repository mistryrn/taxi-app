package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.groupten.thecabpool.R;
import com.tyczj.mapnavigator.Navigator;

import views.LoginScreen;
import views.MainMenu;
import views.OfferListScreen;
import views.OfferScreen;
import views.RegisterScreen;
import views.RequestListScreen;
import views.RequestScreen;
import views.ScanCodeScreen;
import views.SettingsScreen;
import views.settings.FriendsListScreen;
import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ShareController extends RequestScreen implements View.OnClickListener{
	private static Context shareContext;
	private Intent i;
	final static int cameraData = 0;
	private static double[] startLocation = new double[2];
	private static double[] finalLocation = new double[2];
	private static ArrayList<Navigator[]> tripNavigators;
	static GoogleMap map;
	private static Thread updateTimer;
	private static boolean timerFlag;
	
	public ShareController(Context context, GoogleMap map) {
		ShareController.shareContext=context;
		this.map = map;
	}
	
	public ShareController(Context context) {
		ShareController.shareContext=context;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnRequestScreenStartLocation:
			startLocationClicked();
			break;
			
		case R.id.btnRequestScreenArrivalDestination:
			arrivalLocationClicked();
			break;
			
		case R.id.btnRequestScreenSubmit:
			submitRequestClicked();
			break;
			
		case R.id.btnOfferScreenSubmit:
			submitOfferClicked();
			break;
			
		case R.id.btnOfferScreenArrivalLocation:
			offerArrivalLocationClicked();
			break;
			
		default:
			
			break;	
		}
	}
	

	private void offerArrivalLocationClicked() {
		double[] pos = OfferScreen.getLocation();
		String location = "Lat = " + pos[0] + " Long = " + pos[1];
		finalLocation = pos;
		OfferScreen.setArrivalLocation(pos);
	}
	private void submitOfferClicked() {
		if(OfferScreen.submitGetText().equals("Submit Offer")){
		double[] scanLocation = ScanCodeScreen.getLocation();
		double[] arrivalLocation = OfferScreen.getLocation();
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		String startTime = hour+":"+minute;
		
		//format data
		String username = LoginScreen.getLoginData()[0];
		String startLocation = scanLocation[0] + " " + scanLocation[1];
		String arrivalDestination = arrivalLocation[0] + " " + arrivalLocation[1];
		String ageStart = OfferScreen.getYoungAge();
		String ageEnd = OfferScreen.getOldAge();
		String gender = OfferScreen.getGender();
		//send data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "offer"));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("startLocation", startLocation));
		nameValuePairs.add(new BasicNameValuePair("startTime", startTime));
		nameValuePairs.add(new BasicNameValuePair("arrivalDestination",  arrivalDestination));
		nameValuePairs.add(new BasicNameValuePair("preferredAgeStart",  ageStart));
		nameValuePairs.add(new BasicNameValuePair("preferredAgeEnd", ageEnd));
		nameValuePairs.add(new BasicNameValuePair("preferredSex", gender));
		DispatcherTask offer = new DispatcherTask("Share", nameValuePairs);
		offer.execute();
		
		timerFlag = true;
		//for updating location
		updateTimer = new Thread(){
			public void run(){
				while(timerFlag){
				try{
					sleep(10000); // every ten seconds
					updateOffer();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			}
		};
		updateTimer.start();
		}
		else{
			String username = LoginScreen.getLoginData()[0];
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("requestType", "removeOffer"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			DispatcherTask removeOffer = new DispatcherTask("Share", nameValuePairs);
			removeOffer.execute();
		}
	}
	
	public static double[] getStart(){
		return startLocation;
	}
	
	public static double[] getFinal(){
		
		return finalLocation;
	}
	
	
	private void submitRequestClicked() {
		int[] time = RequestScreen.getTime();
		String timeString = time[0] + ":" + time[1];

		//make request
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "request"));
        nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
        DispatcherTask request = new DispatcherTask("Share", nameValuePairs);
		request.execute();
	}
	
	private void startLocationClicked() {
		double[] pos = RequestScreen.getMarkedLocation();
		String location = "Lat = " + pos[0] + " Long = " + pos[1];
		startLocation = pos;
		RequestScreen.setStartLocation(pos);
	}
	
	private void arrivalLocationClicked() {
		double[] pos = RequestScreen.getMarkedLocation();
		String location = "Lat = " + pos[0] + " Long = " + pos[1];
		finalLocation = pos;
		RequestScreen.setArrivalLocation(pos);
	}
	
	private static void updateOffer(){
		double[] userLocation = OfferScreen.getCurrentLocation();
		//format data
		String username = LoginScreen.getLoginData()[0];
		String currentLocation = userLocation[0] + " " + userLocation[1];
		//send data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "updateOffer"));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("currentLocation", currentLocation));
		DispatcherTask update = new DispatcherTask("Share", nameValuePairs);
		update.execute();
	}
	
	public static void calcDistance(JSONObject offer) throws JSONException{
		  double[] reqStart = ShareController.getStart();
		  double reqStartLat = reqStart[0];
		  double reqStartLong = reqStart[1];
		  LatLng rStart = new LatLng(reqStartLat, reqStartLong);
		  
		  double[] reqEnd = ShareController.getFinal();
		  double reqEndLat = reqEnd[0];
		  double reqEndLong = reqEnd[1];
		  LatLng rEnd = new LatLng(reqEndLat, reqEndLong);
		  
		  String[] offerCur =  offer.getString("currentLocation").split(" ");
		  double offerCurLat = Double.parseDouble(offerCur[0]);
		  double offerCurLong = Double.parseDouble(offerCur[1]);
		  LatLng oCurrent = new LatLng(offerCurLat, offerCurLong);
		  
		  String[] offerEnd =  offer.getString("arrivalDestination").split(" ");
		  double offerEndLat = Double.parseDouble(offerEnd[0]);
		  double offerEndLong = Double.parseDouble(offerEnd[1]);
		  LatLng oEnd = new LatLng(offerEndLat, offerEndLong);
		  
		  Navigator pickupTrip = new Navigator(map, oCurrent, rStart, "ShareContext");
		  Navigator middleTrip = new Navigator(map, rStart, oEnd, "ShareContext");
		  Navigator dropoffTrip = new Navigator(map, oEnd, rEnd, "ShareContext");
		  
		  pickupTrip.findDirections(true);
		  middleTrip.findDirections(true);
		  dropoffTrip.findDirections(true);
		  
		  Navigator[] trips = new Navigator[3];
		  trips[0] = pickupTrip;
		  trips[1] = middleTrip;
		  trips[2] = dropoffTrip;
		  tripNavigators.add(trips);
	  }
	
	public static void httpResponse(String response) throws JSONException {
			JSONObject wholeObject = new JSONObject(response);
			String type = wholeObject.getString("type");
			String success = wholeObject.getString("success");
			if(type.equals("retrieveOffers")) retrieveOffersResponse(wholeObject);
			if(type.equals("updateOffer")) updateOfferResponse(wholeObject);
			if(type.equals("placeOffer")) placeOfferResponse(wholeObject);	
			if(type.equals("removeOffer")) removeOfferResponse(wholeObject);	
	}

	private static void removeOfferResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
			OfferScreen.submitSetText("Submit Offer");
			timerFlag = false;
			ScanCodeScreen.setCodeScanned(false);
			((Activity) shareContext).finish();
		}
		else{
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private static void placeOfferResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
			OfferScreen.submitSetText("Remove Offer");
			Intent intent = new Intent(shareContext, OfferListScreen.class);
	    	shareContext.startActivity(intent);
		}
		else{
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private static void updateOfferResponse(JSONObject wholeObject) throws JSONException {
		if(wholeObject.getString("success").equals("1")){
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
		else{
			Toast toast = Toast.makeText(shareContext, wholeObject.getString("message") , Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private static void retrieveOffersResponse(JSONObject wholeObject) throws JSONException{
		Log.d("offersList", "here");
		tripNavigators = new ArrayList<Navigator[]>();
		JSONObject input = wholeObject.getJSONObject("message");
		JSONObject[] offersList = new JSONObject[15];
		for(int i=1; i<15; i++){
			if(input.has(""+i)){
				offersList[i] = input.getJSONObject(""+i); //offer number
				calcDistance(offersList[i]);
			}
		}
		Log.d("offersList", offersList.toString());
		RequestListScreen.setOffers(offersList, map, tripNavigators);
		Intent intent = new Intent(shareContext, RequestListScreen.class);
    	shareContext.startActivity(intent);
	}

	
	

}
