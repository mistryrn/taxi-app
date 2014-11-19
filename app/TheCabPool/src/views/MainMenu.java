package views;

import com.groupten.thecabpool.R;

import controllers.MainController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {
	//declare buttons
	Button btnRegister;
	Button btnLogin;
	Button btnOffer;
	Button btnRequest;
	Button btnSettings;
	Button btnExit;
	
	//declare editable textview
	static TextView lblMessage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //initialize textview
        lblMessage = (TextView) findViewById(R.id.lblMessage);
        
        //initialize buttons
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnOffer = (Button) findViewById(R.id.btnOffer);
        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnExit = (Button) findViewById(R.id.btnExit);
        
        //initialize controller
        MainController btnController = new MainController(this);
        
        //add controller to each button
        btnRegister.setOnClickListener(btnController);
        btnLogin.setOnClickListener(btnController);
        btnOffer.setOnClickListener(btnController);
        btnRequest.setOnClickListener(btnController);
        btnSettings.setOnClickListener(btnController);
        btnExit.setOnClickListener(btnController);
    }

    
    public static void errorMessage(){
    	lblMessage.setText("Error");
    }

}
