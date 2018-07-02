package com.ims.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ticket_ppm")
public class TicketPpm {
	
	@Id
	private String id;
	
	private String data;
	
	private String ppmFlag;
	
	private Timestamp createDate;
	
	private Timestamp lastSeenDate;

}
