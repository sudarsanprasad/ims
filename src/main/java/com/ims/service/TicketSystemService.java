package com.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.entity.TicketSystem;
import com.ims.repository.TicketSystemRepository;

@Service
public class TicketSystemService {

	@Autowired
	private TicketSystemRepository ticketSystemRepository;

	public TicketSystem getTicketSystemById(Long id) {
		return ticketSystemRepository.findOne(id);
	}

	public void saveTicketSystem(TicketSystem ticketSystem) {
		ticketSystemRepository.save(ticketSystem);
	}

	public void updateTicketSystem(TicketSystem ticketSystem) {
		ticketSystemRepository.save(ticketSystem);
	}

	public void deleteTicketSystem(TicketSystem ticketSystem) {
		ticketSystemRepository.delete(ticketSystem);
	}

	public void deleteTicketSystemById(Long id) {
		ticketSystemRepository.delete(id);
	}
}
