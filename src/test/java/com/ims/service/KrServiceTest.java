package com.ims.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.web.client.ResourceAccessException;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketStatistics;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;

@RunWith(MockitoJUnitRunner.class)
public class KrServiceTest {

	@Mock
	private Environment env;

	@Mock
	ImsConfigurationRepository imsConfigurationRepository;

	@Mock
	private TicketStatisticsRepository ticketStatisticsRepository;

	@InjectMocks
	private KrService krService;

	@Test(expected = ResourceAccessException.class)
	public void triggerKr() {

		TicketStatistics ticketStatistics = constructTicketStatistics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		doReturn(ticketStatisticsList).when(ticketStatisticsRepository)
				.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(Mockito.anyString(),
						Mockito.anyString());
		doReturn("http://localhost:3001/kr/build_kr").when(env).getProperty("kr.url");
		krService.triggerKr();
	}

	@Test
	public void updateConfiguration() {
		TicketStatistics record = constructTicketStatistics();
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("kr.build.status")).thenReturn(imsConfiguration);
		krService.updateConfiguration(record, "Success");
	}
	
	@Test
	public void updateConfigurationFAILED() {
		TicketStatistics record = constructTicketStatistics();
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("kr.build.status")).thenReturn(imsConfiguration);
		krService.updateConfiguration(record, "FAILED");
	}

	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		ticketStatistics.setAutomationStatus("COMPLETED");
		ticketStatistics.setKnowledgeBaseStatus("OPEN");
		return ticketStatistics;
	}

}