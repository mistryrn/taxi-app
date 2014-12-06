package com.tyczj.mapnavigator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Legs {
	
	private ArrayList<Steps> steps;
	private String totalDistance;
	private String totalDuration;
	
	public Legs(JSONObject leg){
		steps = new ArrayList<Steps>();
		parseSteps(leg);
	}
	
	public ArrayList<Steps> getSteps(){
		return steps;
	}
	
	private void parseSteps(JSONObject leg){
		try{
			if(!leg.isNull("steps")){
				JSONArray step = leg.getJSONArray("steps");
				
				for(int i=0; i<step.length();i++){
					JSONObject obj = step.getJSONObject(i);
					steps.add(new Steps(obj));
				}
			}
			
			if(!leg.isNull("distance")){
				JSONObject obj = leg.getJSONObject("distance");
				totalDistance = obj.getString("value");
			}
			
			if(!leg.isNull("duration")){
				JSONObject obj = leg.getJSONObject("duration");
				totalDuration = obj.getString("value");
			}
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public double getLegDistance(){
		//Log.d("Leg", totalDistance);
		return Double.parseDouble(totalDistance);
	}
	
	public double getLegDuration(){
		return Double.parseDouble(totalDuration);
	}

}
