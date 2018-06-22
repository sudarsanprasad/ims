package com.ims.jobs;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

public class ImsForecastAutomationJob implements Job {
	
	private static final Logger LOG = Logger.getLogger(ImsForecastAutomationJob.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<String> customers = ticketStatisticsRepository.findDistinctForecastCustomers();
		List<TicketStatistics> systems = ticketStatisticsRepository.findAllByCustomerIn(customers);
		if(!CollectionUtils.isEmpty(systems)){
			String forecastUrl = env.getProperty("forecast.url");
			StringBuilder url = new StringBuilder(forecastUrl);
			RestTemplate restTemplate = new RestTemplate();
			for(TicketStatistics system:systems){
				url.append(system.getCustomer());
				runForecast(url, restTemplate, system);
				
			}
		}
		
		
	}

	private void runForecast(StringBuilder url, RestTemplate restTemplate, TicketStatistics system) {
		try{
			system.setForecastStatus(StatusType.INPROGRESS.getDescription());
			ticketStatisticsRepository.save(system);
			String result = restTemplate.getForObject(url.toString(), String.class);
			if("Success".equalsIgnoreCase(result)){
				system.setForecastStatus(StatusType.COMPLETED.getDescription());
				ticketStatisticsRepository.save(system);
			}else{
				system.setForecastStatus(StatusType.INPROGRESS.getDescription());
				ticketStatisticsRepository.save(system);
			}
		}catch(Exception ex){
			LOG.info("Exception ===>> "+ex);
			system.setForecastStatus(StatusType.FAILED.getDescription());
			ticketStatisticsRepository.save(system);
		}
	}
}
