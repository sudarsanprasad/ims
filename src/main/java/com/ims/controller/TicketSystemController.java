package com.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ims.entity.TicketSystem;
import com.ims.service.TicketSystemService;

@RestController
@RequestMapping("/ticketsystem")
public class TicketSystemController {

	@Autowired
	private TicketSystemService ticketSystemService;

	@GetMapping("/getbyid/{id}")
	public TicketSystem getTicketSystemById(@PathVariable("id") Long id) {
		return ticketSystemService.getTicketSystemById(id);
	}

	@PostMapping("/save")
	public void saveTicketSystem(@RequestBody TicketSystem ticketSystem) {
		ticketSystemService.saveTicketSystem(ticketSystem);
	}

	@PutMapping("/update")
	public void updateTicketSystem(@RequestBody TicketSystem ticketSystem) {
		ticketSystemService.updateTicketSystem(ticketSystem);
	}

	@DeleteMapping("/delete")
	public void deleteTicketSystem(@RequestBody TicketSystem ticketSystem) {
		ticketSystemService.deleteTicketSystem(ticketSystem);
	}

	@DeleteMapping("/deletebyid/{id}")
	public void deleteTicketSystemById(@PathVariable("id") Long id) {
		ticketSystemService.deleteTicketSystemById(id);
	}
}
