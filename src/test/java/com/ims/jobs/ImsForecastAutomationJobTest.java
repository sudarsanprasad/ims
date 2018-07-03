package com.ims.jobs;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

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
import org.springframework.web.client.RestTemplate;

import com.ims.constant.SourceType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

@RunWith(MockitoJUnitRunner.class)
public class ImsForecastAutomationJobTest {
	
	@Spy
	@InjectMocks
	ImsForecastAutomationJob imsForecastAutomationJob;

	@Mock
	private Environment env;

	@Mock
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@SuppressWarnings("unchecked")
	@Test
	public void execute() throws JobExecutionException{
		JobExecutionContext context=Mockito.mock(JobExecutionContext.class);
		//
		TicketStatistics ticketStatistics = constructTicketStatistics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		List<String> listOfCusotmers=new ArrayList<>();
		listOfCusotmers.add("Deloitte");
		doReturn(listOfCusotmers).when(ticketStatisticsRepository).findDistinctForecastCustomers();
		doReturn(ticketStatisticsList).when(ticketStatisticsRepository).findAllByCustomerIn(Mockito.anyList());
		doReturn("http://localhost:8080/forecasting/").when(env).getProperty("forecast.url");
		//doReturn(restTemplate).when(imsForecastAutomationJob).getRestTemplate();
		//Mockito.when(imsForecastAutomationJob.getRestTemplate()).thenReturn(restTemplate);
		Mockito.doReturn(ticketStatistics).when(ticketStatisticsRepository).save(Mockito.any(TicketStatistics.class));
		imsForecastAutomationJob.execute(context);
	}
	
	@Test
	public void runForecast() {
		RestTemplate restTemplate=Mockito.mock(RestTemplate.class);
		TicketStatistics ticketStatistics = constructTicketStatistics();
		StringBuilder url=new StringBuilder("testUrl");
		imsForecastAutomationJob.runForecast(url,restTemplate, ticketStatistics);
	}
	
	@Test
	public void runForecastResultAsSucess() {
		RestTemplate restTemplate=Mockito.mock(RestTemplate.class);
		doReturn("Success").when(restTemplate).getForObject(anyString(),any());
		TicketStatistics ticketStatistics = constructTicketStatistics();
		StringBuilder url=new StringBuilder("testUrl");
		imsForecastAutomationJob.runForecast(url,restTemplate, ticketStatistics);
	}
	
	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		ticketStatistics.setAutomationStatus("COMPLETED");
		ticketStatistics.setForecastStatus("OPEN");
		ticketStatistics.setSource(SourceType.API.toString());
		ticketStatistics.setSource(SourceType.FTP.toString());
		return ticketStatistics;
	}
}
