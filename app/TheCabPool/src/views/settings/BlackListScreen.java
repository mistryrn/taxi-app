package views.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.groupten.thecabpool.R;

import controllers.SettingsController;

public class BlackListScreen extends Activity{
	
	ListView lstBlacklist;
	EditText txtUsername;
	Button btnAdd;
	Button btnRemove;
	TextView lblMessage;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist_screen);
		
		lstBlacklist = (ListView) findViewById(R.id.lstBlacklist);
		txtUsername = (EditText) findViewById(R.id.txtBlacklistUsername);
		btnAdd = (Button) findViewById(R.id.btnBlacklistAdd);
		btnRemove = (Button) findViewById(R.id.btnBlacklistRemove);
		lblMessage = (TextView) findViewById(R.id.lblBlacklistMessage);
		
		SettingsController controller = new SettingsController(this);
		
		btnAdd.setOnClickListener(controller);
		btnRemove.setOnClickListener(controller);
	}
	
	public String getUsername(){
		return txtUsername.getText().toString();
	}
	
	public void displayMessage(String msg){
		lblMessage.setText(msg);
	}

}