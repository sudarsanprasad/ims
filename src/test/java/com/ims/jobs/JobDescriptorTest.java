package com.ims.jobs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;

@RunWith(MockitoJUnitRunner.class)
public class JobDescriptorTest {
	
	@InjectMocks
	JobDescriptor jobDescriptor;
	
	@InjectMocks
	KrJobDescriptor KrJobDescriptor;
	
	@InjectMocks
	ForecastJobDescriptor forecastJobDescriptor;
	
	@Test
	public void buildDescriptor() {
		List<Trigger> triggersOfJob=new ArrayList<>();
		Trigger trigger=mock(Trigger.class);
		JobDataMap jobDataMap=mock(JobDataMap.class);
		triggersOfJob.add(trigger);
		JobDetailImpl  jobDetail =mock(JobDetailImpl.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(trigger.getKey()).thenReturn(triggerKey);
		when(trigger.getJobDataMap()).thenReturn(jobDataMap);
		when(jobDataMap.get("fireTime")).thenReturn(LocalDateTime.now());
		when(jobDetail.getKey()).thenReturn(new JobKey("test"));
		jobDescriptor.buildDescriptor(jobDetail, triggersOfJob);
	}
	
	@Test
	public void buildDescriptorKR() {
		List<Trigger> triggersOfJob=new ArrayList<>();
		Trigger trigger=mock(Trigger.class);
		JobDataMap jobDataMap=mock(JobDataMap.class);
		triggersOfJob.add(trigger);
		JobDetailImpl  jobDetail =mock(JobDetailImpl.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(trigger.getKey()).thenReturn(triggerKey);
		when(trigger.getJobDataMap()).thenReturn(jobDataMap);
		when(jobDataMap.get("fireTime")).thenReturn(LocalDateTime.now());
		when(jobDetail.getKey()).thenReturn(new JobKey("test"));
		KrJobDescriptor.buildDescriptor(jobDetail, triggersOfJob);
	}
	
	@Test
	public void buildDescriptorForeCast() {
		List<Trigger> triggersOfJob=new ArrayList<>();
		Trigger trigger=mock(Trigger.class);
		JobDataMap jobDataMap=mock(JobDataMap.class);
		triggersOfJob.add(trigger);
		JobDetailImpl  jobDetail =mock(JobDetailImpl.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(trigger.getKey()).thenReturn(triggerKey);
		when(trigger.getJobDataMap()).thenReturn(jobDataMap);
		when(jobDataMap.get("fireTime")).thenReturn(LocalDateTime.now());
		when(jobDetail.getKey()).thenReturn(new JobKey("test"));
		forecastJobDescriptor.buildDescriptor(jobDetail, triggersOfJob);
	}

}
