package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.groupten.thecabpool.R;

import views.LoginScreen;
import views.MainMenu;
import views.OfferScreen;
import views.RegisterScreen;
import views.RequestListScreen;
import views.RequestScreen;
import views.ScanCodeScreen;
import views.SettingsScreen;
import AsyncTasks.ShareTask;
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
		Toast toast = Toast.makeText(shareContext, location , Toast.LENGTH_SHORT);
		toast.show();
		finalLocation = pos;
		OfferScreen.setArrivalLocation(pos);
	}
	private void submitOfferClicked() {
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
		ShareTask offer = new ShareTask("Offer", nameValuePairs);
		offer.execute();
		
		//for updating location
		Thread timer = new Thread(){
			public void run(){
				while(true){
				try{
					sleep(10000); // every ten seconds
					updateOffer();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			}
		};
		timer.start();
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
        ShareTask request = new ShareTask("Request", nameValuePairs);
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
		ShareTask update = new ShareTask("Update", nameValuePairs);
		update.execute();
	}
	
	public static void httpResponse(String response) throws JSONException {
		if(response.equals("Offer Successful")){
			Toast toast = Toast.makeText(shareContext, "Offer Successfully Placed" , Toast.LENGTH_SHORT);
			toast.show();
			updateOffer();
		}
		else if(response.equals("Update Successful")){
			Toast toast = Toast.makeText(shareContext, "Update Successful" , Toast.LENGTH_SHORT);
			toast.show();
		}
		else if(response.charAt(0) == '{'){//JSON response			 
			JSONObject reader = new JSONObject(response);
			JSONObject[] offersList = new JSONObject[15];
			for(int i=1; i<15; i++){
				if(reader.has(""+i))
					offersList[i] = reader.getJSONObject(""+i); //offer number
			}
			
			
			

			RequestListScreen.setOffers(offersList);
			
			Intent intent = new Intent(shareContext, RequestListScreen.class);
	    	shareContext.startActivity(intent);
				
			
			
		}
	}


	
	

}
