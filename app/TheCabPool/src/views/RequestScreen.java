package views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.groupten.thecabpool.R;

public class RequestScreen extends Activity{
	
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_screen);
		RequestScreen.context = this;
	}

}
