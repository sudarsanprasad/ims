package com.ims.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;

@RunWith(MockitoJUnitRunner.class)
public class FTPServiceTest {

	@Mock
	private Environment env;

	@Mock
	TicketMetadataRepository ticketMetadataRepository;

	@Mock
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@Mock
	TicketSystemRepository ticketSystemRepository;
	
	@InjectMocks
	FTPService fTPService;
	
	@Test
	public void downloadExcel() throws ImsException {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setCustomer("Deloitte");
		when(ticketSystemRepository.findByCustomerAndEnableFlagAndType("Deloitte", "Y", "FTP")).thenReturn(getTicketSystemList());
		fTPService.downloadExcel(ticketSystem);
	}
	
	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		ticketSystem.setUrl("localhost:8080");
		ticketSystem.setUserName("admin");
		ticketSystem.setPassword("admin");
		return ticketSystem;
	}

	private List<TicketSystem> getTicketSystemList() {
		List<TicketSystem> ticketSystemList = new ArrayList<TicketSystem>();
		ticketSystemList.add(constructTicketSystem());
		return ticketSystemList;
	}
}
