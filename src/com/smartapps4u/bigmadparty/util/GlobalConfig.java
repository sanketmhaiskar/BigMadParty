package com.smartapps4u.bigmadparty.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.zxing.client.android.R;


public class GlobalConfig {

public static String LOGIN_URL="http://bigmadparty.com/event/wsmobile.asmx/Mobile_GetLogin";
public static String EVENTS_URL="http://bigmadparty.com/event/wsmobile.asmx/Mobile_GetEvents";
public static String TICKETSFOREVENTS_URL="http://bigmadparty.com/event/wsmobile.asmx/Mobile_GetTicketsForEvent";
public static String PROCESSTICKET_URL="http://bigmadparty.com/event/wsmobile.asmx/Mobile_ProcessTicket";
public static String PROCESSTICKETS_URL="http://bigmadparty.com/event/wsmobile.asmx/Mobile_ProcessTickets";


public static String USER_ID;
public static String PIN;

public static String EVENT_ID="";
public static String EVENT_NAME="";

public static Boolean ONLINE_MODE=false;



}


