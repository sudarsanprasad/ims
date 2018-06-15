package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketSystem;

public interface TicketSystemRepository extends JpaRepository<TicketSystem, Long> {
	TicketSystem findBySystemNameAndCustomer(String systemName, String customer);
	TicketSystem findBySystemNameAndCustomerAndEnableFlag(String systemName, String customer,String enableFlag);
	List<TicketSystem> findByCustomerAndEnableFlagAndType(String customer,String enableFlag, String type);
	List<TicketSystem> findByCustomer(String customer);
}
