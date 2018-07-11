package com.ims.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.ImsConfiguration;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;

@Service
public class PpmService {
	
	private static final Logger LOG = Logger.getRootLogger();
	
	@Autowired
	private Environment env;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	private TicketStatisticsRepository ticketStatisticsRepository;
	
	public void triggerPpm(){
		String url = env.getProperty("ppm.url");
		LOG.info("PPM URL ==>> "+url);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);
		LOG.info("Result from PPM ==>> "+result);
		updateConfiguration(result);
		
	}

	public void updateConfiguration(String result) {
		if("Success".equalsIgnoreCase(result)){
			ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("ppm.scheduler.status");
			imsConfiguration.setValue("COMPLETED");
			imsConfigurationRepository.save(imsConfiguration);
		}
	}

}
