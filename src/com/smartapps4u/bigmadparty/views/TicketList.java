package com.smartapps4u.bigmadparty.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.adapters.TicketListAdapter;
import com.smartapps4u.bigmadparty.asynctasks.GetTicketsforEvent;
import com.smartapps4u.bigmadparty.databases.DatabaseHelper;
import com.smartapps4u.bigmadparty.objects.TicketsResults;
import com.smartapps4u.bigmadparty.objects.XMLCallback;
import com.smartapps4u.bigmadparty.util.GlobalConfig;

public class TicketList extends Activity {

	Bundle mBundle;
	ListView list;
	ProgressDialog ShowProgress;
	ArrayList<TicketsResults> arraylist_results;
	ArrayList<TicketsResults> partialNames = new ArrayList<TicketsResults>();
	TicketsResults tr;
	private String eventID = "";
	ImageView btnScan;
	ImageView btnRefresh;
	EditText searchbox;
	TextView btnEvents;
	TextView btnDone;
	TicketListAdapter adapter;
	Boolean ticketprocessed = false;
	GetTicketsforEvent gtticketsforevnt;
	private Context mContext;
	SQLiteDatabase db = null;
	DatabaseHelper dbhelper;
	Cursor cursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_list);
		mContext = this;
		init();

		dbhelper = new DatabaseHelper(this);
		db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		cursor = null;
		cursor = db.rawQuery("select * from "
				+ DatabaseHelper.TABLE_TICKETINSTANCE + " where "
				+ DatabaseHelper.COL_EVENTID + "=" + eventID, null);
		System.err.println(ticketprocessed.toString()
				+ GlobalConfig.ONLINE_MODE.toString());
		/*if ((cursor.getCount() <= 0)
				|| ((ticketprocessed == true) && (GlobalConfig.ONLINE_MODE == true)))*/ 
		if(cursor.getCount() <= 0){
			cursor.close();
			gtticketsforevnt = new GetTicketsforEvent(mContext,
					GlobalConfig.USER_ID, GlobalConfig.PIN, eventID, "-1",
					new XMLCallback() {

						@Override
						public void run(String result) {
							// TODO Auto-generated method stub
							parseTicketJson(result);

						}
					});
			gtticketsforevnt.execute(GlobalConfig.TICKETSFOREVENTS_URL);
		} else {

			getTicketsforEventsfromDB();
			adapter = new TicketListAdapter(getApplicationContext(),
					arraylist_results);
			list.setAdapter(adapter);
		}

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Object obj_ret = list.getItemAtPosition(position);
				TicketsResults result_obj = (TicketsResults) obj_ret;

				Intent intent = new Intent(getApplicationContext(),
						TicketDetails.class);
				intent.putExtra("Ticketobj", result_obj);
				intent.putExtra("QRscan", false);
				finish();
				startActivity(intent);
			}
		});
		btnScan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					Intent intent = new Intent(
							"com.google.zxing.client.android.BigMadParty");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, 1000)
							.show();

				}
			}
		});
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.delete(DatabaseHelper.TABLE_TICKETINSTANCE,
						DatabaseHelper.COL_EVENTID + "=" + eventID, null);
				gtticketsforevnt = new GetTicketsforEvent(mContext,
						GlobalConfig.USER_ID, GlobalConfig.PIN, eventID, "-1",
						new XMLCallback() {

							@Override
							public void run(String result) {
								// TODO Auto-generated method stub
								parseTicketJson(result);

							}
						});
				gtticketsforevnt.execute(GlobalConfig.TICKETSFOREVENTS_URL);
			}
		});

		btnEvents.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(),
						EventList.class));
			}
		});

		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchbox.setText("");
				btnDone.setVisibility(View.INVISIBLE);
				btnRefresh.setVisibility(View.VISIBLE);
			}
		});

		searchbox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				AlterAdapter();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				btnRefresh.setVisibility(View.INVISIBLE);
				btnDone.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_list, menu);
		return true;
	}

	public void init() {
		mBundle = getIntent().getExtras();
		eventID = mBundle.getString("eventID");
		System.err.println("eventid" + eventID);
		list = (ListView) findViewById(R.id.ticketlist);
		btnScan = (ImageView) findViewById(R.id.btnScan);
		btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
		btnEvents = (TextView) findViewById(R.id.btnEvents);
		btnDone = (TextView) findViewById(R.id.btnDone);
		searchbox = (EditText) findViewById(R.id.searchtext);
		try {
			ticketprocessed = getIntent().getBooleanExtra("ticketprocessed",
					false);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void parseTicketJson(String json) {
		try {
			System.err.println("HERE IN PARSE TICKET JSON");
			arraylist_results = new ArrayList<TicketsResults>();

			tr = new TicketsResults();
			JSONObject jobj = new JSONObject(json);
			JSONArray Ticketarray = jobj.getJSONArray("Tickets");
			for (int i = 0; i < Ticketarray.length(); i++) {
				try {
					ContentValues insert_values = new ContentValues();
					JSONObject jobj2 = Ticketarray.getJSONObject(i);
					tr.setAge(jobj2.getInt("Age"));
					insert_values.put(DatabaseHelper.COL_AGE,
							jobj2.getInt("Age"));
					tr.setEventID(jobj2.getInt("EventID"));
					insert_values.put(DatabaseHelper.COL_EVENTID,
							jobj2.getInt("EventID"));
					tr.setEventInstanceID(jobj2.getInt("EventInstanceID"));
					insert_values.put(DatabaseHelper.COL_EVENTINSTANCEID,
							jobj2.getInt("EventInstanceID"));
					tr.setFirstName(jobj2.getString("FirstName"));
					insert_values.put(DatabaseHelper.COL_FIRSTNAME,
							jobj2.getString("FirstName"));
					tr.setLastName(jobj2.getString("LastName"));
					insert_values.put(DatabaseHelper.COL_LASTNAME,
							jobj2.getString("LastName"));
					tr.setProcessedStatus(jobj2.getBoolean("IsProcessed"));
					insert_values.put(DatabaseHelper.COL_ISPROCESSED,
							jobj2.getString("IsProcessed"));
					tr.setTicketID(jobj2.getInt("ID"));
					insert_values.put(DatabaseHelper.COL_TICKETID,
							jobj2.getInt("ID"));
					tr.setTicketNumber(jobj2.getString("Number"));
					insert_values.put(DatabaseHelper.COL_TICKETNUMBER,
							jobj2.getString("Number"));
					tr.setGender(jobj2.getBoolean("Gender"));
					insert_values.put(DatabaseHelper.COL_GENDER,
							jobj2.getString("Gender"));
					tr.setTicketName(jobj2.getString("TicketName"));
					insert_values.put(DatabaseHelper.COL_TICKETTYPE,
							jobj2.getString("TicketName"));
					tr.setDetails(getDetailsString(tr));
					arraylist_results.add(tr);
					db.insertOrThrow(DatabaseHelper.TABLE_TICKETINSTANCE, null,
							insert_values);
					tr = new TicketsResults();
					System.err.println("HERE" + i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			adapter = new TicketListAdapter(getApplicationContext(),
					arraylist_results);
			list.setAdapter(adapter);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String getDetailsString(TicketsResults tobj) {
		String MorF = "Female";
		if (tobj.getGender()) {
			MorF = "Male";
		}
		return tobj.getFirstName() + " " + tobj.getLastName() + " | " + MorF
				+ " | Age:" + String.valueOf(tobj.getAge()) + " | "
				+ String.valueOf(tobj.isProcessed());

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				System.err.println(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				System.err.println(intent.getStringExtra("SCAN_RESULT"));

				Intent scan = new Intent(getApplicationContext(),
						TicketDetails.class);
				TicketsResults temprs = new TicketsResults();
				temprs = getTicketobj(intent.getStringExtra("SCAN_RESULT"));
				if (temprs != null) {
					scan.putExtra("Ticketobj", temprs);
					scan.putExtra("QRscan", true);
					finish();
					startActivity(scan);
				} else {

					try {
						MediaPlayer mPlayer = null;
						mPlayer = MediaPlayer.create(getApplicationContext(),
								R.raw.beep);

						mPlayer.start();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Toast.makeText(getApplicationContext(),
							"Error: Invalid ticket", 1000).show();
					Intent int1 = new Intent(
							"com.google.zxing.client.android.BigMadParty");
					int1.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(int1, 0);

				}

			} else if (resultCode == RESULT_CANCELED) {
				System.err.println("Press a button to start a scan.");
				System.err.println("Scan cancelled.");
			}
		}
	}

	public void getTicketsforEventsfromDB() {

		cursor = null;
		cursor = db.rawQuery("select * from "
				+ DatabaseHelper.TABLE_TICKETINSTANCE + " where "
				+ DatabaseHelper.COL_EVENTID + "=" + eventID, null);
		System.err.println("COUNT " + cursor.getCount());
		arraylist_results = new ArrayList<TicketsResults>();
		tr = new TicketsResults();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();

			tr.setAge(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.COL_AGE)));
			tr.setEventID((cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.COL_EVENTID))));
			tr.setEventInstanceID((cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.COL_EVENTINSTANCEID))));
			tr.setFirstName((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_FIRSTNAME))));
			tr.setLastName((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_LASTNAME))));
			tr.setProcessedStatus(Boolean.parseBoolean(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_ISPROCESSED))));
			tr.setTicketID(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.COL_TICKETID)));
			tr.setTicketNumber(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_TICKETNUMBER)));
			tr.setTicketName(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_TICKETTYPE)));

			tr.setGender(Boolean.parseBoolean(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_GENDER))));
			tr.setDetails(getDetailsString(tr));
			arraylist_results.add(tr);
			tr = new TicketsResults();
		}

		cursor.close();

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

	public TicketsResults getTicketobj(String Ticketnum) {
		for (int i = 0; i < arraylist_results.size(); i++) {
			if (arraylist_results.get(i).getTicketNumber().equals(Ticketnum)) {
				return arraylist_results.get(i);
			}
		}
		return null;
	}

	@SuppressLint("NewApi")
	private void AlterAdapter() {

		if (searchbox.getText().toString().isEmpty()) {
			partialNames.clear();
			partialNames.addAll(arraylist_results);
			adapter.notifyDataSetChanged();
			adapter = new TicketListAdapter(getApplicationContext(),
					partialNames);
			adapter.notifyDataSetChanged();
			list.setAdapter(adapter);

		} else {
			partialNames.clear();
			for (int i = 0; i < arraylist_results.size(); i++) {
				/*
				 * if (arraylist_results.get(i).getTicketNumber().toUpperCase().
				 * contains(
				 * searchbox.getText().toString().toUpperCase())||arraylist_results
				 * .
				 * get(i).getDetails().toUpperCase().contains(searchbox.getText(
				 * ).toString().toUpperCase())) {
				 * partialNames.add(arraylist_results.get(i)); }
				 */
				if (arraylist_results
						.get(i)
						.getTicketNumber()
						.toUpperCase()
						.startsWith(
								searchbox.getText().toString().toUpperCase())
						|| arraylist_results
								.get(i)
								.getDetails()
								.toUpperCase()
								.startsWith(
										searchbox.getText().toString()
												.toUpperCase())) {
					partialNames.add(arraylist_results.get(i));
				}

			}
			adapter = new TicketListAdapter(getApplicationContext(),
					partialNames);
			adapter.notifyDataSetChanged();
			list.setAdapter(adapter);

		}
	}

}
