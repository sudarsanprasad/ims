package com.ims.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	
	private String forecastCronValue;
	
	private String lastRunDate;
	
	private String krCronValue;
	
	private String automationCronValue;
	
	private String firstTimeFlag;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketSystemId")
	private List<FieldMask> fieldsMask;
	
	
}
