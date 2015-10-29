package com.smartapps4u.bigmadparty.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.asynctasks.LoginTask;
import com.smartapps4u.bigmadparty.databases.DatabaseHelper;
import com.smartapps4u.bigmadparty.objects.XMLCallback;
import com.smartapps4u.bigmadparty.util.GlobalConfig;

public class LoginActivity extends Activity {
	LoginTask lgtask = null;
	private TextView txtUsername;
	private TextView txtPassword;
	private TextView btnLogin;
	private Context mContext;
	SQLiteDatabase db = null;
	DatabaseHelper dbhelper;
	private SharedPreferences sharedprefs;
	private SharedPreferences.Editor sharedprefsedit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		dbhelper = new DatabaseHelper(this);
		db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lgtask = new LoginTask(mContext, txtUsername.getText()
						.toString(), txtPassword.getText().toString(),
						new XMLCallback() {

							@Override
							public void run(String result) {
								// TODO Auto-generated method stub
								if (validateLogin(result)) {
									// Login success
									if (txtUsername
											.getText()
											.toString()
											.equals(sharedprefs.getString(
													"USERNAME", null))
											&& txtPassword
													.getText()
													.toString()
													.equals(sharedprefs
															.getString(
																	"PASSWORD",
																	null))) {
										db.delete(
												DatabaseHelper.TABLE_EVENTINSTANCE,
												null, null);
										db.delete(
												DatabaseHelper.TABLE_TICKETINSTANCE,
												null, null);
									}
									sharedprefsedit.putBoolean("LOGGED_IN",
											true);
									sharedprefsedit.putString("USERNAME",
											txtUsername.getText().toString());
									sharedprefsedit.putString("PASSWORD",
											txtUsername.getText().toString());
									sharedprefsedit.commit();
									finish();
									startActivity(new Intent(
											getApplicationContext(),
											UserMenu.class));
								} else {
									Toast.makeText(getApplicationContext(),
											"Invalid Username or Password",
											3000).show();
								}
							}
						});
				lgtask.execute(GlobalConfig.LOGIN_URL);
			}
		});
		if (sharedprefs.getBoolean("LOGGED_IN", false)) {

			GlobalConfig.USER_ID = sharedprefs.getString("USER_ID", null);
			GlobalConfig.PIN = sharedprefs.getString("PIN", null);
			finish();
			startActivity(new Intent(getApplicationContext(), UserMenu.class));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private Boolean validateLogin(String result) {
		try {
			JSONObject c = new JSONObject(result);
			GlobalConfig.USER_ID = c.getString("UserID");
			GlobalConfig.PIN = c.getString("PIN");
			sharedprefsedit.putString("USER_ID", GlobalConfig.USER_ID);
			sharedprefsedit.putString("PIN", GlobalConfig.PIN);
			sharedprefsedit.commit();
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public void init() {
		txtUsername = (TextView) findViewById(R.id.txtUsername);
		txtPassword = (TextView) findViewById(R.id.txtPassword);
		btnLogin = (TextView) findViewById(R.id.btnLogin);
		sharedprefs = getSharedPreferences("smartapps4u_bigmadparty", 0);
		sharedprefsedit = sharedprefs.edit();
		mContext = this;
	}

}
