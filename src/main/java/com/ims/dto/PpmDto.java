package com.ims.dto;

import java.util.List;

import lombok.Data;

import com.ims.entity.TicketPpm;

@Data
public class PpmDto {

	private int num_incidents;
	private List<TicketPpm> response;
	

}
