package com.ims.dto;

import java.util.List;

import lombok.Data;

@Data
public class TopicDto {
	
	private int topic;
	private List<AffectedServiceDto> affective_service_captured;
	

}
