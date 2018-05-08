package com.ims.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ticket_metadata")
public class TicketMetadata {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String systemName;
	
	private String customer;
	
	private String mappingColumn;
	
	private String businessColumn;
	
	private String isKnowledgement;
	
	private String isForecast;
	
	private String isProactive;
	
}
