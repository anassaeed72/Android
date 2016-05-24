package com.anassaeed.contactspics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.Intents;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	public static final String tag = "ContactsPics";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = new Intent(Intents.Insert.ACTION);
		ArrayList<String> tes = null;
		try{
			 tes = getAllConactIds();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( tes == null ) {
			Log.i(tag, "arraylist is null");
		}
		for ( String x: tes) {
			Log.i(tag, x);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 /**
     * @param name name of the contact
     * @param number mobile phone number of contact
     * @param email work email address of contact
     * @param ContactId id of the contact which you want to update
     * @return true if contact is updated successfully<br/>
     *         false if contact is not updated <br/>
     *         false if phone number contains any characters(It should contain only digits)<br/>
     *         false if email Address is invalid <br/><br/>
     *         
     *  You can pass any one among the 3 parameters to update a contact.Passing all three parameters as <b>null</b> will not update the contact        
     *  <br/><br/><b>Note: </b>This method requires permission <b>android.permission.WRITE_CONTACTS</b><br/>
     */

    public boolean updateContact(String name, String number, String email,String ContactId) 
    {
        boolean success = true;
        String phnumexp = "^[0-9]*$";

        try
        {
              name = name.trim();
              email = email.trim();
              number = number.trim();

        if(name.equals("")&&number.equals("")&&email.equals(""))
         {
            success = false;
         }
        else if((!number.equals(""))&& (!match(number,phnumexp)) )
         {
            success = false;
         }
        else if( (!email.equals("")) && (!isEmailValid(email)) )
        {
            success = false;
        }
        else 
        {
            ContextWrapper activity = (ContextWrapper) getBaseContext();
			ContentResolver contentResolver  = activity.getContentResolver();

            String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"; 

            String[] emailParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}; 
            String[] nameParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}; 
            String[] numberParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE}; 

            ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

         if(!email.equals(""))  
         {
             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,emailParams)
                  .withValue(Email.DATA, email)
                  .build());
         }

         if(!name.equals(""))
         {
             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,nameParams)
                  .withValue(StructuredName.DISPLAY_NAME, name)
                  .build());
         }

         if(!number.equals(""))
         {

             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,numberParams)
                  .withValue(Phone.NUMBER, number)
                  .build());
         }
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
         }
        }
        catch (Exception e) 
        {
         e.printStackTrace();
         success = false;
        }
        return success;
    }



	// To get COntact Ids of all contact use the below method 
	
	/**
	 * @return arraylist containing id's  of all contacts <br/> 
	 *         empty arraylist if no contacts exist <br/><br/>
	 * <b>Note: </b>This method requires permission <b>android.permission.READ_CONTACTS</b>
	 */
	public ArrayList<String> getAllConactIds()
	{
	    ArrayList<String> contactList = new ArrayList<String>();
	     Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, "display_name ASC");
	
	        if (cursor != null) 
	        {
	            if (cursor.moveToFirst()) 
	            {
	               do
	               {
	                   int _id = cursor.getInt(cursor.getColumnIndex("_id"));
	                   contactList.add(String.valueOf(_id));
//	                   cursor.getString(cursor.getColumnIndex(Contacts.P.Contacts.P))
	
	               }
	               while(cursor.moveToNext());
	            }
	        }
	
	    return contactList;
	}
	public InputStream openPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
	     Cursor cursor = getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return new ByteArrayInputStream(data);
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }
	public InputStream openDisplayPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri displayPhotoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO);
	     try {
	         AssetFileDescriptor fd = getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
	         return fd.createInputStream();
	     } catch (IOException e) {
	         return null;
	     }
	 }
	
	
	private boolean isEmailValid(String email) 
	{
		String emailAddress = email.toString().trim();
		if (emailAddress == null)
		    return false;
		else if (emailAddress.equals(""))
		    return false;
		else if (emailAddress.length() <= 6)
		    return false;
		else {
		    String expression = "^[a-z][a-z|0-9|]*([_][a-z|0-9]+)*([.][a-z|0-9]+([_][a-z|0-9]+)*)?@[a-z][a-z|0-9|]*\\.([a-z][a-z|0-9]*(\\.[a-z][a-z|0-9]*)?)$";
		    CharSequence inputStr = emailAddress;
		    Pattern pattern = Pattern.compile(expression,
		            Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher(inputStr);
		    if (matcher.matches())
		        return true;
		    else
		        return false;
		}
	}
	
	private boolean match(String stringToCompare,String regularExpression)
	{
		boolean success = false;
		Pattern pattern = Pattern.compile(regularExpression);
		Matcher matcher = pattern.matcher(stringToCompare);
		if(matcher.matches())
		    success =true;
		return success;
	}
}
