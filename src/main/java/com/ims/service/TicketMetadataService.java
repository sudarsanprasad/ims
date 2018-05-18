package com.ims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.entity.TicketMetadata;
import com.ims.repository.TicketMetadataRepository;

@Service
public class TicketMetadataService {

	@Autowired
    private TicketMetadataRepository ticketMetadataRepository;
	
	public List<TicketMetadata> create(List<TicketMetadata> ticketMetadata) {
		return ticketMetadataRepository.save(ticketMetadata);
	}

	public List<TicketMetadata> update(List<TicketMetadata> ticketMetadata) {
		return ticketMetadataRepository.save(ticketMetadata);
	}
	
	public TicketMetadata findById(Long metadataId) {
		return ticketMetadataRepository.findOne(metadataId);
	}

	public void delete(Long metadataId) {
		ticketMetadataRepository.delete(metadataId);
		
	}

	public List<TicketMetadata> findAll() {
		return ticketMetadataRepository.findAll();
	}
	
	public List<TicketMetadata> findBySystemNameAndCustomer(String systemName, String customer){
		return ticketMetadataRepository.findBySystemNameAndCustomer(systemName, customer);
	}
	
	public List<TicketMetadata> findBySystemName(String systemName){
		return ticketMetadataRepository.findBySystemName(systemName);
	}
	
	public List<TicketMetadata> findByCustomer(String customer){
		return ticketMetadataRepository.findByCustomer(customer);
	}
	
	public List<String> findByIsForecast(String systemName){
		return ticketMetadataRepository.findMappingColumnByIsForecast(systemName);
	}
	
	public List<String> findByIsProactive(String systemName){
		return ticketMetadataRepository.findMappingColumnByIsProactive(systemName);
	}

}
