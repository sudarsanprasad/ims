package com.ims.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.ims.constant.SourceType;
import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketStatisticsServiceTest {

	@InjectMocks
	private TicketStatisticsService ticketStatisticsService;

	@Mock
	private TicketStatisticsRepository ticketStatisticsRepository;
	
	@Mock
	private EntityManager entityManager;

	@Test
	public void create() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsRepository.save(ticketStatistics)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsService.create(ticketStatistics);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void update() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsRepository.save(ticketStatistics)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsService.update(ticketStatistics);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void findById() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsRepository.findOne(10L)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsService.findById(10L);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void delete() {
		doNothing().when(ticketStatisticsRepository).delete(10L);
		ticketStatisticsService.delete(10L);

	}

	@Test
	public void findAll() {
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "jobId"));
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsRepository.findAll(sort)).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsService.findAll();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void findAllByFileNameOrderByJobId() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsRepository.findAllByOrderByJobIdDesc()).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsService.findAllByFileNameOrderByJobId();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void findMostRecentRecord() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsRepository.findAllByOrderByJobIdDesc()).thenReturn(ticketStatisticsList);
		TicketStatistics ticketStatisticsResp = ticketStatisticsService.findMostRecentRecord();
		notNull(ticketStatisticsResp);
	}

	@Test
	public void getCurrentRecords() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsRepository
				.findAllByAutomationStatusOrderByJobIdDesc(StatusType.INPROGRESS.getDescription()))
						.thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsService.getCurrentRecords();
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void getCurrentRecordStatus() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		when(ticketStatisticsRepository.findByJobId(10L)).thenReturn(ticketStatistics);
		TicketStatistics ticketStatisticsResp = ticketStatisticsService.getCurrentRecordStatus(10L);
		notNull(ticketStatisticsResp);
	}

	@Test
	public void getStatisticsByCustomer() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		when(ticketStatisticsRepository.findDistinctByCustomer(anyString())).thenReturn(ticketStatisticsList);
		List<TicketStatistics> ticketStatisticsListResp = ticketStatisticsService.getStatistics("Delloite");
		notEmpty(ticketStatisticsListResp);
	}

	@Test
	public void getSystemNames() {
		List<String> listOfSystemNames = new ArrayList<String>();
		listOfSystemNames.add("Service Now");
		when(ticketStatisticsRepository.findDistinctSystems()).thenReturn(listOfSystemNames);
		List<String> ticketStatisticsListResp = ticketStatisticsService.getSystemNames();
		notEmpty(ticketStatisticsListResp);

	}

	@Test
	public void getStatisticsWithSourceNotNull() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		Query query=mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);
		@SuppressWarnings("unchecked")
		List<TicketStatistics> list=mock(List.class);
		when(query.getResultList()).thenReturn(list);
		ticketStatisticsService.getStatistics(ticketStatistics);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getStatisticsWithSourceAsNull() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		ticketStatistics.setSource(null);
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		Query query=mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);
		List<TicketStatistics> list=mock(List.class);
		when(query.getResultList()).thenReturn(list);
		ticketStatisticsService.getStatistics(ticketStatistics);
	}
	
	@Test
	public void getStatisticsWithSourceAndSystemNamesAsNull() {
		TicketStatistics ticketStatistics = constructTicketStatostics();
		ticketStatistics.setSource(null);
		ticketStatistics.setSystemNames(null);
		List<TicketStatistics> ticketStatisticsList = new ArrayList<TicketStatistics>();
		ticketStatisticsList.add(ticketStatistics);
		Query query=mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);
		@SuppressWarnings("unchecked")
		List<TicketStatistics> list=mock(List.class);
		when(query.getResultList()).thenReturn(list);
		ticketStatisticsService.getStatistics(ticketStatistics);
	}

	private TicketStatistics constructTicketStatostics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		ticketStatistics.setSource(SourceType.FTP.toString());
		List<String> systemNames =new ArrayList<>();
		systemNames.add("Service Now");
		ticketStatistics.setSystemNames(systemNames);
		Date automationStartDate=new Date();
		ticketStatistics.setAutomationStartDate(automationStartDate);
		Date automationEndDate=new Date();
		ticketStatistics.setAutomationEndDate(automationEndDate);
		return ticketStatistics;
	}

}
