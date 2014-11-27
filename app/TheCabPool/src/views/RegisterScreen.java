package views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.groupten.thecabpool.R;

import controllers.SecurityController;

public class RegisterScreen extends Activity{
	
	private static EditText txtUsername;
	private static EditText txtPassword;
	private static EditText txtDateOfBirth;
	private static EditText txtSex;
	private static EditText txtPhoneNumber;
	
	private Button btnSubmit;
	
	private static TextView lblMessage;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_screen);

		txtUsername = (EditText) findViewById(R.id.txtRegisterUsername);
		txtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
		txtDateOfBirth = (EditText) findViewById(R.id.txtRegisterDateOfBirth);
		txtSex = (EditText) findViewById(R.id.txtRegisterSex);
		txtPhoneNumber = (EditText) findViewById(R.id.txtRegisterPhoneNumber);
		
		btnSubmit = (Button) findViewById(R.id.btnRegisterSubmit);
		
		lblMessage = (TextView) findViewById(R.id.lblRegisterMessage);
		
		SecurityController controller = new SecurityController(this);
		
		btnSubmit.setOnClickListener(controller);
	}
	
	public static void displayError(String msg){
		lblMessage.setText(msg);
	}
	
	public static String getUsername(){
		return txtUsername.getText().toString();
	}

	
	public static String getPassword(){
		return txtPassword.getText().toString();
	}

	
	public static String getDateOfBirth(){
		return txtDateOfBirth.getText().toString();
	}

	
	public static String getSex(){
		return txtSex.getText().toString();
	}

	
	public static String getPhoneNumber(){
		return txtPhoneNumber.getText().toString();
	}
	
	public void closeScreen(){
		finish();
	}

	
	
}
