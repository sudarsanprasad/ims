package com.ims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return ticketStatisticsRepository.findAll();
	}
	
}
