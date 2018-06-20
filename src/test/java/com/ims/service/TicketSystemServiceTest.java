package com.ims.service;

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
import com.ims.repository.FieldMaskRepository;
import com.ims.repository.TicketSystemRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketSystemServiceTest {

	@InjectMocks
	private TicketSystemService ticketSystemService;

	@Mock
	private TicketSystemRepository ticketSystemRepository;

	@Mock
	private FieldMaskRepository fieldMaskRepository;

	@Mock
	ImsJobService imsJobService;

	@Test
	public void getTicketSystemById() {
		when(ticketSystemRepository.findOne(10L)).thenReturn(constructTicketSystem());
		TicketSystem ticketSystem = ticketSystemService.getTicketSystemById(10L);
		notNull(ticketSystem);
	}

	@Test
	public void findAll() {
		when(ticketSystemRepository.findAll()).thenReturn(getTicketSystemList());
		List<TicketSystem> ticketSystemList = ticketSystemService.findAll();
		notEmpty(ticketSystemList);
	}

	@Test
	public void saveTicketSystem() {
		TicketSystem ticketSystem = constructTicketSystem();
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		when(ticketSystemRepository.findByCustomer("Deloite")).thenReturn(new ArrayList<TicketSystem>());
		TicketSystem ticketSystemResp = ticketSystemService.saveTicketSystem(ticketSystem);
		notNull(ticketSystemResp);
	}

	@Test
	public void updateTicketSystem() {
		TicketSystem ticketSystem = constructTicketSystem();
		when(ticketSystemRepository.save(ticketSystem)).thenReturn(ticketSystem);
		TicketSystem ticketSystemResp = ticketSystemService.updateTicketSystem(ticketSystem);
		notNull(ticketSystemResp);
	}

	@Test
	public void deleteTicketSystem() {
		TicketSystem ticketSystem = constructTicketSystem();
		doNothing().when(ticketSystemRepository).delete(ticketSystem);
		ticketSystemService.deleteTicketSystem(ticketSystem);
	}

	@Test
	public void deleteTicketSystemById() {
		TicketSystem ticketSystem = constructTicketSystem();
		when(ticketSystemRepository.findById(10L)).thenReturn(ticketSystem);
		doNothing().when(ticketSystemRepository).delete(ticketSystem);
		String message = ticketSystemService.deleteTicketSystemById(10L);
		assertEquals("System Deleted Successfully", message);
	}

	@Test
	public void getFieldMask() {
		List<FieldMask> fieldMaskList = loadFieldMaskList();
		when(fieldMaskRepository.findByMaskEnabled("X")).thenReturn(fieldMaskList);
		List<FieldMask> fieldMaskListResp = ticketSystemService.getFieldMask();
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
