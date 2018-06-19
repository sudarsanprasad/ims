package com.ims.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.notEmpty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.portable.ApplicationException;

import com.ims.entity.TicketMetadata;
import com.ims.service.TicketMetadataService;

@RunWith(MockitoJUnitRunner.class)
public class TicketMetadataControllerTest {

	@InjectMocks
	private TicketMetadataController ticketMetadataController;

	@Mock
	private TicketMetadataService ticketMetadataService;

	@Test
	public void createMetadata() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.create(ticketMetadataList)).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController.createMetadata(ticketMetadataList);
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void updateMetadata() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.update(ticketMetadataList)).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController.updateMetadata(ticketMetadataList);
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void deleteMetadata() throws ApplicationException {
		doNothing().when(ticketMetadataService).delete(10L);
		ticketMetadataController.deleteMetadata(10L);
	}

	@Test
	public void findMetadataById() throws ApplicationException {
		TicketMetadata ticketMetadata = constructTicketMetaData();
		when(ticketMetadataService.findById(10L)).thenReturn(ticketMetadata);
		TicketMetadata ticketMetadataResp = ticketMetadataController.findMetadataById(10L);
		notNull(ticketMetadataResp);
	}

	@Test
	public void findAll() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.findAll()).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController.findAll();
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findBySystemNameAndCustomer() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.findBySystemNameAndCustomer("Service Now", "Deloitte"))
				.thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController
				.findBySystemNameAndCustomer(constructTicketMetaData());
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findBySystemName() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.findBySystemName("Service Now")).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController
				.findBySystemName(constructTicketMetaData());
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findByCustomer() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataService.findByCustomer("Deloitte")).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataController
				.findByCustomer(constructTicketMetaData());
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findByIsForecast() {
		List<String> list = new ArrayList<>();
		list.add("test");
		when(ticketMetadataService.findByIsForecast("Service Now")).thenReturn(list);
		List<String> listResp = ticketMetadataController.findByIsForecast("Service Now");
		notEmpty(listResp);
	}

	@Test
	public void findMappingColumnByIsKnowledgement() {
		List<String> list = new ArrayList<>();
		list.add("test");
		when(ticketMetadataService.findMappingColumnByIsKnowledgement("Y")).thenReturn(list);
		List<String> listResp = ticketMetadataController.findByIsKnowledgement("Y");
		notEmpty(listResp);
	}

	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}

	private TicketMetadata constructTicketMetaData() {
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadata.setCustomer("Deloitte");
		return ticketMetadata;
	}

}
