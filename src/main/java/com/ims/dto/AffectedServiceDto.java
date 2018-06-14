package com.ims.dto;

import java.util.List;

import lombok.Data;

@Data
public class AffectedServiceDto {
	
	private int num_incidents;
	private List<IncidentDto> incidents;
	private String affective_service_captured;
	
}
