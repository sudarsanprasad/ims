package com.ims.service;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.TicketLogStatistics;
import com.ims.repository.TicketLogStatisticsRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketLogServiceTest {
	
	@InjectMocks
	private TicketLogService ticketLogService;
	
	@Mock
	private TicketLogStatisticsRepository ticketLogStatisticsRepository;

	@Test
	public void downloadLogStatistics() throws IOException {
		List<TicketLogStatistics> ticketLogStatisticsList = constructTicketLogisticsList();
		when(ticketLogStatisticsRepository.findByjobId(10L)).thenReturn(ticketLogStatisticsList);
		ticketLogService.downloadLogStatistics(10L);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void downloadLogStatisticsException() throws IOException {
		when(ticketLogStatisticsRepository.findByjobId(10L)).thenThrow(Exception.class);
		ticketLogService.downloadLogStatistics(10L);
	}

	private List<TicketLogStatistics> constructTicketLogisticsList() {
		List<TicketLogStatistics> ticketLogStatisticsList=new ArrayList<TicketLogStatistics>();
		TicketLogStatistics ticketLogStatistics1=new TicketLogStatistics();
		ticketLogStatistics1.setId(100L);
		ticketLogStatistics1.setJobId(10L);
		ticketLogStatistics1.setMessage("ticket");
		ticketLogStatistics1.setTicketId("121");
		TicketLogStatistics ticketLogStatistics2=new TicketLogStatistics();
		ticketLogStatistics2.setId(100L);
		ticketLogStatistics2.setJobId(10L);
		ticketLogStatistics2.setMessage("ticket");
		ticketLogStatistics2.setTicketId("121");
		ticketLogStatisticsList.add(ticketLogStatistics1);
		ticketLogStatisticsList.add(ticketLogStatistics2);
		return ticketLogStatisticsList;
	}
}
