package com.ims.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
	
	@Id
	private String ticketid;
	
	private String description;
	
	private String shortDescription;
	
	private String comments;

}
