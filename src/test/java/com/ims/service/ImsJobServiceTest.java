package com.ims.service;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.JobDetailImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketSystem;
import com.ims.jobs.JobDescriptor;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ImsJobServiceTest {
	
	@InjectMocks
	ImsJobService imsJobService;

	@Mock
	TicketSystemRepository ticketSystemRepository;
	
	@Mock
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Mock
	Scheduler scheduler;
	
	@Test
	public void createJobs() {
		notNull(imsJobService.createJobs(getTicketSystemList()));
	}
	
	@Test
	public void createJobsEmtyList() {
		isNull(imsJobService.createJobs(new ArrayList<TicketSystem>()));
	}
	
	@Test
	public void createJob() {
		JobDescriptor descriptor=new JobDescriptor().setName("Deloite");
		notNull(imsJobService.createJob("Deloite", descriptor));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public void createJobException() throws SchedulerException {
		JobDescriptor descriptor=new JobDescriptor().setName("Deloite");
		doThrow(new SchedulerException()).when(scheduler).scheduleJob(anyObject(),anySet(),anyBoolean());
		imsJobService.createJob("Deloite", descriptor);
	}
	
	@Test
	public void findJob() {
		notNull(imsJobService.findJob("Deloite", "Deloite"));
	}
	
	@Test(expected=Exception.class)
	public void findJobJobDetail() throws SchedulerException {
		JobDetail  jobDetail =new JobDetailImpl();
		when(scheduler.getJobDetail(anyObject())).thenReturn(jobDetail);
		notNull(imsJobService.findJob("Deloite", "Deloite"));
	}
	
	@Test
	public void findJobException() throws SchedulerException {
		doThrow(new SchedulerException()).when(scheduler).getJobDetail(anyObject());
		notNull(imsJobService.findJob("Deloite", "Deloite"));
	}
	
	@Test
	public void updateJob() throws SchedulerException {
		JobDetailImpl  jobDetail =mock(JobDetailImpl.class);
		when(scheduler.getJobDetail(anyObject())).thenReturn(jobDetail);
		when(jobDetail.getJobDataMap()).thenReturn(new JobDataMap());
		JobBuilder jobBuilder=mock(JobBuilder.class);
		when(jobDetail.getJobBuilder()).thenReturn(jobBuilder);
		when(jobBuilder.usingJobData(anyObject())).thenReturn(jobBuilder);
		when(jobBuilder.storeDurably()).thenReturn(jobBuilder);
		when(jobBuilder.build()).thenReturn(jobDetail);
		when(jobDetail.getKey()).thenReturn(new JobKey("test"));
		imsJobService.updateJob("Deloite", "Deloite");
	}
	
	@Test
	public void updateJobJobDetailAsNull() throws SchedulerException {
		when(scheduler.getJobDetail(anyObject())).thenReturn(null);
		imsJobService.updateJob("Deloite", "Deloite");
	}
	
	@Test
	public void updateJobJobDetailException() throws SchedulerException {
		doThrow(new SchedulerException()).when(scheduler).getJobDetail(anyObject());
		imsJobService.updateJob("Deloite", "Deloite");
	}
	
	@Test
	public void deleteJob() throws SchedulerException {
		when(scheduler.deleteJob(anyObject())).thenReturn(true);
		imsJobService.deleteJob("Deloite", "Deloite");
	}
	
	@Test
	public void deleteJobException() throws SchedulerException {
		doThrow(new SchedulerException()).when(scheduler).deleteJob(anyObject());
		imsJobService.deleteJob("Deloite", "Deloite");
	}
	
	@Test
	public void pauseJob() throws SchedulerException, NoSuchFieldException, SecurityException, ClassNotFoundException {
		ReflectionTestUtils.setField(imsJobService, "ticketSystemRepository", ticketSystemRepository);
		TicketSystem ticketSystem=constructTicketSystem();
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite", "Deloite")).thenReturn(ticketSystem);
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		doNothing().when(scheduler).pauseJob(anyObject());
		imsJobService.pauseJob("Deloite", "Deloite");
	}
	
	@Test
	public void pauseJobException() throws SchedulerException {
		ReflectionTestUtils.setField(imsJobService, "ticketSystemRepository", ticketSystemRepository);
		TicketSystem ticketSystem=constructTicketSystem();
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite", "Deloite")).thenReturn(ticketSystem);
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		doThrow(new SchedulerException()).when(scheduler).pauseJob(anyObject());
		imsJobService.pauseJob("Deloite", "Deloite");
	}
	
	@Test
	public void resumeJob() throws SchedulerException, NoSuchFieldException, SecurityException, ClassNotFoundException {
		ReflectionTestUtils.setField(imsJobService, "ticketSystemRepository", ticketSystemRepository);
		TicketSystem ticketSystem=constructTicketSystem();
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite", "Deloite")).thenReturn(ticketSystem);
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		doNothing().when(scheduler).pauseJob(anyObject());
		imsJobService.resumeJob("Deloite", "Deloite");
	}
	
	@Test
	public void resumeJobException() throws SchedulerException {
		ReflectionTestUtils.setField(imsJobService, "ticketSystemRepository", ticketSystemRepository);
		TicketSystem ticketSystem=constructTicketSystem();
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite", "Deloite")).thenReturn(ticketSystem);
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		doThrow(new SchedulerException()).when(scheduler).resumeJob(anyObject());
		imsJobService.resumeJob("Deloite", "Deloite");
	}
	
/*	@Test
	public void triggerForecastModelScheduler() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		ReflectionTestUtils.setField(imsJobService, "imsConfigurationRepository", imsConfigurationRepository);
		RestTemplate restTemplate=Mockito.mock(RestTemplate.class);
		ReflectionTestUtils.setField(imsJobService, "restTemplate", restTemplate);
		when(imsConfigurationRepository.findByProperty("forecast.model.status")).thenReturn(imsConfiguration);
		when(imsConfigurationRepository.save(imsConfiguration)).thenReturn(imsConfiguration);
		imsJobService.triggerForecastModelScheduler("Deloite");
	}*/
	
	@Test
	public void statusJob() throws SchedulerException {
		when(scheduler.getTriggerState(anyObject())).thenReturn(TriggerState.NORMAL);
		imsJobService.statusJob("Deloite", "Deloite");
	}
	
	@Test
	public void statusJobException() throws SchedulerException {
		doThrow(new SchedulerException()).when(scheduler).getTriggerState(anyObject());
		imsJobService.statusJob("Deloite", "Deloite");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createForecastJob() throws SchedulerException {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("0 0 12 * * ?");
		ReflectionTestUtils.setField(imsJobService, "imsConfigurationRepository", imsConfigurationRepository);
		when(imsConfigurationRepository.findByProperty("forecast.cronvalue")).thenReturn(imsConfiguration);
		doNothing().when(scheduler).scheduleJob(anyObject(),anySet(),anyBoolean());
		imsJobService.createForecastJob();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=Exception.class)
	public void createForecastJobException() throws SchedulerException {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("0 0 12 * * ?");
		ReflectionTestUtils.setField(imsJobService, "imsConfigurationRepository", imsConfigurationRepository);
		when(imsConfigurationRepository.findByProperty("forecast.cronvalue")).thenReturn(imsConfiguration);
		doThrow(new SchedulerException()).when(scheduler).scheduleJob(anyObject(),anySet(),anyBoolean());
		imsJobService.createForecastJob();
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void createKrJob() throws SchedulerException {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("0 0 12 * * ?");
		ReflectionTestUtils.setField(imsJobService, "imsConfigurationRepository", imsConfigurationRepository);
		when(imsConfigurationRepository.findByProperty("forecast.cronvalue")).thenReturn(imsConfiguration);
		doNothing().when(scheduler).scheduleJob(anyObject(),anySet(),anyBoolean());
		imsJobService.createKrJob();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=Exception.class)
	public void createKrJobException() throws SchedulerException {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("0 0 12 * * ?");
		ReflectionTestUtils.setField(imsJobService, "imsConfigurationRepository", imsConfigurationRepository);
		when(imsConfigurationRepository.findByProperty("forecast.cronvalue")).thenReturn(imsConfiguration);
		doThrow(new SchedulerException()).when(scheduler).scheduleJob(anyObject(),anySet(),anyBoolean());
		imsJobService.createKrJob();
	}
	
	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		if(ticketSystem.getId()==null) {
		ticketSystem.setId(10L);
		}
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		ticketSystem.setEnableFlag("Y");
		ticketSystem.setAutomationCronValue("0 0 12 * * ?");
		if(ticketSystem.getFieldsMask()==null) {
		ticketSystem.setFieldsMask(null);
		}
		if(ticketSystem.getFirstTimeFlag()==null) {
		ticketSystem.setFirstTimeFlag("Y");
		}
		if(ticketSystem.getForecastCronValue()==null) {
		ticketSystem.setForecastCronValue("0 0 12 * * ?");
		}
		if(ticketSystem.getKrCronValue()==null) {
		ticketSystem.setKrCronValue("0 0 12 * * ?");
		}
		if(ticketSystem.getLastRunDate()==null) {
		ticketSystem.setLastRunDate("10-10-2018");
		}
		ticketSystem.setPassword("admin");
		ticketSystem.setUserName("admin");
		if(ticketSystem.getType()==null) {
		ticketSystem.setType("API");
		}
		ticketSystem.setUrl("localhost:8080");
		ticketSystem.equals(new TicketSystem());
		ticketSystem.toString();
		ticketSystem.hashCode();
		return ticketSystem;
	}

	private List<TicketSystem> getTicketSystemList() {
		List<TicketSystem> ticketSystemList = new ArrayList<TicketSystem>();
		ticketSystemList.add(constructTicketSystem());
		return ticketSystemList;
	}
}
