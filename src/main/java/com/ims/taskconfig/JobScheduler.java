package com.ims.taskconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.ForecastKRService;
import com.ims.service.ImsConfigurationService;
import com.ims.service.ImsJobService;
import com.ims.service.TicketService;
import com.ims.service.TicketStatisticsService;

@Component
public class JobScheduler {
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsJobService imsJobService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketStatisticsService ticketStatisticsService;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	ImsConfigurationService imsConfigurationService;
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	private ForecastKRService forecastKRService;
	
	//@Scheduled(cron = "0/60 * * * * ?")
	public void createJobs() throws ImsException {
		List<TicketSystem> ticketSystems =  ticketSystemRepository.findAll();
		imsJobService.createJobs(ticketSystems);
	}
	
	//@Scheduled(cron = "0/60 * * * * ?")
	public void runForecastScheduler() throws ImsException {
		forecastKRService.runForecastScheduler();
	}
	
	//@Scheduled(cron = "0/60 * * * * ?")
	public void runForecastModelScheduler() throws ImsException {
		forecastKRService.runForecastModelScheduler();
	}
	
	//@Scheduled(cron = "0/60 * * * * ?")
	public void runKRScheduler() throws ImsException {
		forecastKRService.runKRScheduler();
	}
		
}
