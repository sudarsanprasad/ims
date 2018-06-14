package com.ims.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ticket_system")
public class TicketSystem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String systemName;
	
	private String customer;
	
	private String url;
	
	private String type;
	
	private String userName;
	
	private String password;
	
	private String enableFlag;
	
	private String cronValue;
	
	private String lastRunDate;
	
}
