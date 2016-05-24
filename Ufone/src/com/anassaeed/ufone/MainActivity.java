package com.anassaeed.ufone;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

	private Button button;
	private EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edittext = (EditText) findViewById(R.id.edittext);
				String input = edittext.getText().toString();
				if ( input.length() < 14 ) {
					Toast.makeText(getBaseContext(), "Entered code is less then 14 digit", Toast.LENGTH_LONG).show();
					return;
				} else if ( input.length() > 14 ) {
					Toast.makeText(getBaseContext(), "Entered code is greater then 14 digit", Toast.LENGTH_LONG).show();
					return;
				}
				String ussdCode = "*" + "123*" + input+"%23";
				startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));
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
