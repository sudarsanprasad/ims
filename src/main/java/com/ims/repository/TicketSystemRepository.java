package com.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketSystem;

public interface TicketSystemRepository extends JpaRepository<TicketSystem, Long> {
	TicketSystem findBySystemNameAndCustomer(String systemName, String customer);
}
