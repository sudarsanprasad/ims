package com.ims.taskconfig;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.jobs.JobDescriptor;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.ImsJobService;

@RunWith(MockitoJUnitRunner.class)
public class JobSchedulerTest {

	@Mock
	TicketSystemRepository ticketSystemRepository;

	@Mock
	ImsJobService imsJobService;

	@InjectMocks
	JobScheduler jobScheduler;

	@Test
	public void createJobs() throws ImsException {
		List<TicketSystem> ticketSystems = getTicketSystemList();
		when(imsJobService.createJobs(ticketSystems)).thenReturn(new JobDescriptor());
		ReflectionTestUtils.setField(imsJobService, "ticketSystemRepository", ticketSystemRepository);
		ReflectionTestUtils.setField(jobScheduler, "imsJobService", imsJobService);
		jobScheduler.createJobs();
	}

	@Test
	public void runForecastScheduler() throws ImsException {
		ReflectionTestUtils.setField(jobScheduler, "imsJobService", imsJobService);
		when(imsJobService.createForecastJob()).thenReturn(new JobDescriptor());
		jobScheduler.runForecastScheduler();
	}

	@Test
	public void runKRScheduler() throws ImsException {
		when(imsJobService.createKrJob()).thenReturn(new JobDescriptor());
		ReflectionTestUtils.setField(jobScheduler, "imsJobService", imsJobService);
		jobScheduler.runKRScheduler();
	}

	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		return ticketSystem;
	}

	private List<TicketSystem> getTicketSystemList() {
		List<TicketSystem> ticketSystemList = new ArrayList<TicketSystem>();
		ticketSystemList.add(constructTicketSystem());
		return ticketSystemList;
	}
}
