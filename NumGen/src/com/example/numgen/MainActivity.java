package com.example.numgen;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
	String series;
	char single(int in){
	    if (in == 0) {
	        return '0';
	    }
	    if (in == 1) {
	        return '1';
	    }
	    if (in == 2) {
	        return '2';
	    }
	    if (in == 3) {
	        return '3';
	    }
	    if (in == 4) {
	        return '4';
	    }
	    if (in == 5) {
	        return '5';
	    }
	    if (in == 6) {
	        return '6';
	    }
	    if (in == 7) {
	        return '7';
	    }
	    if (in == 8) {
	        return '8';
	    }
	    return '9';
	}
	int singlely(char in){

	    if (in =='0') {
	        return 0;
	    }
	    if (in =='1') {
	        return 1;
	    }
	    if (in =='2') {
	        return 2;
	    }
	    if (in =='3') {
	        return 3;
	    }
	    if (in =='4') {
	        return 4;
	    }
	    if (in =='5') {
	        return 5;
	    }
	    if (in =='6') {
	        return 6;
	    }
	    if (in =='7') {
	        return 7;
	    }
	    if (in =='8') {
	        return 8;
	    }
	    return 9;
	}
	int string_int(String input){
	    int out = 0;
	    input = reverser(input);
	    int num = 1;
	    for ( char x: input){
	        out = out + singlely(x)*num;
	        num = num*10;
	    }
	    return out;
	}

	String reverser(String in){
	    String output = "";
	    for (int i = in.length()-1; i>=0; i--) {
	        output = output + in.charAt(i);
	    }
	    return output;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
