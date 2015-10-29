package com.smartapps4u.bigmadparty.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.asynctasks.SyncProcessedTickets;
import com.smartapps4u.bigmadparty.databases.DatabaseHelper;
import com.smartapps4u.bigmadparty.objects.XMLCallback;
import com.smartapps4u.bigmadparty.util.GlobalConfig;

public class UserMenu extends Activity {
	TextView btnEvents, btnSettings, btnSycnTickets, btnLogout;
	private SharedPreferences sharedprefs;
	private SharedPreferences.Editor sharedprefsedit;
	Context mContext;
	TextView txtLastSync;
	SyncProcessedTickets synctask;
	SQLiteDatabase db = null;
	DatabaseHelper dbhelper;
	Cursor cursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_menu);
		init();
		mContext = this;
		dbhelper = new DatabaseHelper(this);
		db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		btnEvents.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(),
						EventList.class));
			}
		});
		btnSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(),
						Settings.class));
			}
		});
		btnSycnTickets.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String ticketIDs = getTicketIDs();
				synctask = new SyncProcessedTickets(mContext,
						GlobalConfig.USER_ID, GlobalConfig.PIN, ticketIDs,
						new XMLCallback() {

							@Override
							public void run(String result) {
								// TODO Auto-generated method stub
								if (result.equals("true")) {

									Toast.makeText(getApplicationContext(),
											"Sync Completed.",
											Toast.LENGTH_LONG).show();
									txtLastSync.setText("Last Sync: "
											+ getcurrentDateandTime());
									sharedprefsedit.putString("LASTSYNC",
											getcurrentDateandTime());
									sharedprefsedit.commit();
								} else {
									Toast.makeText(getApplicationContext(),
											"Error while trying to Sync.",
											Toast.LENGTH_LONG).show();
								}
							}
						});
				synctask.execute(GlobalConfig.PROCESSTICKETS_URL);
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				clearallsessionData();
				startActivity(new Intent(getApplicationContext(),
						LoginActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_menu, menu);

		return true;

	}

	private void init() {
		btnEvents = (TextView) findViewById(R.id.btnEvents);
		btnSettings = (TextView) findViewById(R.id.btnSettings);
		btnSycnTickets = (TextView) findViewById(R.id.btnSyncProcessed);
		btnLogout = (TextView) findViewById(R.id.btnLogout);
		txtLastSync = (TextView) findViewById(R.id.txtlastsync);
		sharedprefs = getSharedPreferences("smartapps4u_bigmadparty", 0);
		sharedprefsedit = sharedprefs.edit();
		GlobalConfig.ONLINE_MODE = sharedprefs.getBoolean("ONLINEMODE", true);
		txtLastSync.setText("Last Sync: "
				+ sharedprefs.getString("LASTSYNC", "Never"));
	}

	public String getTicketIDs() {

		String temp = "";
		cursor = null;
		cursor = db.rawQuery("select * from "
				+ DatabaseHelper.TABLE_TICKETINSTANCE + " where "
				+ DatabaseHelper.COL_ISPROCESSED + "='true'", null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			if (i == 0) {
				temp = cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.COL_TICKETID));
			} else {
				temp = temp
						+ ","
						+ cursor.getString(cursor
								.getColumnIndex(DatabaseHelper.COL_TICKETID));
			}

		}
		cursor.close();
		System.err.println(temp);
		return temp;
	}

	public String getcurrentDateandTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm aaa");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;

	}

	public void clearallsessionData() {
		// TODO Auto-generated method stub
		// clear databases		
		sharedprefsedit.clear();
		sharedprefsedit.commit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			cursor.close();
			db.close();
			dbhelper.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();
	}
}
