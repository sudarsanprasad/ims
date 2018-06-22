package com.ims.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.ims.entity.TicketMetadata;
import com.ims.entity.TicketStatistics;
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
	@Spy
	FTPService fTPService;
	
	@Test
	public void downloadExcel() throws ImsException {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setCustomer("Deloitte");
		when(ticketSystemRepository.findByCustomerAndEnableFlagAndType("Deloitte", "Y", "FTP")).thenReturn(getTicketSystemList());
		fTPService.downloadExcel(ticketSystem);
	}
	
	/*@Test
	public void getTicketStatistics() {
		fTPService.getTicketStatistics("abc.text","Service Now", "Deloitte");
	}*/
	
	@Test
	public void processFile() throws ImsException, SQLException {
		TicketStatistics ticketStatistics =constructTicketStatistics();
		Connection con=mock(Connection.class);
		Statement stmt=mock(Statement.class);
		doReturn(con).when(fTPService).getConnection();
		doReturn(stmt).when(con).createStatement();
		doReturn(ticketStatistics).when(fTPService).getTicketStatistics(anyString(),anyString(),anyString());
		when(ticketStatisticsRepository.save(ticketStatistics)).thenReturn(ticketStatistics);
		when(ticketMetadataRepository.findBySystemNameAndCustomerOrderById(anyString(),anyString())).thenReturn(constructTicketMetaDataList());
		fTPService.processFile("", "ServiceNow", "Deloite",new File("abc"), "C;/", "XLS", "test");
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
	
	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		return ticketStatistics;
	}
	
	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}
}
