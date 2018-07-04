package com.ims.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ims.entity.TicketPpm;

public interface TicketPpmRepository extends JpaRepository<TicketPpm, String> {
	
	List<TicketPpm> findByPpmFlag(String ppmFlag);
	
	List<TicketPpm> findByPpmFlagAndCreateDateIsAfter(String ppmFlag,Timestamp createDate);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update TicketPpm tp set tp.lastSeenDate =current_timestamp, tp.ppmFlag ='Y'")
	void updatePpmFlagAsY();
	
}
