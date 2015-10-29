package com.smartapps4u.bigmadparty.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.objects.TicketsResults;

public class TicketListAdapter extends BaseAdapter {

	private static ArrayList<TicketsResults> arrayList;
	private LayoutInflater mInflater;
	private Context mContext;

	public TicketListAdapter(Context context, ArrayList<TicketsResults> results) {
		arrayList = results;
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ticket_row_item, null);
			holder = new ViewHolder();

			holder.txtTicketID = (TextView) convertView
					.findViewById(R.id.txtTicketID);
			holder.txtDetails = (TextView) convertView
					.findViewById(R.id.txtTicketDetails);
			holder.imgProcessStatus = (ImageView) convertView
					.findViewById(R.id.imgProcessStatus);
			holder.rowlayout = (LinearLayout) convertView
					.findViewById(R.id.rowlayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtTicketID.setText(arrayList.get(position).getTicketNumber());
		holder.txtDetails.setText(arrayList.get(position).getDetails());
		if (arrayList.get(position).isProcessed()) {
			holder.imgProcessStatus.setImageResource(R.drawable.icon_processed);
			holder.rowlayout.setBackgroundResource(R.color.green_processed);
		} else {
			holder.rowlayout.setBackgroundResource(R.color.white);
			holder.imgProcessStatus
					.setImageResource(R.drawable.icon_unprocessed);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView txtTicketID, txtDetails;
		ImageView imgProcessStatus;
		LinearLayout rowlayout;
		// ImageView imgProcessStatus;
	}
}
