package com.ims.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
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

import com.ims.entity.FieldMask;
import com.ims.entity.TicketSystem;
import com.ims.service.TicketSystemService;

@RunWith(MockitoJUnitRunner.class)
public class TicketSystemControllerTest {

	@InjectMocks
	private TicketSystemController ticketSystemController;

	@Mock
	private TicketSystemService ticketSystemService;

	@Test
	public void testGetTicketSystemById() throws Exception {
		when(ticketSystemService.getTicketSystemById(10L)).thenReturn(constructTicketSystem());
		TicketSystem ticketSystem = ticketSystemController.getTicketSystemById(10L);
		notNull(ticketSystem);
	}

	@Test
	public void testFindAll() {
		when(ticketSystemService.findAll()).thenReturn(getTicketSystemList());
		List<TicketSystem> ticketSystemList = ticketSystemController.findAll();
		notEmpty(ticketSystemList);
	}

	@Test
	public void testSaveTicketSystem() throws Exception {
		TicketSystem ticketSystem = constructTicketSystem();
		when(ticketSystemService.saveTicketSystem(ticketSystem)).thenReturn(ticketSystem);
		TicketSystem ticketSystemResp = ticketSystemController.saveTicketSystem(ticketSystem);
		notNull(ticketSystemResp);
	}

	@Test
	public void testUpdateTicketSystem() throws Exception {
		TicketSystem ticketSystem = constructTicketSystem();
		when(ticketSystemService.updateTicketSystem(ticketSystem)).thenReturn(ticketSystem);
		TicketSystem ticketSystemResp = ticketSystemController.updateTicketSystem(ticketSystem);
		notNull(ticketSystemResp);
	}

	@Test
	public void testDeleteTicketSystem() {
		TicketSystem ticketSystem = constructTicketSystem();
		doNothing().when(ticketSystemService).deleteTicketSystem(ticketSystem);
		ticketSystemController.deleteTicketSystem(ticketSystem);
	}

	@Test
	public void testDeleteTicketSystemById() {
		when(ticketSystemService.deleteTicketSystemById(10L)).thenReturn("System Deleted Successfully");
		String message = ticketSystemController.deleteTicketSystemById(10L);
		assertEquals("System Deleted Successfully", message);
	}

	@Test
	public void testGetFieldMask() {
		List<FieldMask> fieldMaskList = loadFieldMaskList();
		when(ticketSystemService.getFieldMask()).thenReturn(fieldMaskList);
		List<FieldMask> fieldMaskListResp =ticketSystemController.getFieldMask();
		notEmpty(fieldMaskListResp);
	}

	private List<FieldMask> loadFieldMaskList() {
		FieldMask fieldMask = new FieldMask();
		fieldMask.setId(100L);
		fieldMask.setCustomer("Deloite");
		fieldMask.setSystemName("ServiceNow");
		fieldMask.setMaskEnabled("Y");
		List<FieldMask> fieldMaskList = new ArrayList<FieldMask>();
		fieldMaskList.add(fieldMask);
		return fieldMaskList;
	}

	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		return ticketSystem;
	}

	private List<TicketSystem> getTicketSystemList() {
		List<TicketSystem> ticketSystemList = new ArrayList<TicketSystem>();
		ticketSystemList.add(constructTicketSystem());
		return ticketSystemList;
	}

}
