package com.ims.util;

import java.util.Date;

import com.ims.entity.TicketSystem;

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

	public static String getFileName(String location, TicketSystem system) {
		StringBuilder fileName = new StringBuilder(location);
		fileName.append(system.getCustomer()).append("_").append(system.getSystemName()).append("_").append(DateUtil.convertDateToAPIString(new Date())).append(".csv");
		return fileName.toString();
	}

	public static String getPpmFileName(String ppmLocation, TicketSystem system) {
		StringBuilder fileName = new StringBuilder(ppmLocation);
		fileName.append(system.getCustomer()).append("_").append(system.getSystemName()).append("_").append("PPM").append("_").append(DateUtil.convertDateToAPIString(new Date())).append(".csv");
		return fileName.toString();
	}
}
