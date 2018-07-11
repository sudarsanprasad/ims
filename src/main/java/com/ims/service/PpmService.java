package com.ims.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.ImsConfiguration;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DateUtil;

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
		StringBuilder url = new StringBuilder(env.getProperty("ppm.url"));
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("ppm.lastrundate");
		url.append(imsConfiguration.getValue());
		LOG.info("PPM URL ==>> "+url);
		String lastRunDate = DateUtil.convertDateToString(new Date());
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url.toString(), String.class);
		LOG.info("Result from PPM ==>> "+result);
		updateStatus(result);
		updateDate(result, lastRunDate, imsConfiguration);
	}

	public void updateStatus(String result) {
		if("Success".equalsIgnoreCase(result)){
			ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("ppm.scheduler.status");
			imsConfiguration.setValue("COMPLETED");
			imsConfigurationRepository.save(imsConfiguration);
		}
	}
	
	public void updateDate(String result, String lastRunDate, ImsConfiguration imsConfiguration) {
		if("Success".equalsIgnoreCase(result)){
			imsConfiguration.setValue(lastRunDate);
			imsConfigurationRepository.save(imsConfiguration);
		}
	}

}
