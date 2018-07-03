package com.ims.repository;

import java.util.List;

import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ims.entity.TicketPpm;

public interface TicketPpmRepository extends JpaRepository<TicketPpm, String> {
	
	List<TicketPpm> findByPpmFlag(String ppmFlag);
	
	List<TicketPpm> findByPpmFlagAndCreateDateIsAfter(String ppmFlag,Timestamp createDate);
	
	@Modifying(clearAutomatically = true)
	@Query("update TicketPpm tp set tp.lastSeenDate =:lastSeenDate, tp.ppmFlag ='Y'")
	void updatePpmFlagAsN(@Param("lastSeenDate") Timestamp lastSeenDate);
	
}
