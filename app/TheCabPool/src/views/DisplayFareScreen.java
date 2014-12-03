package views;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.groupten.thecabpool.R;

import controllers.ShareController;

import AsyncTasks.DispatcherTask;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayFareScreen extends Activity {
	
	private Button btnSubmit;
	private static TextView lblFare;
	private static EditText txtRating;
	private static String cost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_fare_screen);
		
		btnSubmit = (Button) findViewById(R.id.btnDisplayFareScreenSubmit);
		lblFare = (TextView) findViewById(R.id.lblFareOwed);
		txtRating = (EditText) findViewById(R.id.txtRating);
		
		ShareController controller = new ShareController(this);
		btnSubmit.setOnClickListener(controller);
		
		setFare("Your total fare is " + cost + ".");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("requestType", "clearInTaxi"));
		nameValuePairs.add(new BasicNameValuePair("username", LoginScreen.getLoginData()[0]));
		DispatcherTask check = new DispatcherTask("Share", nameValuePairs);
		check.execute();
		
		
	}
	
	public static void setFare(String msg){
		lblFare.setText(msg);
	}
	
	public static String getRating(){
		return txtRating.getText().toString();
	}
	
	public static void setCost(String s){
		cost = s;
	}

}
