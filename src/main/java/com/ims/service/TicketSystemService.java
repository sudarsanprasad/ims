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
	
	@Autowired
	ImsJobService imsJobService;

	public TicketSystem getTicketSystemById(Long id) {
		return ticketSystemRepository.findOne(id);
	}
	
	public List<TicketSystem> findAll() {
		return ticketSystemRepository.findAll();
	}

	public TicketSystem saveTicketSystem(TicketSystem ticketSystem) {
		List<TicketSystem> ticketSystems =  ticketSystemRepository.findByCustomer(ticketSystem.getCustomer());
		if(CollectionUtils.isEmpty(ticketSystems)){
			ticketSystem.setFirstTimeFlag("Y");
		}else{
			ticketSystem.setFirstTimeFlag("N");
		}
		ticketSystem.setAutomationCronValue("0 0/2 * * * ?");
		return ticketSystemRepository.save(ticketSystem);
	}

	public TicketSystem updateTicketSystem(TicketSystem ticketSystem) {
		return ticketSystemRepository.save(ticketSystem);
	}

	public void deleteTicketSystem(TicketSystem ticketSystem) {
		ticketSystemRepository.delete(ticketSystem);
	}

	public String deleteTicketSystemById(Long id) {
		TicketSystem system = ticketSystemRepository.findById(id);
		ticketSystemRepository.delete(id);
		imsJobService.deleteJob(system.getCustomer(), system.getSystemName());
		return "System Deleted Successfully";
	}
	
	public List<FieldMask> getFieldMask() {
		return fieldMaskRepository.findByMaskEnabled("X");
	}
	
	public List<String> getCustomers() {
		return ticketSystemRepository.findDistinctCustomers();
	}
}
