package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ims.entity.TicketSystem;

public interface TicketSystemRepository extends JpaRepository<TicketSystem, Long> {
	TicketSystem findBySystemNameAndCustomer(String systemName, String customer);
	TicketSystem findBySystemNameAndCustomerAndEnableFlag(String systemName, String customer,String enableFlag);
	List<TicketSystem> findByCustomerAndEnableFlagAndType(String customer,String enableFlag, String type);
	List<TicketSystem> findByCustomer(String customer);
	TicketSystem findById(Long id);
	@Modifying(clearAutomatically = true)
	@Query("update TicketSystem ts set ts.firstTimeFlag ='N'  where ts.customer = :customer")
	void updateFirstTimeFlagAsN(@Param("customer") String customer);
	
	@Query("SELECT DISTINCT ts.customer FROM TicketSystem ts")
	List<String> findDistinctCustomers();
}
