package com.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.entity.Ticket;
import com.ims.service.TicketService;
import com.ims.taskconfig.ScheduledTasks;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private ScheduledTasks scheduledTasks;
	
	@GetMapping
	public List<Ticket> getTickets(){
		return ticketService.getTicketData();
	}
	
}
