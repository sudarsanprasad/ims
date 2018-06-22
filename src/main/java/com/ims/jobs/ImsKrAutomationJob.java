package com.ims.jobs;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketStatistics;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.TicketService;

@Slf4j
public class ImsKrAutomationJob implements Job {
	
	@Autowired
	private Environment env;
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	private TicketStatisticsRepository ticketStatisticsRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Job triggered to run KR");
		List<TicketStatistics> list = ticketStatisticsRepository.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(StatusType.COMPLETED.getDescription(), StatusType.OPEN.getDescription());
		if(CollectionUtils.isEmpty(list)){
			for(TicketStatistics record : list){
				log.info("Calling KR API for file "+record.getFileName());
				String url = (String)env.getProperty("kr.url");
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getForObject(url, String.class);
			}
		}
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("forecast.model.status");
		imsConfiguration.setValue("COMPLETED");
		imsConfigurationRepository.save(imsConfiguration);
	}
	
}
