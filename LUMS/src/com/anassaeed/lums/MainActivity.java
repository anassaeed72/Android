package com.anassaeed.lums;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	   Button aone;
	   Button atwo;
	   Button athree;
	   Button afour;
	   Button afive;
	   Button asix;
	   Button aseven;
	   Button aeight;
	   Button anine;
	   Button aten;
	   Button aeleven;
	   Button atwelve;
	   Button athirteen;
	   Button afourteen;
	   Button afifteen;
	   Button asixteen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aone = (Button) findViewById(R.id.btnAone);
        atwo = (Button) findViewById(R.id.btnAtwo);
        athree = (Button) findViewById(R.id.btnAthree);
        afour = (Button) findViewById(R.id.btnAfour);
        afive = (Button) findViewById(R.id.btnAfive);
        asix = (Button) findViewById(R.id.btnAsix);
        aseven = (Button) findViewById(R.id.btnAseven);
        aeight = (Button) findViewById(R.id.btnAeight);
        anine = (Button) findViewById(R.id.btnAnine);
        aten = (Button) findViewById(R.id.btnAten);
        aeleven = (Button) findViewById(R.id.btnAeleven);
        atwelve = (Button) findViewById(R.id.btnAtwelve);
        athirteen = (Button) findViewById(R.id.btnAthirteen);
        afourteen = (Button) findViewById(R.id.btnAfourteen);
        afifteen = (Button) findViewById(R.id.btnAfifteen);
        asixteen = (Button) findViewById(R.id.btnAsixteen);
        
        aone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                Intent intent  = new Intent(MainActivity.this, Aone.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        atwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                Intent intent  = new Intent(MainActivity.this, Atwo.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        athree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               
                Intent intent  = new Intent(MainActivity.this, Athree.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        afour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            
                Intent intent  = new Intent(MainActivity.this, Afour.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        afive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                Intent intent  = new Intent(MainActivity.this, Afive.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        asix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               
                Intent intent  = new Intent(MainActivity.this, Asix.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        aseven.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                Intent intent  = new Intent(MainActivity.this, Aseven.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        aeight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                Intent intent  = new Intent(MainActivity.this, Aeight.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        anine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                             Intent intent  = new Intent(MainActivity.this, Anine.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        aten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               
                Intent intent  = new Intent(MainActivity.this, Aten.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        aeleven.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, Aeleven.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        atwelve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             
                Intent intent  = new Intent(MainActivity.this, Atwelve.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        athirteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, Athirteen.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        afourteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, Afourteen.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        afifteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              
                Intent intent  = new Intent(MainActivity.this, Afifteen.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
            }
         });
        
        asixteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              
                Intent intent  = new Intent(MainActivity.this, Asixteen.class);
                try{
                	startActivity(intent);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
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
