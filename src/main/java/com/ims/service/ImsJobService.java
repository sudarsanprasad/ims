package com.ims.service;

import static org.quartz.JobKey.jobKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ims.entity.TicketSystem;
import com.ims.jobs.JobDescriptor;
import com.ims.jobs.TriggerDescriptor;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImsJobService {
	private final Scheduler scheduler;
	
	public JobDescriptor createJobs(List<TicketSystem> ticketSystems) {
		
		if(!CollectionUtils.isEmpty(ticketSystems)){
			JobDescriptor descriptor = new JobDescriptor();
			for(TicketSystem system:ticketSystems){
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
					log.info("About to save job with key - {}", jobDetail.getKey());
					try {
						scheduler.scheduleJob(jobDetail, triggersForJob, false);
						log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
					} catch (SchedulerException e) {
						log.info("Exception "+e);
						log.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
						throw new IllegalArgumentException(e.getLocalizedMessage());
					}
				}
			}
			
			
			return descriptor;
		}
		return null;
	}
	
	
	public JobDescriptor createJob(String group, JobDescriptor descriptor) {
		descriptor.setGroup(group);
		JobDetail jobDetail = descriptor.buildJobDetail();
		Set<Trigger> triggersForJob = descriptor.buildTriggers();
		log.info("About to save job with key - {}", jobDetail.getKey());
		try {
			scheduler.scheduleJob(jobDetail, triggersForJob, false);
			log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
		} catch (SchedulerException e) {
			log.info("Exception details"+e);
			log.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return descriptor;
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
	
	public void updateJob(String group, String name, JobDescriptor descriptor) {
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
			scheduler.pauseJob(jobKey(name, group));
			log.info("Paused job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("Scheduler Exception "+e);
			log.error("Could not pause job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
	
	public void resumeJob(String group, String name) {
		try {
			scheduler.resumeJob(jobKey(name, group));
			log.info("Resumed job with key - {}.{}", group, name);
		} catch (SchedulerException e) {
			log.info("SchedulerException "+e);
			log.error("Could not resume job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
		}
	}
}
