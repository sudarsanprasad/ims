package com.ims.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.TicketPpm;
import com.ims.repository.TicketPpmRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketPpmServiceTest {

	@InjectMocks
	TicketPpmService ticketPpmService;

	@Mock
	private TicketPpmRepository ticketPpmRepository;

	@Test
	public void findByPpmFlag() {
		List<TicketPpm> ticketPpmList = new ArrayList<>();
		TicketPpm ticketPpm=new TicketPpm();
		ticketPpm.setData("{\"name\":\"John\", \"age\":31, \"city\":\"New York\" }");
		ticketPpmList.add(ticketPpm);
		doReturn(ticketPpmList).when(ticketPpmRepository).findByPpmFlagAndCreateDateIsAfter(anyString(),anyObject());
		ticketPpmService.findByPpmFlag();
	}
	
	@Test
	public void findByPpmFlagParseException() {
		List<TicketPpm> ticketPpmList = new ArrayList<>();
		TicketPpm ticketPpm=new TicketPpm();
		ticketPpm.setData("data");
		ticketPpmList.add(ticketPpm);
		doReturn(ticketPpmList).when(ticketPpmRepository).findByPpmFlagAndCreateDateIsAfter(anyString(),anyObject());
		ticketPpmService.findByPpmFlag();
	}

	@Test
	public void updateNotifications() {
		doNothing().when(ticketPpmRepository).updatePpmFlagAsY();
		ticketPpmService.updateNotifications();
	}
}
