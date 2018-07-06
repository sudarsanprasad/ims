package com.ims.service;

import static org.quartz.JobKey.jobKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.JobType;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketSystem;
import com.ims.jobs.JobDescriptor;
import com.ims.jobs.TriggerDescriptor;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImsJobService {
	
	private static final Logger LOG = Logger.getLogger(ImsJobService.class);
	
	private final Scheduler scheduler;
	
	@Autowired
	private Environment env;
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	public JobDescriptor createJobs(List<TicketSystem> ticketSystems) {
		
		if(!CollectionUtils.isEmpty(ticketSystems)){
			JobDescriptor descriptor = new JobDescriptor();
			for(TicketSystem system:ticketSystems){
				startScheduler(descriptor, system);
			}
			return descriptor;
		}
		return null;
	}


	private void startScheduler(JobDescriptor descriptor, TicketSystem system) {
		if("Y".equalsIgnoreCase(system.getEnableFlag())){
			List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();
			TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
			triggerDescriptor.setCron(system.getAutomationCronValue());
			triggerDescriptor.setGroup(system.getCustomer());
			triggerDescriptor.setName(system.getSystemName());
			
			triggerDescriptors.add(triggerDescriptor);
			descriptor.setTriggerDescriptors(triggerDescriptors);
			descriptor.setGroup(system.getCustomer());
			descriptor.setName(system.getSystemName());
			JobDetail jobDetail = descriptor.buildJobDetail();
			Set<Trigger> triggersForJob = descriptor.buildTriggers();
			log.info("About to save job with key - { }", jobDetail.getKey());
			try {
				scheduler.scheduleJob(jobDetail, triggersForJob, false);
				log.info("Job with key - { } saved sucessfully", jobDetail.getKey());
			} catch (SchedulerException e) {
				LOG.info(e);
				log.info("Data job is already running");
			}
		}
	}
	
	@Transactional(readOnly = true)
	public Optional<JobDescriptor> findJob(String group, String name) {
		// @formatter:off
		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey(name, group));
			if(Objects.nonNull(jobDetail))
				return Optional.of(
						JobDescriptor.buildDescriptor(jobDetail, 
								scheduler.getTriggersOfJob(jobKey(name, group))));
		} catch (SchedulerException e) {
			log.info("S Exception "+e);
			log.error("Could not find job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
		// @formatter:on
		log.warn("Could not find job with key - {}.{}", group, name);
		return Optional.empty();
	}
	
	public void updateJob(String group, String name) {
		try {
			JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(name, group));
			if(Objects.nonNull(oldJobDetail)) {
				JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
				JobBuilder jb = oldJobDetail.getJobBuilder();
				JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
				scheduler.addJob(newJobDetail, true);
				log.info("Updated job with key - {}", newJobDetail.getKey());
				return;
			}
			log.warn("Could not find job with key - {}.{} to update", group, name);
		} catch (SchedulerException e) {
			log.info("SCH Exception "+e);
			log.error("Could not find job with key - {}.{} to update due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
	
	public void deleteJob(String group, String name) {
		try {
			scheduler.deleteJob(jobKey(name, group));
			log.info("Deleted job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("Exception Log"+e);
			log.error("Could not delete job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
	
	public void pauseJob(String group, String name) {
		try {
			TicketSystem system = ticketSystemRepository.findBySystemNameAndCustomer(name, group);
			system.setEnableFlag("N");
			ticketSystemRepository.save(system);
			scheduler.pauseJob(jobKey(name, group));
			log.info("Paused job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("Scheduler Exception "+e);
			log.error("Could not pause job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
	
	public void resumeJob(String group, String name) {
		try {
			TicketSystem system = ticketSystemRepository.findBySystemNameAndCustomer(name, group);
			system.setEnableFlag("Y");
			ticketSystemRepository.save(system);
			scheduler.resumeJob(jobKey(name, group));
			log.info("Resumed job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("SchedulerException "+e);
			log.error("Could not resume job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
	
	public void triggerForecastModelScheduler(String customerName){
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("forecast.model.status");
		imsConfiguration.setValue("INPROGRESS");
		imsConfigurationRepository.save(imsConfiguration);
		String location = env.getProperty("forecast.model.url");
		StringBuilder url = new StringBuilder(location).append(customerName);
		LOG.info("Forecast model URL ==>> "+url.toString());
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url.toString(), String.class);
		LOG.info("fore cast model building status ===>> "+result);
		if("Success".equalsIgnoreCase(result)){
			ticketSystemRepository.updateFirstTimeFlagAsN(customerName);
			imsConfiguration.setValue("COMPLETED");
		}else{
		imsConfiguration.setValue("FAILED");
		}
		imsConfigurationRepository.save(imsConfiguration);
	}
	
	public TriggerState statusJob(String group, String name) {
		TriggerState state = null;
		try {
			TriggerKey triggerKey = new TriggerKey(group, name);
			state = scheduler.getTriggerState(triggerKey);
			log.info("Resumed job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("SchedulerException "+e);
			log.error("Could not resume job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
		return state;
	}
	
	public JobDescriptor createForecastJob() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("forecast.cronvalue");
		JobDescriptor forecastDescriptor = new JobDescriptor();
		List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();
		TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
		triggerDescriptor.setCron(configuration.getValue());
		triggerDescriptor.setGroup(JobType.FORECAST.getDescription());
		triggerDescriptor.setName(JobType.FORECAST.getDescription());
		
		triggerDescriptors.add(triggerDescriptor);
		forecastDescriptor.setTriggerDescriptors(triggerDescriptors);
		forecastDescriptor.setGroup(JobType.FORECAST.getDescription());
		forecastDescriptor.setName(JobType.FORECAST.getDescription());
		JobDetail jobDetail = forecastDescriptor.buildForecastJobDetail();
		Set<Trigger> triggersForJob = forecastDescriptor.buildTriggers();
		log.info("Forecast About to save job with key - {}", jobDetail.getKey());
		try {
			scheduler.scheduleJob(jobDetail, triggersForJob, false);
			log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
		} catch (SchedulerException e) {
			LOG.info(e);
			log.info("Forecast job is already running");
		}
		return forecastDescriptor;
	}
	
	public JobDescriptor createKrJob() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("forecast.cronvalue");
		JobDescriptor krDescriptor = new JobDescriptor();
		List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();
		TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
		triggerDescriptor.setCron(configuration.getValue());
		triggerDescriptor.setGroup(JobType.KR.getDescription());
		triggerDescriptor.setName(JobType.KR.getDescription());
		
		triggerDescriptors.add(triggerDescriptor);
		krDescriptor.setTriggerDescriptors(triggerDescriptors);
		krDescriptor.setGroup(JobType.KR.getDescription());
		krDescriptor.setName(JobType.KR.getDescription());
		JobDetail jobDetail = krDescriptor.buildKrJobDetail();
		Set<Trigger> triggersForJob = krDescriptor.buildTriggers();
		log.info("KR About to save job with key - {}", jobDetail.getKey());
		try {
			scheduler.scheduleJob(jobDetail, triggersForJob, false);
			log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
		} catch (SchedulerException e) {
			LOG.info(e);
			log.info("KR job is already running");
		}
		return krDescriptor;
	}
	
}
