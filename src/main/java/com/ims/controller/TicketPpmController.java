package com.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.dto.PpmDto;
import com.ims.service.TicketPpmService;

@RestController
@RequestMapping("/ppm")
public class TicketPpmController {

	@Autowired
	TicketPpmService ticketPpmService;

	@GetMapping(value = "/notifications")
	public PpmDto getNotifications() {
		return ticketPpmService.findByPpmFlag();
	}

	@GetMapping(value = "/updatePpm")
	public void updateForecastStatus() {
		ticketPpmService.updateNotifications();
	}

}
