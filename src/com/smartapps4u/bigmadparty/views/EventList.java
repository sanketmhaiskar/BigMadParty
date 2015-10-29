package com.smartapps4u.bigmadparty.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.adapters.EventListAdapter;
import com.smartapps4u.bigmadparty.asynctasks.GetEvents;
import com.smartapps4u.bigmadparty.databases.DatabaseHelper;
import com.smartapps4u.bigmadparty.objects.EventsResults;
import com.smartapps4u.bigmadparty.util.GlobalConfig;
import com.smartapps4u.bigmadparty.objects.XMLCallback;

public class EventList extends Activity {

	ListView list;
	ProgressDialog ShowProgress;
	static ArrayList<EventsResults> arraylist_results;
	EventsResults er;
	static EventListAdapter adapter;
	GetEvents gtevents;
	private static Context mContext;
	static SQLiteDatabase db = null;
	static DatabaseHelper dbhelper;
	Cursor cursor = null;
	ImageView btnRefresh, btnHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		init();

		mContext = this;

		dbhelper = new DatabaseHelper(this);
		db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		cursor = null;
		cursor = db.rawQuery("select * from "
				+ DatabaseHelper.TABLE_EVENTINSTANCE, null);
		if (cursor.getCount() <= 0) {
			cursor.close();
			gtevents = new GetEvents(mContext, GlobalConfig.USER_ID,
					GlobalConfig.PIN, new XMLCallback() {

						@Override
						public void run(String result) {
							// TODO Auto-generated method stub
							parseEventJson(result);
						}
					});
			gtevents.execute(GlobalConfig.EVENTS_URL);
		} else {

			getEventsfromDB();
			adapter = new EventListAdapter(getApplicationContext(),
					arraylist_results);
			list.setAdapter(adapter);
		}

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Object obj_ret = list.getItemAtPosition(position);
				EventsResults result_obj = (EventsResults) obj_ret;
				String temp = result_obj.getEventID();
				GlobalConfig.EVENT_NAME = result_obj.getEventName();
				Intent intent = new Intent(getApplicationContext(),
						TicketList.class);
				intent.putExtra("eventID", temp);
				GlobalConfig.EVENT_ID = temp;
				finish();
				startActivity(intent);
			}
		});
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.delete(DatabaseHelper.TABLE_EVENTINSTANCE, null, null);
				gtevents = new GetEvents(mContext, GlobalConfig.USER_ID,
						GlobalConfig.PIN, new XMLCallback() {

							@Override
							public void run(String result) {
								// TODO Auto-generated method stub
								parseEventJson(result);
							}
						});
				gtevents.execute(GlobalConfig.EVENTS_URL);
			}
		});

		btnHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(),
						UserMenu.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_list, menu);
		return true;
	}

	public void init() {
		list = (ListView) findViewById(R.id.eventlist);
		btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
		btnHome = (ImageView) findViewById(R.id.btnHome);
	}

	private void parseEventJson(String json) {

		arraylist_results = new ArrayList<EventsResults>();
		try {
			er = new EventsResults();
			JSONArray eventarray = new JSONArray(json);
			for (int i = 0; i < eventarray.length(); i++) {
				try {
					ContentValues insert_values = new ContentValues();
					JSONObject c = eventarray.getJSONObject(i);
					er.setEventName(c.getString("Name"));
					insert_values.put(DatabaseHelper.COL_EVENTNAME,
							c.getString("Name"));
					er.setPlaceName(c.getString("PlaceName"));
					insert_values.put(DatabaseHelper.COL_PLACENAME,
							c.getString("PlaceName"));
					er.setEventID(c.getString("ID"));
					insert_values.put(DatabaseHelper.COL_EVENTID,
							c.getString("ID"));
					er.setAddress(c.getString("Address"));
					insert_values.put(DatabaseHelper.COL_ADDRESS,
							c.getString("Address"));
					er.setPlaceTypes(c.getString("PlaceTypes"));
					insert_values.put(DatabaseHelper.COL_PLACETYPES,
							c.getString("PlaceTypes"));
					er.setImageName(c.getString("ImgFileName"));
					insert_values.put(DatabaseHelper.COL_IMAGEFILENAME,
							c.getString("ImgFileName"));
					er.setStartDate(c.getString("DateStartString"));
					insert_values.put(DatabaseHelper.COL_DATESTART,
							c.getString("DateStartString"));
					arraylist_results.add(er);
					db.insertOrThrow(DatabaseHelper.TABLE_EVENTINSTANCE, null,
							insert_values);
					er = new EventsResults();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
				adapter = new EventListAdapter(getApplicationContext(),
						arraylist_results);
				list.setAdapter(adapter);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getEventsfromDB() {

		cursor = null;
		cursor = db.rawQuery("select * from "
				+ DatabaseHelper.TABLE_EVENTINSTANCE, null);
		System.err.println("COUNT " + cursor.getCount());
		arraylist_results = new ArrayList<EventsResults>();
		er = new EventsResults();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();

			er.setEventID((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_EVENTID))));
			er.setEventName((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_EVENTNAME))));
			er.setPlaceTypes((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_PLACETYPES))));
			er.setPlaceName((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_PLACENAME))));
			er.setStartDate((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_DATESTART))));
			er.setAddress((cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_ADDRESS))));
			arraylist_results.add(er);
			er = new EventsResults();
		}

		cursor.close();

	}

	public static void deleteEvent(final String Eventid, final int pos) {
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	arraylist_results.remove(pos);
		    		adapter.notifyDataSetChanged();
		    		int row=db.delete(DatabaseHelper.TABLE_EVENTINSTANCE,DatabaseHelper.COL_EVENTID+"="+Eventid, null);
		    		System.err.println(row);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	dialog.dismiss();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("Are you sure you want to delete the event?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();

	
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
