package com.ims.taskconfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.service.TicketService;
import com.ims.service.TicketStatisticsService;

@Component
public class ScheduledTasks {
	
	private static final Logger LOG = Logger.getLogger(ScheduledTasks.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketStatisticsService ticketStatisticsService;
	

	@Scheduled(cron = "${shedule.time.sec}")
	public void performTaskUsingCron() throws ImsException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		LOG.info("Regular task performed using Cron at "+ dateFormat.format(new Date()));
		TicketStatistics ticketStatistics = ticketService.updateTicketStatistics(getTicketStatistics());
		ticketService.updateTicketData(getRecords(), ticketStatistics);
	}
	
	private String getRecords() {
		 final String ticketURL = env.getProperty("ticketsystem.url");
		 RestTemplate restTemplate = new RestTemplate();
		 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(env.getProperty("servicenow.username"), env.getProperty("servicenow.password")));
		 return restTemplate.getForObject(ticketURL, String.class);
	}
	
	private TicketStatistics getTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName((String)env.getProperty("ticketsystem"));
		ticketStatistics.setCustomer((String)env.getProperty("customer"));
		ticketStatistics.setAutomationStatus(StatusType.OPEN.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setComments("Scheduler started successfully");
		return ticketStatistics;
	}
	
	
}
