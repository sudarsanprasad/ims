package com.ims.dto;

import lombok.Data;

@Data
public class IncidentDto {
	
	private String title;
	
	private String solution;
	
	private String id;
	
	private String affective_service_captured;
	
	private double similarity;

}
