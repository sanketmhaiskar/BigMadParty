package com.smartapps4u.bigmadparty.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	Context context;
	public static final String DB_NAME = "com_smartapps4u_BMP";
	static final int DB_VERSION = 1;

	public static final String TABLE_EVENTINSTANCE = "EventInstance";
	public static final String TABLE_TICKETINSTANCE = "TicketInstance";

	public static final String COL_ID = "ID";

	public static final String COL_EVENTID = "EventID";
	public static final String COL_EVENTNAME = "Name";
	public static final String COL_IMAGEFILENAME = "ImgFileName";
	public static final String COL_DATESTART = "DateStart";
	public static final String COL_Status = "STATUS";
	public static final String COL_PLACENAME = "PlaceName";
	public static final String COL_PLACETYPES = "PlaceTypes";
	public static final String COL_ADDRESS = "Address";

	public static final String COL_FIRSTNAME = "FirstName";
	public static final String COL_LASTNAME = "LastName";
	public static final String COL_GENDER = "Gender";
	public static final String COL_AGE = "Age";
	public static final String COL_EVENTINSTANCEID = "EventInstanceID";
	public static final String COL_TICKETID = "TicketID";
	public static final String COL_TICKETNUMBER = "Number";
	public static final String COL_TICKETTYPE = "TicketType";
	public static final String COL_ISPROCESSED = "isProcessed";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE_EVENTINSTANCE + " (" + COL_ID
				+ " INTEGER PRIMARY KEY," + COL_EVENTID + " INTEGER NOT NULL,"
				+ COL_EVENTNAME + " TEXT NOT NULL," + COL_IMAGEFILENAME
				+ " TEXT," + COL_DATESTART + " TEXT," + COL_Status
				+ " INTEGER," + COL_PLACENAME + " TEXT NOT NULL,"
				+ COL_PLACETYPES + " TEXT," + COL_ADDRESS + " TEXT)");

		db.execSQL("CREATE TABLE " + TABLE_TICKETINSTANCE + " (" + COL_ID
				+ " INTEGER PRIMARY KEY," + COL_FIRSTNAME + " TEXT NOT NULL,"
				+ COL_LASTNAME + " TEXT," + COL_GENDER + " TEXT," + COL_AGE
				+ " INTEGER," + COL_EVENTID + " INTEGER NOT NULL,"
				+ COL_EVENTINSTANCEID + " INTEGER NOT NULL," + COL_TICKETID
				+ " INTEGER NOT NULL," + COL_TICKETNUMBER + " TEXT NOT NULL,"
				+ COL_TICKETTYPE + " TEXT NOT NULL," + COL_ISPROCESSED
				+ " TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP " + TABLE_EVENTINSTANCE + " IF EXISTS");
		db.execSQL("DROP " + TABLE_TICKETINSTANCE + " IF EXISTS");
	}
}
