package com.smartapps4u.bigmadparty.views;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.asynctasks.ProcessTicket;
import com.smartapps4u.bigmadparty.databases.DatabaseHelper;
import com.smartapps4u.bigmadparty.objects.TicketsResults;
import com.smartapps4u.bigmadparty.objects.XMLCallback;
import com.smartapps4u.bigmadparty.util.GlobalConfig;

public class TicketDetails extends Activity {
	private TextView txtTicketNo, txtName, txtAge, txtGender, txtisProcessed,
			txtEvent, btnProcessTicket, btnTicketlist;
	Context mContext;
	ProcessTicket processtickettask;
	TicketsResults tr;
	SQLiteDatabase db = null;
	DatabaseHelper dbhelper;
	Cursor cursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_details);
		init();
		dbhelper = new DatabaseHelper(this);
		db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		mContext = this;
		tr = (TicketsResults) getIntent().getSerializableExtra("Ticketobj");

		setTicketDetails();
		btnTicketlist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(getApplicationContext(),
						TicketList.class);
				intent.putExtra("eventID", GlobalConfig.EVENT_ID);
				startActivity(intent);
			}
		});
		btnProcessTicket.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processTicket();
			}
		});
		if (getIntent().getExtras().getBoolean("QRscan")) {
			processTicket();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			cursor.close();
			db.close();
			dbhelper.close();
			System.err.println("IN CLOSE");
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_details, menu);
		return true;
	}

	public void init() {
		txtTicketNo = (TextView) findViewById(R.id.txtTicketNo);
		txtName = (TextView) findViewById(R.id.txtName);
		txtAge = (TextView) findViewById(R.id.txtAge);
		btnTicketlist = (TextView) findViewById(R.id.btnTicketlist);
		txtGender = (TextView) findViewById(R.id.txtGender);
		txtisProcessed = (TextView) findViewById(R.id.txtisProcessed);
		txtEvent = (TextView) findViewById(R.id.txtEvent);
		btnProcessTicket = (TextView) findViewById(R.id.btnProcessTicket);
	}

	public void setTicketDetails() {

		txtTicketNo.setText(String.valueOf(tr.getTicketNumber()));
		txtName.setText(tr.getFirstName() + " " + tr.getLastName());
		txtAge.setText(String.valueOf(tr.getAge()));

		if (tr.getGender()) {
			txtGender.setText("Male");
		} else {
			txtGender.setText("Female");
		}
		// txtisProcessed.setText(tr.isProcessed().toString());
		txtEvent.setText(GlobalConfig.EVENT_NAME);
		if (tr.isProcessed()) {
			btnProcessTicket.setVisibility(View.GONE);
			txtisProcessed.setText("Yes");
		} else {
			txtisProcessed.setText("No");
		}

	}

	public void processTicket() {
		System.err.println(GlobalConfig.USER_ID + "::" + GlobalConfig.PIN);
		if (GlobalConfig.ONLINE_MODE) {
			processtickettask = new ProcessTicket(mContext,
					GlobalConfig.USER_ID, GlobalConfig.PIN, String.valueOf(tr
							.getTicketID()), new XMLCallback() {

						@Override
						public void run(String result) {
							// TODO Auto-generated method stub
							result = result.replaceAll("\"", "");
							if (result.equals("Success")) {
								btnProcessTicket.setVisibility(View.GONE);
								int count;
								ContentValues cv = new ContentValues();
								cv.put(DatabaseHelper.COL_ISPROCESSED, "true");
								count = db.update(DatabaseHelper.TABLE_TICKETINSTANCE, cv,
										DatabaseHelper.COL_TICKETID + "=" + tr.getTicketID(), null);
								if (count == 1) {
									btnProcessTicket.setVisibility(View.GONE);
									Toast.makeText(getApplicationContext(), "Ticket Processed.",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(getApplicationContext(),
											"Error Processing Ticket.", Toast.LENGTH_LONG).show();
								}
								Toast.makeText(getApplicationContext(),
										"Success: Ticket Processed",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"Error Processing Ticket.",
										Toast.LENGTH_LONG).show();
							}
							
							finish();
							Intent ticketlist = new Intent(
									getApplicationContext(), TicketList.class);
							ticketlist.putExtra("eventID",
									GlobalConfig.EVENT_ID);
							ticketlist.putExtra("ticketprocessed", true);
							startActivity(ticketlist);
						}
					});
			processtickettask.execute(GlobalConfig.PROCESSTICKET_URL);

		} else {
			int count;
			ContentValues cv = new ContentValues();
			cv.put(DatabaseHelper.COL_ISPROCESSED, "true");
			count = db.update(DatabaseHelper.TABLE_TICKETINSTANCE, cv,
					DatabaseHelper.COL_TICKETID + "=" + tr.getTicketID(), null);
			if (count == 1) {
				btnProcessTicket.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "Ticket Processed.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error Processing Ticket.", Toast.LENGTH_LONG).show();
			}
			finish();
			Intent ticketlist = new Intent(getApplicationContext(),
					TicketList.class);
			ticketlist.putExtra("eventID", GlobalConfig.EVENT_ID);
			ticketlist.putExtra("ticketprocessed", true);
			startActivity(ticketlist);
		}
	}

}
