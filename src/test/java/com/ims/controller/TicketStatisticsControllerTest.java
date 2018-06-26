package com.ims.controller;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.TicketStatistics;
import com.ims.service.TicketStatisticsService;

@RunWith(MockitoJUnitRunner.class)
public class TicketStatisticsControllerTest {

	@InjectMocks
	TicketStatisticsController ticketStatisticsController;

	@Mock
	TicketStatisticsService ticketStatisticsService;

	@Test
	public void createStatistics() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsService.create(ticketStatistics)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsController.createStatistics(ticketStatistics);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void updateStatistics() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsService.create(ticketStatistics)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsController.updateStatistics(ticketStatistics);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void getStatistics() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsService.findById(10L)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsController.getStatistics(10L);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void findAll() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsService.findAll()).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsController.findAll();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void findAllByFileNameOrderByJobId() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsService.findAllByFileNameOrderByJobId()).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsController.findAllByFileNameOrderByJobId();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void findMostRecentRecord() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsService.findMostRecentRecord()).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsController.findMostRecentRecord();
		notNull(ticketStatisticsResp);
	}

	@Test
	public void getCurrentRecords() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsService.getCurrentRecords()).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsController.getCurrentRecords();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void getCurrentRecordStatus() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsService.getCurrentRecordStatus(10L)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsController.getCurrentRecordStatus(10L);
		notNull(ticketStatisticsResp);
	}
	
	@Test
	public void getStatisticsByCustomer() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsService.getStatistics(anyString())).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsController.getStatistics("customer");
		notEmpty(ticketStatisticsListResp);
	}
	
	@Test
	public void getSystemNames() {
		List<String> listOfSystemNames=new ArrayList<>();
		listOfSystemNames.add("ServiceNow");
		listOfSystemNames.add("AMPM");
		when(ticketStatisticsService.getSystemNames()).thenReturn(listOfSystemNames);
		List<String> listOfSystemNamesResp = ticketStatisticsController.getSystemNames();
		notEmpty(listOfSystemNamesResp);
	}

	private TicketStatistics constructTicketStatostics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		return ticketStatistics;
	}

}
