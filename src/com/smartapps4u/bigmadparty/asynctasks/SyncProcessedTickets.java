package com.smartapps4u.bigmadparty.asynctasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.smartapps4u.bigmadparty.network.CustomHttpClient;
import com.smartapps4u.bigmadparty.objects.XMLCallback;
import com.smartapps4u.bigmadparty.util.XmlParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SyncProcessedTickets extends AsyncTask<String, Void, String> {

	Context ctx;
	private SyncProcessedTickets synctask = null;
	ProgressDialog ShowProgress;
	Boolean insidecancel = false;
	XMLCallback callback;
	String userid = "";
	String userpin = "";
	String TicketIDs = "";

	public SyncProcessedTickets(Context ctx, String User_ID, String User_PIN,
			String TicketIDs, XMLCallback callback) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		this.callback = callback;
		this.userid = User_ID;
		this.userpin = User_PIN;
		this.TicketIDs = TicketIDs;
	}

	@Override
	protected String doInBackground(String... url) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("userID", userid.toString()));
		postParameters.add(new BasicNameValuePair("pin", userpin.toString()));
		postParameters.add(new BasicNameValuePair("ticketInstanceIDs", TicketIDs
				.toString()));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(url[0], postParameters);
			XmlParser xmlparser = new XmlParser(response);
			return xmlparser.getJSONString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		synctask = this;
		ShowProgress = new ProgressDialog(ctx);
		ShowProgress.setMessage("Syncing Processed Tickets...");
		ShowProgress.setCancelable(true);
		insidecancel = false;
		ShowProgress
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						try {
							System.out.println("Inside cancel");
							Toast.makeText(ctx, "Sync Cancelled.", 3000)
									.show();
							insidecancel = true;
							synctask.cancel(true);
							Log.i("TASK", "Cancelled");
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});

		ShowProgress.show();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		callback.run(result);
		ShowProgress.dismiss();
		super.onPostExecute(result);
	}

}
