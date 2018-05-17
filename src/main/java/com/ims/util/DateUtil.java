package com.ims.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private DateUtil(){
		
	}
	
	public static String convertDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return dateFormat.format(date);  
	}
	
	public static String[] getDateAndTime(String timestamp){
		return timestamp.split(" ");
	}

}
