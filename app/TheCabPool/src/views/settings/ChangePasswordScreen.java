package views.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.groupten.thecabpool.R;

import controllers.SettingsController;

public class ChangePasswordScreen extends Activity{

	static EditText txtPasswordOld;
	static EditText txtPasswordNew;
	static EditText txtPasswordConfirm;
	
	Button btnSubmit;
	
	TextView lblMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password_screen);
		
		txtPasswordOld = (EditText) findViewById(R.id.txtPasswordOld);
		txtPasswordNew = (EditText) findViewById(R.id.txtPasswordNew);
		txtPasswordConfirm = (EditText) findViewById(R.id.txtPasswordConfirm);
		
		btnSubmit = (Button) findViewById(R.id.btnSubmitPassword);
		
		lblMessage = (TextView) findViewById(R.id.lblMessagePasswordScreen);
		
		SettingsController controller = new SettingsController(this);
		
		btnSubmit.setOnClickListener(controller);
	}
	
	public void displayMessage(String msg){
		lblMessage.setText(msg);
	}
	
	public static String getPasswordOld(){
		return txtPasswordOld.getText().toString();
	}

	public static String getPasswordNew(){
		return txtPasswordNew.getText().toString();
	}
	
	public static String getPasswordConfirm(){
		return txtPasswordConfirm.getText().toString();
	}
}