package com.ims.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataMaskUtil {
	
	private DataMaskUtil(){
		
	}
	
	public static String maskData(String ticketData){
		return maskPhoneNumber1(ticketData);
		
	}
	
	private static String maskPhoneNumber1(String ticketData){
		Pattern pattern = Pattern.compile("(?:(?:\\+|0{0,2})91(\\s*[\\- ]\\s*)?|[0 ]?)?[789]\\d{9}|(\\d[ -]?){10}\\d");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumber = matcher.replaceAll(" xxxxx");
		return maskPhoneNumber2(maskedPhoneNumber);
	}
	
	private static String maskPhoneNumber2(String ticketData){
		Pattern pattern = Pattern.compile("\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatOne = matcher.replaceAll(" xxxxx");
		return maskWebSiteName(maskedPhoneNumberFormatOne);
	}
	
	private static String maskWebSiteName(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedWebSite = matcher.replaceAll("xx");
		return maskEmailId(maskedWebSite);
	}
	
	private static String maskEmailId(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedEmail = matcher.replaceAll("xx@xx");
		return ssn(maskedEmail);
	}
	
	private static String ssn(String ticketData){
		Pattern pattern = Pattern.compile("\\d{3}[-]\\d{2}[-]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedSsn = matcher.replaceAll("xxx");
		return maskName(maskedSsn);
	}
	
	private static String maskName(String ticketData){
		Pattern pattern = Pattern. compile("[Hi\\Hello ][a-zA-Z]+[,]");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedEmail = matcher.replaceAll(" xxx");
		return replaceSpecialChars(maskedEmail);
	}
	
	public static String replaceSpecialChars(String ticketData){
		return ticketData.replaceAll("[^a-zA-Z0-9]", " ");
	}

}
