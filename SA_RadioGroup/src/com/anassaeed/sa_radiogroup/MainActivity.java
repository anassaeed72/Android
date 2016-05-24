package com.anassaeed.sa_radiogroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	 private RadioGroup radioSexGroup;
	  private RadioButton radioSexButton;
	  private Button btnDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();

	  }

	  public void addListenerOnButton() {

		radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
		btnDisplay = (Button) findViewById(R.id.btnDisplay);

		btnDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			        // get selected radio button from radioGroup
				int selectedId = radioSexGroup.getCheckedRadioButtonId();

				// find the radiobutton by returned id
			        radioSexButton = (RadioButton) findViewById(selectedId);

				Toast.makeText(MainActivity.this,
					radioSexButton.getText(), Toast.LENGTH_SHORT).show();

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
