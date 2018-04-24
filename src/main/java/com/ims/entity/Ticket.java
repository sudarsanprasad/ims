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
	
	private String status;
	
	private String createdDate;
	
	private String updatedDate;
	
	private String createdBy;
	
	private String category;
	
	private String priority;

}
