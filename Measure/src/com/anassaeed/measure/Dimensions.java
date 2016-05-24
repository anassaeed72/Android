package com.anassaeed.measure;

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

public class Dimensions extends Activity {
	Button button;
	EditText edittextLength;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dimensions);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edittextLength = (EditText) findViewById(R.id.length);
				String lengthTemp = edittextLength.getText().toString();
            	
				SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
            	shfEditorObject.putString(MainActivity.length,lengthTemp);
            	shfEditorObject.commit();
            	
            	Toast.makeText(Dimensions.this, "Dimensions Saved ", Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(Dimensions.this, MainActivity.class);
            	startActivity(intent);
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            		
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
