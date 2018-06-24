package com.ims.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

	@InjectMocks
	private TicketService ticketService;
	
	@Mock
	private Environment env;

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private TicketMetadataRepository ticketMetadataRepository;

	@Mock
	TicketStatisticsRepository ticketStatisticsRepository;

	@Mock
	FieldConfigurationRepository fieldConfigurationRepository;

	@Mock
	ImsConfigurationRepository imsConfigurationRepository;

	@Mock
	TicketSystemRepository ticketSystemRepository;

	@Test(expected=Exception.class)
	public void updateTicketData() throws ImsException {
		TicketSystem ticketSystem = constructTicketSystem();
		TicketStatistics ticketStatistics = constructTicketStatistics();
		Mockito.doReturn(ticketStatistics).when(ticketStatisticsRepository).save(Mockito.any(TicketStatistics.class));
		ticketService.updateTicketData("[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]", ticketSystem);
	}
	
	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		return ticketSystem;
	}
	
	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		return ticketStatistics;
	}

}
