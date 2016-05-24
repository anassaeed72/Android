package com.anassaeed.test;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	public static final String tag = "test";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		/*PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "test message do not care";

	        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("com.whatsapp");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(Intent.createChooser(waIntent, "Share with"));

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
	   */
		try{
//			sendWhatsAppMessageTo("19174352360@s.whatsapp.net");
			shareWhatsApp(this,"Salman Saeed American");
		}catch (Exception e ) {
			e.printStackTrace();
		}
		Log.i(tag,"function called");

	}

	public void sendWhatsAppMessageTo(String whatsappid) {
		Cursor c = null;
		try{
		 c = getBaseContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
		        new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + "=?",
		        new String[] { whatsappid }, null);
		c.moveToFirst();
		}catch (Exception e ) {
			e.printStackTrace();
			return;
		}
		
		Intent whatsapp = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
//		c.close();
		Log.i(tag, "the cursor is "+c.getString(0));

		 if (whatsapp != null) {
//			
		     String text = "test message do not care";
		     PackageManager pm=getPackageManager();
		        try {
					PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
				} catch (NameNotFoundException e) {
//					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        //Check if package exists or not. If not then code 
//		        //in catch block will be called
		        whatsapp.setPackage("com.whatsapp");
//
		        whatsapp.putExtra(Intent.EXTRA_TEXT, text);
//		        whatsapp.setType("text/plain");
		            startActivity(whatsapp);      

		} else {
		        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
		                .show();
		//download for example after dialog
		                Uri uri = Uri.parse("market://details?id=com.whatsapp");
		                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		    }

		}
	public static void shareWhatsApp(Activity appActivity, String texto) {

	    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
	    sendIntent.setType("text/plain");
	    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, texto);

	    PackageManager pm = appActivity.getApplicationContext().getPackageManager();
	    final List<ResolveInfo> matches = pm.queryIntentActivities(sendIntent, 0);
	    boolean temWhatsApp = false;
	    for (final ResolveInfo info : matches) {
	      if (info.activityInfo.packageName.startsWith("com.whatsapp"))  {
	          final ComponentName name = new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);
	          sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	          sendIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
	          sendIntent.setComponent(name);
	          temWhatsApp = true;
	          break;
	      }
	    }               

	    if(temWhatsApp) {
	        //abre whatsapp
	        appActivity.startActivity(sendIntent);
	    } else {
	        //alerta - você deve ter o whatsapp instalado
	        Toast.makeText(appActivity, " not sent", Toast.LENGTH_SHORT).show();
	    }
	}
}