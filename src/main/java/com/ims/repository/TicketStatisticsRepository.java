package com.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketStatistics;

public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Long> {
	
}
