package views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.groupten.thecabpool.R;

import controllers.SecurityController;

public class LoginScreen extends Activity{

	private static EditText txtUsername;
	private static EditText txtPassword;
	private static Button btnSubmit;
	private static TextView lblMessage;
	private static String username, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		username = "";
		password = "";
		
		txtUsername = (EditText) findViewById(R.id.txtLoginUsername);
		txtPassword = (EditText) findViewById(R.id.txtLoginPassword);
		btnSubmit = (Button) findViewById(R.id.btnLoginSubmit);
		lblMessage = (TextView) findViewById(R.id.lblLoginMessage);
		
		SecurityController controller = new SecurityController(this);
		
		btnSubmit.setOnClickListener(controller);
	}
	


	public static String getUsername(){
		return txtUsername.getText().toString();
	}
	
	public static String getPassword(){
		return txtPassword.getText().toString();
	}
	
	public static void setLoginData(String user, String pass){
		username = user;
		password = pass;
	}
	
	public static String[] getLoginData(){
		String data[] = {username, password};
		return data;
	}
	
	public void closeScreen(){
		finish();
	}
}