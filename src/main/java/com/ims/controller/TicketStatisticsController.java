package com.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.entity.TicketStatistics;
import com.ims.service.TicketStatisticsService;

@RestController
@RequestMapping("/statistics")
public class TicketStatisticsController {
	
	@Autowired
	TicketStatisticsService ticketStatisticsService;

	@PostMapping
	public TicketStatistics createStatistics(@RequestBody TicketStatistics ticketStatistics) {
		return ticketStatisticsService.create(ticketStatistics);
	}
	
	@PutMapping
	public TicketStatistics updateStatistics(@RequestBody TicketStatistics ticketStatistics) {
		return ticketStatisticsService.create(ticketStatistics);
	}
	
	@GetMapping(value = "/{id}")
	public TicketStatistics getStatistics(@PathVariable Long  id) {
		return ticketStatisticsService.findById(id);
	}
	
	@GetMapping
	public List<TicketStatistics> findAll() {
		return ticketStatisticsService.findAll();
	}
	
	@GetMapping(value = "/log")
	public List<TicketStatistics> findAllByFileNameOrderByJobId() {
		return ticketStatisticsService.findAllByFileNameOrderByJobId();
	}
	
	@GetMapping(value = "/record")
	public TicketStatistics findMostRecentRecord() {
		return ticketStatisticsService.findMostRecentRecord();
	}
	
	@GetMapping(value = "/getCurrentRecords")
	public List<TicketStatistics> getCurrentRecords() {
		return ticketStatisticsService.getCurrentRecords();
	}
	
	@GetMapping(value = "/getCurrentRecords/{id}")
	public TicketStatistics getCurrentRecordStatus(@PathVariable Long id) {
		return ticketStatisticsService.getCurrentRecordStatus(id);
	}

}
