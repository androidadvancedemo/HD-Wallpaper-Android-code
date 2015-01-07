package com.nicksapp.wallpapers;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizLogLevel;
import com.startapp.android.publish.StartAppAd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.nicksapp.wallpapers.util.PrefManager;

public class SettingsActivity extends Activity {
	private PrefManager pref;
	private TextView txtGoogleUsername, txtNoOfGridColumns, txtGalleryName;
	private Button btnSave;
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		StartAppAd.init(SettingsActivity.this, "103205820", "203170552");

		StartAppAd.showSlider(this);

		AdBuddiz.setLogLevel(AdBuddizLogLevel.Info); // log level
		AdBuddiz.setPublisherKey("e14546c7-3dd1-475c-a0d3-f16eedd213d5"); // replace
																			// with
																			// your
																			// app
																			// publisher
																			// key

		AdBuddiz.cacheAds(SettingsActivity.this);
		startAppAd.loadAd(); // load the next ad
		startAppAd.showAd(); // show the ad
		startAppAd.loadAd(); // load the next ad
		AdBuddiz.showAd(SettingsActivity.this);
		txtGoogleUsername = (TextView) findViewById(R.id.txtGoogleUsername);
		txtNoOfGridColumns = (TextView) findViewById(R.id.txtNoOfColumns);
		txtGalleryName = (TextView) findViewById(R.id.txtGalleryName);
		btnSave = (Button) findViewById(R.id.btnSave);

		pref = new PrefManager(getApplicationContext());

		// Display edittext values stored in shared preferences
		// Google username
		txtGoogleUsername.setText(pref.getGoogleUserName());

		// Number of grid columns
		txtNoOfGridColumns.setText(String.valueOf(pref.getNoOfGridColumns()));

		// Gallery name
		txtGalleryName.setText(pref.getGalleryName());

		// Save settings button click listener
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Validating the data before saving to shared preferences
				// validate google username
				String googleUsername = txtGoogleUsername.getText().toString()
						.trim();
				if (googleUsername.length() == 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.toast_enter_google_username),
							Toast.LENGTH_LONG).show();
					return;
				}

				// validate number of grid columns
				String no_of_columns = txtNoOfGridColumns.getText().toString()
						.trim();
				if (no_of_columns.length() == 0 || !isInteger(no_of_columns)) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.toast_enter_valid_grid_columns),
							Toast.LENGTH_LONG).show();
					return;
				}

				// validate gallery name
				String galleryName = txtGalleryName.getText().toString().trim();
				if (galleryName.length() == 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.toast_enter_gallery_name),
							Toast.LENGTH_LONG).show();
					return;
				}

				// Check for setting changes
				if (!googleUsername.equalsIgnoreCase(pref.getGoogleUserName())
						|| !no_of_columns.equalsIgnoreCase(String.valueOf(pref
								.getNoOfGridColumns()))
						|| !galleryName.equalsIgnoreCase(pref.getGalleryName())) {
					// User changed the settings
					// save the changes and launch SplashScreen to initialize
					// the app again
					pref.setGoogleUsername(googleUsername);
					pref.setNoOfGridColumns(Integer.parseInt(no_of_columns));
					pref.setGalleryName(galleryName);

					// start the app from SplashScreen
					Intent i = new Intent(SettingsActivity.this,
							SplashActivity.class);
					// Clear all the previous activities
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
				} else {
					// user not modified any values in the form
					// skip saving to shared preferences
					// just go back to previous activity
					onBackPressed();
				}

			}
		});

	}

	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
