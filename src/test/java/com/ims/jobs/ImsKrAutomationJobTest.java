package com.ims.jobs;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.env.Environment;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketStatistics;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.TicketService;

@RunWith(MockitoJUnitRunner.class)
public class ImsKrAutomationJobTest {

	@Mock
	private Environment env;

	@Mock
	TicketSystemRepository ticketSystemRepository;

	@Mock
	ImsConfigurationRepository imsConfigurationRepository;

	@Mock
	private TicketService ticketService;

	@Mock
	private FTPService ftpService;

	@Mock
	private TicketStatisticsRepository ticketStatisticsRepository;

	@Spy
	@InjectMocks
	ImsKrAutomationJob imsKrAutomationJob;

	@Test(expected=Exception.class)
	public void execute() throws JobExecutionException {
		JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
		TicketStatistics ticketStatistics = constructTicketStatistics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		doReturn(ticketStatisticsList).when(ticketStatisticsRepository)
				.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(Mockito.anyString(),
						Mockito.anyString());
		doReturn("http://localhost:3001/kr/build_kr").when(env).getProperty("kr.url");
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("forecast.model.status")).thenReturn(imsConfiguration);
		imsKrAutomationJob.execute(context);
	}

	@Test
	public void executeEmptyList() throws JobExecutionException {
		JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		// ticketStatisticsList.add(ticketStatistics);
		doReturn(ticketStatisticsList).when(ticketStatisticsRepository)
				.findAllByAutomationStatusAndKnowledgeBaseStatusOrderByJobIdDesc(Mockito.anyString(),
						Mockito.anyString());
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("forecast.model.status")).thenReturn(imsConfiguration);
		imsKrAutomationJob.execute(context);
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
