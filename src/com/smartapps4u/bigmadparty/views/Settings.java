package com.smartapps4u.bigmadparty.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.util.GlobalConfig;

public class Settings extends Activity {

	ImageView btnOn_Off;
	TextView btnBMP;

	private SharedPreferences sharedprefs;
	private SharedPreferences.Editor sharedprefsedit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		init();
		if (sharedprefs.getBoolean("ONLINEMODE", true)) {
			// btnOnline.setChecked(true);
			btnOn_Off.setImageResource(R.drawable.on);
			GlobalConfig.ONLINE_MODE = true;
		} else {
			btnOn_Off.setImageResource(R.drawable.off);
			GlobalConfig.ONLINE_MODE = false;
		}
		/*
		 * btnSave.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub switch (radioGroup.getCheckedRadioButtonId()) { case
		 * R.id.rdbOnline: sharedprefsedit.putBoolean("ONLINEMODE", false);
		 * GlobalConfig.ONLINE_MODE = false; break; case R.id.rdbOffline:
		 * sharedprefsedit.putBoolean("ONLINEMODE", true);
		 * GlobalConfig.ONLINE_MODE = true; break; } sharedprefsedit.commit();
		 * Toast.makeText(getApplicationContext(), "Settings Saved.",
		 * Toast.LENGTH_LONG).show(); finish(); startActivity(new
		 * Intent(getApplicationContext(), UserMenu.class)); } });
		 */

		btnOn_Off.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GlobalConfig.ONLINE_MODE) {
					btnOn_Off.setImageResource(R.drawable.off);
					GlobalConfig.ONLINE_MODE = false;
				} else {
					btnOn_Off.setImageResource(R.drawable.on);
					GlobalConfig.ONLINE_MODE = true;
				}
			}
		});

		btnBMP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedprefsedit.putBoolean("ONLINEMODE",
						GlobalConfig.ONLINE_MODE);
				sharedprefsedit.commit();
				finish();
				startActivity(new Intent(getApplicationContext(),
						UserMenu.class));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void init() {

		btnOn_Off = (ImageView) findViewById(R.id.btnOn_Off);
		btnBMP = (TextView) findViewById(R.id.btnBMP);
		sharedprefs = getSharedPreferences("smartapps4u_bigmadparty", 0);
		sharedprefsedit = sharedprefs.edit();
	}
}
