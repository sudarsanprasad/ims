package com.ims.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ims.service.PpmService;

public class ImsPpmAutomationJob implements Job {
	
	
	@Autowired
	private PpmService ppmService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ppmService.triggerPpm();
	}
	
}
