package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketStatistics;

public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Long> {
	
	List<TicketStatistics> findAllByFileNameOrderByJobIdDesc(String fileName);
	
}
