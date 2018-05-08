package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketMetadata;

public interface TicketMetadataRepository extends JpaRepository<TicketMetadata, Long> {
	
	List<TicketMetadata> findBySystemNameAndCustomer(String systemName, String customer);
	
	List<TicketMetadata> findBySystemName(String systemName);
	
	List<TicketMetadata> findByCustomer(String customer);
	
}
