package com.ims.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

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
		doReturn(ticketPpmList).when(ticketPpmRepository).findByPpmFlag("Y");
		ticketPpmService.findByPpmFlag();
	}

	@Test
	public void updateNotifications() {
		doNothing().when(ticketPpmRepository).updatePpmFlagAsY();
		ticketPpmService.updateNotifications();
	}
}
