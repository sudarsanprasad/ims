package com.ims.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ims.entity.Ticket;
import com.ims.repository.TicketRepository;

@Service
public class TicketService {
	
	private static final Logger LOG = Logger.getLogger(TicketService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	public List<Ticket> updateTicketData(String result){
		LOG.info("Result in Service === "+result);
		List<Ticket> tickets = null;
		JSONObject jsonObj = new JSONObject(result);
		 JSONArray records = jsonObj.getJSONArray("result");
		 if(records != null && records.length() != 0){
			 tickets = new ArrayList<>();
			 for(int i=0; i < records.length(); i++){
				 Ticket ticket = new Ticket();
				 JSONObject record = records.getJSONObject(i);
				 ticket.setTicketid((String) record.get(env.getProperty("tikcetid")));
				 ticket.setComments((String) record.get(env.getProperty("comments")));
				 ticket.setDescription((String) record.get(env.getProperty("description")));
				 ticket.setShortDescription((String) record.get(env.getProperty("shortdescription")));
				 ticket.setStatus((String) record.get(env.getProperty("status")));
				 ticket.setCategory((String) record.get(env.getProperty("category")));
				 ticket.setPriority((String) record.get(env.getProperty("priority")));
				 ticket.setCreatedDate((String) record.get(env.getProperty("createddate")));
				 ticket.setCreatedBy((String) record.get(env.getProperty("createdby")));
				 ticket.setUpdatedDate((String) record.get(env.getProperty("updateddate")));
				 tickets.add(ticket);
			 }
			 tickets = ticketRepository.save(tickets);
		 }
		 return tickets;
	}
	
	public List<Ticket> getTicketData(){
		return ticketRepository.findAll();
	}

}
