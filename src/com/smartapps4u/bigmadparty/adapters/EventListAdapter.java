package com.smartapps4u.bigmadparty.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapps4u.bigmadparty.R;
import com.smartapps4u.bigmadparty.objects.EventsResults;
import com.smartapps4u.bigmadparty.util.NoOfDaystoGo;
import com.smartapps4u.bigmadparty.views.EventList;

public class EventListAdapter extends BaseAdapter {
	private static ArrayList<EventsResults> arrayList;
	private LayoutInflater mInflater;
	private Context mContext;

	public EventListAdapter(Context context, ArrayList<EventsResults> results) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.event_row_item, null);
			holder = new ViewHolder();

			holder.txtEventName = (TextView) convertView
					.findViewById(R.id.txtEventName);
			holder.txtPlaceName = (TextView) convertView
					.findViewById(R.id.txtPlaceName);
			holder.txtNoofDays = (TextView) convertView
					.findViewById(R.id.txtNoofDays);
			holder.btnDeleteEvent = (ImageView) convertView
					.findViewById(R.id.btnDeleteEvent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtEventName.setText(arrayList.get(position).getEventName());
		holder.txtPlaceName.setText(arrayList.get(position).getPlaceName());
		holder.txtNoofDays.setText(String.valueOf(new NoOfDaystoGo(arrayList
				.get(position).getStartDate()).getNoofDays()));
		holder.btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventList.deleteEvent(arrayList.get(position).getEventID(), position);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView txtEventName, txtPlaceName, txtNoofDays;
		ImageView btnDeleteEvent;
	}

}
