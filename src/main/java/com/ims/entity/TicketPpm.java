package com.ims.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import org.json.simple.JSONObject;

@Data
@Entity
@Table(name = "ticket_ppm")
public class TicketPpm {
	
	@Id
	private String id;
	
	@Column(length=10485760)
	private String data;
	
	private String ppmFlag;
	
	private Timestamp createDate;
	
	private Timestamp lastSeenDate;
	
	@Transient
	JSONObject ppmData;
	
}
