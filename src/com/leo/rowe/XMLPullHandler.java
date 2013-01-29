package com.leo.rowe;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLPullHandler {
	
	 XmlPullParserFactory factory;
	 XmlPullParser xpp ;
	 URL url;
	
	public XMLPullHandler(URL url) {
		// TODO Auto-generated constructor stub
		this.url=url;
	}
	
	public  String getWeather()
	{
		try {
			factory = XmlPullParserFactory.newInstance();
			xpp = factory.newPullParser();
			xpp.setInput(url.openStream(), null);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;   //solve when httpTimeout
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		int eventType = 0;
		try {
			eventType = xpp.getEventType();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		do {
			 if (eventType == XmlPullParser.START_TAG) {
				String name1 = xpp.getName();
				if (name1.equals("condition")) {						
				//	int a=xpp.getAttributeCount();
				   return    xpp.getAttributeValue(0);	
				}
			 }				
				try {
					eventType = xpp.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} while (eventType != XmlPullParser.END_DOCUMENT);
		return null;
	}

}
