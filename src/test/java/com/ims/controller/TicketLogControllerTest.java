package com.ims.controller;

import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ims.service.TicketLogService;

@RunWith(MockitoJUnitRunner.class)
public class TicketLogControllerTest {

	@InjectMocks
	private TicketLogController ticketLogController;

	@Mock
	private TicketLogService ticketLogService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void downloadLogStatistics() throws IOException {
		when(ticketLogService.downloadLogStatistics(10L)).thenReturn(new ResponseEntity(HttpStatus.OK));
		notNull(ticketLogController.downloadLogStatistics(10L));
	}
}
