package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketStatistics;

public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Long> {
	
	List<TicketStatistics> findAllByOrderByJobIdDesc();
	
	List<TicketStatistics> findAllByAutomationStatusOrderByJobIdDesc(String automationStatus);
	
	List<TicketStatistics> findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc(String automationStatus, String forecastStatus);
	List<TicketStatistics> findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(String automationStatus, String knowledgeBaseStatus);

	TicketStatistics findByJobId(Long jobId);
	
}
