package views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.groupten.thecabpool.R;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.splash);
		final int splashSeconds = 0; //make enable than zero to add splash screen
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(splashSeconds*1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMain = new Intent("android.intent.action.MENU");
					startActivity(openMain);
					finish();
				}
			}
		};
		timer.start();
	}

}
