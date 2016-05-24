package com.anassaeed.lums;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anassaeed.lums.*;
public class Atwelve extends Activity{
	 Button buttonBack;

		@Override
	    protected void onCreate(Bundle savedInstanceState) {	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.atwelve);
	        buttonBack = (Button) findViewById(R.id.btnMainMenu);
	        buttonBack.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	
	                Intent intent  = new Intent(Atwelve.this, MainActivity.class);
	                try{
	                	startActivity(intent);
	                }
	                catch (Exception e){
	                	e.printStackTrace();
	                }
	            }
	         });
	         }
}
