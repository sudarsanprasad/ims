package com.ims.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketStatistics;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;

@Service
public class KrService {
	
	private static final Logger LOG = Logger.getRootLogger();
	
	@Autowired
	private Environment env;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	private TicketStatisticsRepository ticketStatisticsRepository;
	
	public void triggerKr(){
		
		LOG.info("Job triggered to run KR");
		List<TicketStatistics> list = ticketStatisticsRepository.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(StatusType.COMPLETED.getDescription(), StatusType.OPEN.getDescription());
		if(!CollectionUtils.isEmpty(list)){
			for(TicketStatistics record : list){
				if(StatusType.COMPLETED.getDescription().equalsIgnoreCase(record.getAutomationStatus()) && StatusType.OPEN.getDescription().equalsIgnoreCase(record.getKnowledgeBaseStatus())){
					LOG.info("Calling KR API for file "+record.getFileName());
					record.setKnowledgeBaseStatus(StatusType.INPROGRESS.getDescription());
					ticketStatisticsRepository.save(record);
					String url = env.getProperty("kr.url");
					LOG.info("KR URL ==>> "+url);
					RestTemplate restTemplate = new RestTemplate();
					String result = restTemplate.getForObject(url, String.class);
					LOG.info("Result from KR ==>> "+result);
					if("Success".equalsIgnoreCase(result)){
						record.setKnowledgeBaseStatus(StatusType.COMPLETED.getDescription());
						ticketStatisticsRepository.save(record);
						ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("kr.build.status");
						imsConfiguration.setValue("COMPLETED");
						imsConfigurationRepository.save(imsConfiguration);
					}
				}
			}
		}
		
	}

}
