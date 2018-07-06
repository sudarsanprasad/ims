package com.ims.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.dto.PpmDto;
import com.ims.service.TicketPpmService;

@RunWith(MockitoJUnitRunner.class)
public class TicketPpmControllerTest {

	@InjectMocks
	TicketPpmController ticketPpmController;
	
	@Mock
	TicketPpmService ticketPpmService;

	@Test
	public void getNotifications() {
		when(ticketPpmService.findByPpmFlag()).thenReturn(new PpmDto());
		ticketPpmController.getNotifications();
	}

	@Test
	public void updateForecastStatus() {
		doNothing().when(ticketPpmService).updateNotifications();
		ticketPpmController.updateForecastStatus();
	}
}
