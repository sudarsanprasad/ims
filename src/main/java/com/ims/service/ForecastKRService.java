package com.ims.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

/**
 * 
 * @author RKB
 *
 */
@Service
public class ForecastKRService {

	private static final Logger LOG = Logger.getLogger(ForecastKRService.class);
	
	private TicketStatisticsRepository ticketStatisticsRepository;

	RestTemplate restTemplate = new RestTemplate();
	
	public void runForecastScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc("COMPLETED","OPEN");
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url", String.class);
			}
		}
	}

	public void runForecastModelScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc("COMPLETED","OPEN");
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url", String.class);
			}
		}
	}

	public void runKRScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc("COMPLETED","OPEN");
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url", String.class);
			}
		}
	}
}
