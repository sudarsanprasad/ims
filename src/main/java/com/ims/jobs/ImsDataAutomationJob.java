package com.ims.jobs;

import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.ImsJobService;
import com.ims.service.KrService;
import com.ims.service.TicketService;
import com.ims.util.DateUtil;

@Slf4j
public class ImsDataAutomationJob implements Job {
	
	private static final Logger LOG = Logger.getAnonymousLogger();
	
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
	
	@Autowired
	private KrService krService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("Job triggered to get data");
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
				
				if("Y".equalsIgnoreCase(ticketSystem.getKkrFirstTimeFlag())){
					LOG.info("Trigger KR");
					krService.triggerKr();
				}
			}
		} catch (ImsException e) {
			log.error("Exception == "+e);
		}
		ticketSystem = ticketSystemRepository.findBySystemNameAndCustomer(system, customer);
		ticketSystem.setFirstTimeFlag("N");
		ticketSystem.setKkrFirstTimeFlag("N");
		ticketSystemRepository.save(ticketSystem);
		LOG.info("Job completed");
		
	}
	
	private String getRecords(TicketSystem ticketSystem) {
		 RestTemplate restTemplate = new RestTemplate();
		 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(ticketSystem.getUserName(), ticketSystem.getPassword()));
		 return restTemplate.getForObject(getUrl(ticketSystem), String.class);
	}
	
	private String getUrl(TicketSystem ticketSystem){
		StringBuilder builder = new StringBuilder();
		String systemName = ticketSystem.getSystemName().trim().toLowerCase();
		String system = systemName.trim();
		builder.append(system).append(".filter");
		LOG.info("FilterName ==>> "+builder.toString());
		ImsConfiguration systemFilter = imsConfigurationRepository.findByProperty(builder.toString());
		LOG.info("Filter Value from DB ==>> "+systemFilter.getValue());
		StringBuilder filterBuilder = new StringBuilder();
		filterBuilder.append(ticketSystem.getUrl()).append(systemFilter.getValue());
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("servicenow.lastrundate");
		String[] dateAndTime = DateUtil.getDateAndTime(configuration.getValue());
		filterBuilder.append("('").append(dateAndTime[0]).append("','").append(dateAndTime[1]).append("')");
		LOG.info("Service now URL  ==>> "+filterBuilder.toString());
		return filterBuilder.toString();
	}
	
}
