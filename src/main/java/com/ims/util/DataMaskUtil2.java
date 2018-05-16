package com.ims.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataMaskUtil2 {
	
	private DataMaskUtil2(){
		
	}
	
	public static String maskData(String ticketData){
		return maskEmailId(ticketData);
	}
	
	private static String maskEmailId(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedEmail = matcher.replaceAll("****@*****");
		return maskPhoneNumberFormatOne(maskedEmail);
	}
	
	private static String maskPhoneNumberFormatOne(String ticketData){
		Pattern pattern = Pattern.compile("\\+\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatOne = matcher.replaceAll("************");
		return maskPhoneNumberFormatTwo(maskedPhoneNumberFormatOne);
	}
	
	private static String maskPhoneNumberFormatTwo(String ticketData){
		Pattern pattern = Pattern.compile("\\+\\d{2}[-\\.\\s]\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatTwo = matcher.replaceAll("************");
		return maskPhoneNumberFormatThree(maskedPhoneNumberFormatTwo);
	}
	
	private static String maskPhoneNumberFormatThree(String ticketData){
		Pattern pattern = Pattern.compile("\\d{2}[-\\.\\s]\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatThree = matcher.replaceAll("**********");
		return maskPhoneNumberFormatFour(maskedPhoneNumberFormatThree);
	}
	
	private static String maskPhoneNumberFormatFour(String ticketData){
		Pattern pattern = Pattern.compile("\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatFour = matcher.replaceAll("**********");
		return ssn(maskedPhoneNumberFormatFour);
	}
	
	private static String ssn(String ticketData){
		Pattern pattern = Pattern. compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedSsn = matcher.replaceAll("*********");
		return maskCompanyName(maskedSsn);
	}
	
	private static String maskCompanyName(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		return matcher.replaceAll("*********");
	}

}
