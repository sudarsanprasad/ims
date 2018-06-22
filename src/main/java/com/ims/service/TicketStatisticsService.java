package com.ims.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

@Service
public class TicketStatisticsService {

	@Autowired
    private TicketStatisticsRepository ticketStatisticsRepository;
	
	public TicketStatistics create(TicketStatistics ticketStatistics) {
		return ticketStatisticsRepository.save(ticketStatistics);
	}

	public TicketStatistics update(TicketStatistics ticketStatistics) {
		return ticketStatisticsRepository.save(ticketStatistics);
	}
	
	public TicketStatistics findById(Long ticketStatisticsId) {
		return ticketStatisticsRepository.findOne(ticketStatisticsId);
	}

	public void delete(Long ticketStatisticsId) {
		ticketStatisticsRepository.delete(ticketStatisticsId);
		
	}

	public List<TicketStatistics> findAll() {
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "jobId"));
		return ticketStatisticsRepository.findAll(sort);
	}
	
	public List<TicketStatistics> findAllByFileNameOrderByJobId(){
		return ticketStatisticsRepository.findAllByOrderByJobIdDesc();
	}
	
	public TicketStatistics findMostRecentRecord(){
		TicketStatistics ticketStatistics = null;
		List<TicketStatistics> ticketStatisticsList = ticketStatisticsRepository.findAllByOrderByJobIdDesc();
		if(!CollectionUtils.isEmpty(ticketStatisticsList)){
			ticketStatistics = ticketStatisticsList.get(0);
		}
		return ticketStatistics;
	}

	public List<TicketStatistics> getCurrentRecords() {
		return ticketStatisticsRepository.findAllByAutomationStatusOrderByJobIdDesc(StatusType.INPROGRESS.getDescription());
	}

	public TicketStatistics getCurrentRecordStatus(Long id) {
		return ticketStatisticsRepository.findByJobId(id);
	}

	public List<TicketStatistics> getStatistics(String customerName) {
		return ticketStatisticsRepository.findDistinctByCustomer(customerName);
	}

	public List<String> getSystemNames() {
		return ticketStatisticsRepository.findDistinctSystems();
	}

	public List<TicketStatistics> getStatistics(TicketStatistics ticketStatistics) {
		return ticketStatisticsRepository.findAll(Example.of(ticketStatistics));
	}
	
}
