package com.anassaeed.sapromptuserdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	Button button;
	EditText result;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// components from main.xml
				button = (Button) findViewById(R.id.buttonPrompt);
				result = (EditText) findViewById(R.id.editTextResult);

				// add button listener
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						// get prompts.xml view
						LayoutInflater li = LayoutInflater.from(context);
						View promptsView = li.inflate(R.layout.prompts, null);

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set prompts.xml to alertdialog builder
						alertDialogBuilder.setView(promptsView);

						final EditText userInput = (EditText) promptsView
								.findViewById(R.id.editTextDialogUserInput);

						// set dialog message
						alertDialogBuilder
							.setCancelable(false)
							.setPositiveButton("OK",
							  new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog,int id) {
								// get user input and set it to result
								// edit text
								result.setText(userInput.getText());
							    }
							  })
							.setNegativeButton("Cancel",
							  new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							    }
							  });

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

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
