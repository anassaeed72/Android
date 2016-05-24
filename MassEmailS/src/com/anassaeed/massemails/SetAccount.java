package com.anassaeed.massemails;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SetAccount extends Activity {
	Button btnSetAccount;
	EditText username;
	EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setaccount);
        btnSetAccount = (Button) findViewById(R.id.btnSetAccount);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        
        btnSetAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String usernameString = username.getText().toString();
				String passwordString = password.getText().toString();
				Mail mail = new Mail();
				mail.setUsername(usernameString);
				mail.setPassword(passwordString);
				SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
            	shfEditorObject.putString(MainActivity.usernameDB,usernameString);
            	shfEditorObject.putString(MainActivity.passwordDB,passwordString);
            	shfEditorObject.commit();
            	Toast.makeText(SetAccount.this, "Account Info Saved", Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(SetAccount.this, MainActivity.class);
            	startActivity(intent);
			}
		});
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
}
