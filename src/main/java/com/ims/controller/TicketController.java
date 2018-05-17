package com.ims.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.entity.Ticket;
import com.ims.exception.ImsException;
import com.ims.service.FTPService;
import com.ims.service.ImsConfigurationService;
import com.ims.service.TicketService;
import com.ims.taskconfig.ScheduledTasks;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	
	private static final Logger LOG = Logger.getLogger(TicketController.class);
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	private ScheduledTasks scheduledTasks;
	
	@Autowired
	ImsConfigurationService imsConfigurationService;
	
	@GetMapping
	public List<Ticket> getTickets(){
		return ticketService.getTicketData();
	}
	
	@GetMapping(value = "/triggerFtp")
	public boolean downloadExcel() {
			try {
				return ftpService.downloadExcel();
			} catch (ImsException e) {
				LOG.error(e);
			}
		return false;
	}
	
}
