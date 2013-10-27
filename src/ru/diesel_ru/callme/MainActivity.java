package ru.diesel_ru.callme;

import ru.diesel_ru.callme.Abaut;
import ru.diesel_ru.callme.PrefActivity;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	//TextView tv;
	String strOprator = "";
	SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		strOprator = sp.getString("defaultOperator","1");
        
        Intent localIntent = getIntent();
        if (localIntent.getAction().contains("android.intent.action.SENDTO")){
        	String strNum = "";
        	strNum = Uri.decode(localIntent.getData().toString()).replace("smsto:", "").replace("sms:", "").replace("+7", "8").replace("-", "").replace(" ", "");
        	//tv.setText(strNum);

        	Intent _intent = new Intent(Intent.ACTION_CALL);
        	
        	if (strOprator.compareToIgnoreCase("1") == 0) {
        		//"MTS":
				_intent.setData(Uri.fromParts("tel", "*110*" + strNum + "#", "#"));				
        	}else if (strOprator.compareToIgnoreCase("2") == 0) {				
        		//"Beeline":
				_intent.setData(Uri.fromParts("tel", "*144#" + strNum + "#", "#"));			
        	}else if (strOprator.compareToIgnoreCase("3") == 0) {
        		//"Megafon":
				_intent.setData(Uri.fromParts("tel", "*144*" + strNum + "#", "#"));
        	}else if (strOprator.compareToIgnoreCase("4") == 0) {
        		//"Tele2":
				_intent.setData(Uri.fromParts("tel", "*118*" + strNum + "#", "#"));
        	}else if (strOprator.compareToIgnoreCase("5") == 0) {
        		//"BWC":
				_intent.setData(Uri.fromParts("tel", "*141*" + strNum + "#", "#"));
        	}else if (strOprator.compareToIgnoreCase("6") == 0) {
        		//"Other":
        		_intent.setData(Uri.fromParts("tel", sp.getString("otherStartUSSD","") + strNum + sp.getString("otherEndUSSD",""), "#"));
			}

			startActivity(_intent);
			finish();
        }
        
        
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        //menu.add("menu1");
        //return true;
	      MenuItem mi = menu.add(0, 1, 0, "Настройки");
	      mi.setIntent(new Intent(this, PrefActivity.class));
	      mi = menu.add(0, 1, 0, "О программе");
	      mi.setIntent(new Intent(this, Abaut.class));
	      
	      return super.onCreateOptionsMenu(menu);
    }

}
