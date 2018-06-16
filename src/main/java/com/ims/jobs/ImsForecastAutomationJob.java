package com.ims.jobs;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ImsForecastAutomationJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Job triggered to run forecast");
		String url = "http://192.168.204.13:3004/forecasting/deloitte";
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getForObject(url, String.class);
	}
}
