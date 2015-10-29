package com.smartapps4u.bigmadparty.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlParser {
	String XMLString;

	public XmlParser(String XMLString) {
		// TODO Auto-generated constructor stub
		this.XMLString = XMLString;
	}

	public String getJSONString() {

		
	        // We will get the XML from an input stream		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();
			InputStream is = new ByteArrayInputStream(XMLString.getBytes("UTF-8"));
			xpp.setInput(is, "UTF-8");
			int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	          if(eventType == XmlPullParser.START_DOCUMENT) {
	              //System.out.println("Start document");
	          } else if(eventType == XmlPullParser.START_TAG) {
	             // System.out.println("Start tag "+xpp.getName());
	          } else if(eventType == XmlPullParser.END_TAG) {
	              //System.out.println("End tag "+xpp.getName());
	          } else if(eventType == XmlPullParser.TEXT) {
	              //System.out.println("Text "+xpp.getText());
	        	  return xpp.getText();
	          }
	          eventType = xpp.next();
	         }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
