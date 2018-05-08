package com.ims.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataMaskUtil {
	
	private DataMaskUtil(){
		
	}
	
	public static String maskData(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		String repalceString = matcher.replaceAll("****@*****");
		pattern = Pattern.compile("\\+\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
		matcher = pattern.matcher(repalceString);
		return matcher.replaceAll("**********");
	}

}
