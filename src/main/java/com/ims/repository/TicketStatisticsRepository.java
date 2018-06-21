package com.ims.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ims.entity.TicketStatistics;

public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Long> {
	
	List<TicketStatistics> findAllByOrderByJobIdDesc();
	
	List<TicketStatistics> findAllByAutomationStatusOrderByJobIdDesc(String automationStatus);
	
	List<TicketStatistics> findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc(String automationStatus, String forecastStatus);
	
	List<TicketStatistics> findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(String automationStatus, String knowledgeBaseStatus);

	TicketStatistics findByJobId(Long jobId);
	
	@Query("SELECT DISTINCT ts.customer FROM TicketStatistics ts where ts.automationStatus='COMPLETED' and ts.forecastStatus='OPEN'")
	List<String> findDistinctForecastCustomers();
	
	@Query("SELECT DISTINCT ts.customer FROM TicketStatistics ts where ts.automationStatus='COMPLETED' and ts.knowledgeBaseStatus='OPEN'")
	List<String> findDistinctKRCustomers();
	
	List<TicketStatistics> findAllByCustomerIn(List<String> customers);
	
	List<TicketStatistics> findDistinctByCustomer(String customer);
	
	List<TicketStatistics> findBySystemNameAndAutomationStartDateAndAutomationEndDate(String systemName,Date automationStartDate,Date automationEndDate);
}
