package com.ims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ims.entity.FieldMask;
import com.ims.entity.TicketSystem;
import com.ims.repository.FieldMaskRepository;
import com.ims.repository.TicketSystemRepository;

@Service
public class TicketSystemService {

	@Autowired
	private TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	private FieldMaskRepository fieldMaskRepository;

	public TicketSystem getTicketSystemById(Long id) {
		return ticketSystemRepository.findOne(id);
	}

	public void saveTicketSystem(TicketSystem ticketSystem) {
		List<TicketSystem> ticketSystems =  ticketSystemRepository.findByCustomer(ticketSystem.getCustomer());
		if(CollectionUtils.isEmpty(ticketSystems)){
			ticketSystem.setFirstTimeFlag("Y");
		}
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
	
	public List<FieldMask> getFieldMask() {
		return fieldMaskRepository.findByMaskEnabled("X");
	}
}
