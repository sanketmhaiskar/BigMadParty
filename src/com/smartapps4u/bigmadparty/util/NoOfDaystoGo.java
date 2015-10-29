package com.smartapps4u.bigmadparty.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoOfDaystoGo {
	private int noofdays=0;
	String str_date = "";
	
	public NoOfDaystoGo(String str_date) {
		// TODO Auto-generated constructor stub		
		this.str_date=str_date;
		DateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			long temptime = (timeStampDate.getTime() / 1000)
					- System.currentTimeMillis() / 1000;
			System.err.println(temptime);
			noofdays = (int) (temptime / 86400);
			if(noofdays<0)
				noofdays=0;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int getNoofDays()
	{
		return noofdays;
	}
}
