package com.ims.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import com.ims.entity.FieldMask;

public class DataMaskUtil {
	
	private DataMaskUtil(){
		
	}
	
	public static String maskData(String ticketData, List<FieldMask> fieldsMask){
		return maskEmailId(ticketData, fieldsMask);
		
	}
	private static String maskEmailId(String ticketData, List<FieldMask> fieldsMask){
		String maskedEmail = ticketData;
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		if(isMaskEnabled(fieldsMask, "Email")){
			maskedEmail = matcher.replaceAll("xxxx@xxxx");
		}
		return maskPhoneNumber1(maskedEmail, fieldsMask);
	}
	private static String maskPhoneNumber1(String ticketData, List<FieldMask> fieldsMask){
		String maskedPhoneNumber = ticketData;
		Pattern pattern = Pattern.compile("(?:(?:\\+|0{0,2})91(\\s*[\\- ]\\s*)?|[0 ]?)?[789]\\d{9}|(\\d[ -]?){10}\\d");
		Matcher matcher = pattern.matcher(ticketData);
		if(isMaskEnabled(fieldsMask, "Phone number")){
			maskedPhoneNumber = matcher.replaceAll(" xxxxx");
		}
		return maskPhoneNumber2(maskedPhoneNumber, fieldsMask);
	}
	
	private static String maskPhoneNumber2(String ticketData, List<FieldMask> fieldsMask){
		String maskedPhoneNumberFormatOne = ticketData;
		Pattern pattern = Pattern.compile("\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		if(isMaskEnabled(fieldsMask, "Phone number")){
			maskedPhoneNumberFormatOne = matcher.replaceAll(" xxxxx");
		}
		return maskWebSiteName(maskedPhoneNumberFormatOne, fieldsMask);
	}
	
	private static String maskWebSiteName(String ticketData, List<FieldMask> fieldsMask){
		String maskedWebSite = ticketData;
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		if(isMaskEnabled(fieldsMask, "URL")){
			maskedWebSite = matcher.replaceAll("xxxxx");
		}
		return ssn(maskedWebSite, fieldsMask);
	}
	
	private static String ssn(String ticketData, List<FieldMask> fieldsMask){
		String maskedSsn = ticketData;
		Pattern pattern = Pattern.compile("\\d{3}[-]\\d{2}[-]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		if(isMaskEnabled(fieldsMask, "SSN")){
			maskedSsn = matcher.replaceAll("xxx");
		}	
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
	
	private static boolean isMaskEnabled(List<FieldMask> fieldsMask, String fieldName){
		boolean isEnabled = false;
		if(!CollectionUtils.isEmpty(fieldsMask)){
			for(FieldMask mask:fieldsMask){
				if(fieldName.equalsIgnoreCase(mask.getFieldName())){
					if("Y".equalsIgnoreCase(mask.getMaskEnabled())){
						isEnabled = true;
					}
				}
			}
		}
		return isEnabled;
	}

}
