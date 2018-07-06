package com.ims.jobs;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.ImsJobService;
import com.ims.service.KrService;
import com.ims.service.TicketService;

@RunWith(MockitoJUnitRunner.class)
public class ImsDataAutomationJobTest {
	
	@Mock
	TicketSystemRepository ticketSystemRepository;
	
	@Mock
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Mock
	private TicketService ticketService;
	
	@Mock
	private FTPService ftpService;
	
	@Mock
	ImsJobService imsJobService;
	
	@Mock
	private KrService krService;
	
	@InjectMocks
	ImsDataAutomationJob imsDataAutomationJob;
	
	@Test
	public void execute() throws JobExecutionException, ImsException {
		TicketSystem ticketSystem = constructTicketSystem();
		ticketSystem.setType("FTP");
		JobExecutionContext context=mock(JobExecutionContext.class);
		Trigger trigger=mock(Trigger.class);
		//TriggerKey triggerKey=mock(TriggerKey.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(context.getTrigger()).thenReturn(trigger);
		when(trigger.getKey()).thenReturn(triggerKey);
		//when(triggerKey.getGroup()).thenReturn("Deloite");
		doNothing().when(ticketService).updateDataToHDFS(anyString(), anyObject());
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite","DEFAULT")).thenReturn(ticketSystem);
		when(ftpService.downloadExcel(ticketSystem)).thenReturn(true);
		imsDataAutomationJob.execute(context);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void executeException() throws JobExecutionException, ImsException {
		TicketSystem ticketSystem = constructTicketSystem();
		ticketSystem.setType("FTP");
		JobExecutionContext context=mock(JobExecutionContext.class);
		Trigger trigger=mock(Trigger.class);
		//TriggerKey triggerKey=mock(TriggerKey.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(context.getTrigger()).thenReturn(trigger);
		when(trigger.getKey()).thenReturn(triggerKey);
		//when(triggerKey.getGroup()).thenReturn("Deloite");
		doNothing().when(ticketService).updateDataToHDFS(anyString(), anyObject());
		when(ticketSystemRepository.findBySystemNameAndCustomer("Deloite","DEFAULT")).thenReturn(ticketSystem);
		when(ftpService.downloadExcel(ticketSystem)).thenThrow(ImsException.class);
		imsDataAutomationJob.execute(context);
	}
	
	@Test
	public void getUrl(){
		TicketSystem ticketSystem = constructTicketSystem();
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("06 43 19 77");
		when(imsConfigurationRepository.findByProperty(anyString())).thenReturn(imsConfiguration);
		imsDataAutomationJob.getUrl(ticketSystem);
	}
	
	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		ticketSystem.setEnableFlag("Y");
		ticketSystem.setFirstTimeFlag("Y");
		ticketSystem.setKkrFirstTimeFlag("Y");
		return ticketSystem;
	}

}
