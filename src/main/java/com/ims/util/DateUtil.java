package com.ims.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	
	private static final Logger LOG = Logger.getLogger(DateUtil.class);
	
	private DateUtil(){
		
	}
	
	public static String convertDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return dateFormat.format(date);  
	}
	
	public static String[] getDateAndTime(String timestamp){
		return timestamp.split(" ");
	}
	
	public static String convertDate(String dateInString){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date;
        String resultDate = null;
		try {
			date = formatter.parse(dateInString);
			resultDate = formatter.format(date);
		} catch (ParseException e) {
			LOG.info(e);
		}
        return resultDate;
	}
	
	public static String convertDateToAPIString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        return dateFormat.format(date);  
	}
	
}
