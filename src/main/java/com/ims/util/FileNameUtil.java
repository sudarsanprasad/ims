package com.ims.util;

public class FileNameUtil {
	private FileNameUtil(){}
	
	public static String getCustomerName(String fileName){
		String[] fileArgs = fileName.split("_");
		return fileArgs[0];
	}
	
	public static String getSystemName(String fileName){
		String[] fileArgs = fileName.split("_");
		return fileArgs[1];
	}
}
