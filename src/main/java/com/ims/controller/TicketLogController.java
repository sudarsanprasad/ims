package com.ims.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.service.TicketLogService;

@RestController
@RequestMapping("/ticketlog")
public class TicketLogController {

	@Autowired
	private TicketLogService ticketLogService;
	
	@GetMapping(value="/download/{jobId}") 
	public ResponseEntity<Object> downloadLogStatistics(@PathVariable("jobId") Long jobId) throws IOException  {
		return ticketLogService.downloadLogStatistics(jobId);
		
	}
}
