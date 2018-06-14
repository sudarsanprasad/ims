package com.ims.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

/**
 * 
 * @author RKB
 *
 */
@Service
public class ForecastKRService {

	private TicketStatisticsRepository ticketStatisticsRepository;

	RestTemplate restTemplate = new RestTemplate();
	
	public void runForecastScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc(StatusType.COMPLETED.getDescription(),StatusType.OPEN.getDescription());
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url"+customerName, String.class);
			}
		}
	}

	public void runForecastModelScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndForecastStatusOrderByJobIdDesc(StatusType.COMPLETED.getDescription(),StatusType.OPEN.getDescription());
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url"+customerName, String.class);
			}
		}
	}

	public void runKRScheduler() {
		List<TicketStatistics>  ticketStatistics = ticketStatisticsRepository.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(StatusType.COMPLETED.getDescription(),StatusType.OPEN.getDescription());
		if(!CollectionUtils.isEmpty(ticketStatistics)){
			for(TicketStatistics ticket:ticketStatistics){
				String customerName = ticket.getCustomer();
				restTemplate.getForObject("Url"+customerName, String.class);
			}
		}
	}
}
