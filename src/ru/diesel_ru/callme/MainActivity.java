package ru.diesel_ru.callme;

import ru.diesel_ru.callme.Abaut;
import ru.diesel_ru.callme.PrefActivity;
import ru.diesel_ru.callme.R;
import ru.diesel_ru.callme.FavContList;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends Activity {

	protected static final int PICK_RESULT = 0;
	static final private int PHONE_NUMBER = 3;
	
	String strOprator = "";
	SharedPreferences sp;
	
	ImageButton buttonSelectContact;
	ImageButton buttonSelectFavoritesContact;
	ImageButton buttonSend;
	
	TextView txtPhoneNumber;
	
	AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		strOprator = sp.getString("defaultOperator","0");
        
		txtPhoneNumber = (TextView) findViewById(R.id.etPhone);
		buttonSelectContact = (ImageButton) findViewById(R.id.btnContacts);
        buttonSelectFavoritesContact = (ImageButton) findViewById(R.id.btnFContacts);
        //buttonSend = (ImageButton) findViewById(R.id.btnSend);
        
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
			}else if (strOprator.compareToIgnoreCase("0") == 0) {
				Toast.makeText(getApplicationContext(), "�������� ��������� �����!", Toast.LENGTH_SHORT).show();
				return;
			}

			startActivity(_intent);
			finish();
        }
        
      //�������� adView ca-app-pub-9670568035952143/5674883316
        //adView = new AdView(this, AdSize.BANNER, "a1510fa3b8c4d5e");
        adView = new AdView(this, AdSize.BANNER, "ca-app-pub-9670568035952143/5674883316");
        										    
        // ����� � LinearLayout (��������������, ��� ��� ��������
        // ������� android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.admobLayout);

        // ���������� adView
        layout.addView(adView);

        // ������������� ������ ������� �� �������� ������ � �����������
        adView.loadAd(new AdRequest());
        
        
        // ���������� ������ ��������
        buttonSelectContact.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// ����� ������ ��������� ������� (��� ��������)
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(pickIntent, PICK_RESULT);
        	}
        });
        
        // ���������� ������ ���������� ��������
        buttonSelectFavoritesContact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FavContList.class);
			    startActivityForResult(intent, PHONE_NUMBER);
        	}
        });
        
        // ���������� ������� �� ������ ���������
        buttonSend.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if (txtPhoneNumber.length() < 1)
        		{
        			Toast.makeText(getApplicationContext(), "������� ����� ��������!", Toast.LENGTH_SHORT).show();
        			return;
        		}
        		
        		sendUSSD(txtPhoneNumber.getText().toString());
        	}
        });
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	      MenuItem mi = menu.add(0, 1, 0, "���������");
	      mi.setIntent(new Intent(this, PrefActivity.class));
	      mi = menu.add(0, 1, 0, "� ���������");
	      mi.setIntent(new Intent(this, Abaut.class));
	      
	      return super.onCreateOptionsMenu(menu);
    }

    // ��������� ������ ��������
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// �������� ����� �� ��������� ���������
    	if (requestCode == PHONE_NUMBER) {
    		if (resultCode == RESULT_OK) {
    			String thiefname = data.getStringExtra(FavContList.PHONE_NUMBER);
    			txtPhoneNumber.setText(thiefname.replace("+7", "8").replace("-", "").replace(" ", ""));
    		}else {
    			txtPhoneNumber.setText(""); // ������� �����
    		}
    	}
    	// �������� ����� �� �������� ���������
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                	c = getContentResolver().query(uri, new String[]{ 
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE},
                        null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        showSelectedNumber(type, number);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }

    // ������������ �������� ������ �������� 
    private void showSelectedNumber(int type, String number) {
    	txtPhoneNumber.setText(number.replace("+7", "8").replace("-", "").replace(" ", ""));      
    }
    
    private void sendUSSD(String strNum){
    	
    	strNum = strNum.replace("smsto:", "").replace("sms:", "").replace("+7", "8").replace("-", "").replace(" ", "");
    	
    	Intent _intent = new Intent(Intent.ACTION_CALL);
    	
    	if (strOprator.compareToIgnoreCase("1") == 0) {
    		//"MTS"
			_intent.setData(Uri.fromParts("tel", "*110*" + strNum + "#", "#"));				
    	}else if (strOprator.compareToIgnoreCase("2") == 0) {				
    		//"Beeline"
			_intent.setData(Uri.fromParts("tel", "*144#" + strNum + "#", "#"));			
    	}else if (strOprator.compareToIgnoreCase("3") == 0) {
    		//"Megafon"
			_intent.setData(Uri.fromParts("tel", "*144*" + strNum + "#", "#"));
    	}else if (strOprator.compareToIgnoreCase("4") == 0) {
    		//"Tele2"
			_intent.setData(Uri.fromParts("tel", "*118*" + strNum + "#", "#"));
    	}else if (strOprator.compareToIgnoreCase("5") == 0) {
    		//"BWC"
			_intent.setData(Uri.fromParts("tel", "*141*" + strNum + "#", "#"));
    	}else if (strOprator.compareToIgnoreCase("6") == 0) {
    		//"Other"
    		_intent.setData(Uri.fromParts("tel", sp.getString("otherStartUSSD","") + strNum + sp.getString("otherEndUSSD",""), "#"));
		}else if (strOprator.compareToIgnoreCase("0") == 0) {
			Toast.makeText(getApplicationContext(), "�������� ��������� �����!", Toast.LENGTH_SHORT).show();
			return;
		}

		startActivity(_intent);
    }
}
