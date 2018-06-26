package com.ims.jobs;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.ImsJobService;
import com.ims.service.TicketService;

@Slf4j
public class ImsDataAutomationJob implements Job {
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	ImsJobService imsJobService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Job triggered to get data");
		String customer = context.getTrigger().getKey().getGroup();
		String system = context.getTrigger().getKey().getName();
		TicketSystem ticketSystem = ticketSystemRepository.findBySystemNameAndCustomer(system, customer);
		
		try {
			if("Y".equalsIgnoreCase(ticketSystem.getEnableFlag())){
				if("API".equalsIgnoreCase(ticketSystem.getType())){
					ticketService.updateDataToHDFS(getRecords(ticketSystem), ticketSystem);
				}else{
					ftpService.downloadExcel(ticketSystem);
				}
				if("Y".equalsIgnoreCase(ticketSystem.getFirstTimeFlag())){
					imsJobService.triggerForecastModelScheduler(customer);
				}
			}
		} catch (ImsException e) {
			log.error("Exception == "+e);
		}
		ticketSystem.setFirstTimeFlag("N");
		ticketSystemRepository.save(ticketSystem);
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
	
}
