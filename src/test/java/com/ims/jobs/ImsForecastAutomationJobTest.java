package com.ims.jobs;

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
		//RestTemplate restTemplate=Mockito.mock(RestTemplate.class);
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
	
	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		return ticketStatistics;
	}
}
