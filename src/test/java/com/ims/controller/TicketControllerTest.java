package com.ims.controller;

import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notEmpty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.springframework.util.Assert.isTrue;

import com.ims.entity.Ticket;
import com.ims.exception.ImsException;
import com.ims.service.FTPService;
import com.ims.service.TicketService;

@RunWith(MockitoJUnitRunner.class)
public class TicketControllerTest {

	@InjectMocks
	private TicketController ticketController;

	@Mock
	private FTPService ftpService;

	@Mock
	private TicketService ticketService;

	@Test
	public void getTickets() {
		List<Ticket> ticketList = constructTicketList();
		when(ticketService.getTicketData()).thenReturn(ticketList);
		notEmpty(ticketController.getTickets());
	}

	@Test
	public void testDownloadExcel() throws ImsException {
		when(ftpService.downloadExcel()).thenReturn(true);
		isTrue(ticketController.downloadExcel());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadExcelException() throws ImsException {
		when(ftpService.downloadExcel()).thenThrow(ImsException.class);
		assertFalse(ticketController.downloadExcel());

	}

	private List<Ticket> constructTicketList() {
		List<Ticket> ticketList = new ArrayList<>();
		Ticket ticket = new Ticket();
		ticket.setTicketid("10");
		ticket.setCategory("ServiceNow");
		ticketList.add(ticket);
		return ticketList;
	}
}
