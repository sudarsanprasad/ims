package com.ims.jobs;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.TicketService;

@Slf4j
public class ImsSystemJob implements Job {
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private FTPService ftpService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Job triggered to send emails");
		String customer = context.getTrigger().getKey().getGroup();
		String system = context.getTrigger().getKey().getName();
		TicketSystem ticketSystem = ticketSystemRepository.findBySystemNameAndCustomer(system, customer);
		TicketStatistics ticketStatistics = ticketService.updateTicketStatistics(getTicketStatistics(ticketSystem));
		try {
			if("API".equalsIgnoreCase(ticketSystem.getType())){
				ticketService.updateTicketData(getRecords(ticketSystem), ticketStatistics);
			}else{
				ftpService.downloadExcel();
			}
		} catch (ImsException e) {
			log.error("Exception == "+e);
		}
		log.info("Job completed");
	}
	
	private String getRecords(TicketSystem ticketSystem) {
		 RestTemplate restTemplate = new RestTemplate();
		 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(ticketSystem.getUserName(), ticketSystem.getPassword()));
		 return restTemplate.getForObject(getUrl(ticketSystem), String.class);
	}
	
	private String getUrl(TicketSystem ticketSystem){
		 return ticketSystem.getUrl();
	}
	
	private TicketStatistics getTicketStatistics(TicketSystem ticketSystem) {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName(ticketSystem.getSystemName());
		ticketStatistics.setCustomer(ticketSystem.getCustomer());
		ticketStatistics.setAutomationStatus(StatusType.OPEN.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setComments("Scheduler started successfully");
		return ticketStatistics;
	}
}
