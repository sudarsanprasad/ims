package com.ims.service;

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

import com.ims.entity.TicketMetadata;
import com.ims.repository.TicketMetadataRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketMetadataServiceTest {

	@InjectMocks
	private TicketMetadataService ticketMetadataService;

	@Mock
	private TicketMetadataRepository ticketMetadataRepository;

	@Test
	public void create() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.save(ticketMetadataList)).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.create(ticketMetadataList);
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void update() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.save(ticketMetadataList)).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.update(ticketMetadataList);
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findById() {
		TicketMetadata ticketMetadata = constructTicketMetaData();
		when(ticketMetadataRepository.findOne(10L)).thenReturn(ticketMetadata);
		TicketMetadata ticketMetadataResp = ticketMetadataService.findById(10L);
		notNull(ticketMetadataResp);
	}

	@Test
	public void delete() {
		doNothing().when(ticketMetadataRepository).delete(10L);
		ticketMetadataService.delete(10L);
	}

	@Test
	public void findAll() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.findAll()).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.findAll();
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findBySystemNameAndCustomer() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.findBySystemNameAndCustomerOrderById("Service Now", "Deloitte"))
				.thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.findBySystemNameAndCustomer("Service Now",
				"Deloitte");
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findBySystemName() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.findBySystemName("Service Now")).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.findBySystemName("Service Now");
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findByCustomer() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		when(ticketMetadataRepository.findByCustomer("Deloitte")).thenReturn(ticketMetadataList);
		List<TicketMetadata> ticketMetadataListResp = ticketMetadataService.findByCustomer("Deloitte");
		notEmpty(ticketMetadataListResp);
	}

	@Test
	public void findByIsForecast() {
		List<String> list = new ArrayList<>();
		list.add("test");
		when(ticketMetadataRepository.findMappingColumnByIsForecast("Service Now")).thenReturn(list);
		List<String> listResp = ticketMetadataService.findByIsForecast("Service Now");
		notEmpty(listResp);
	}

	@Test
	public void findMappingColumnByIsKnowledgement() {
		List<String> list = new ArrayList<>();
		list.add("test");
		when(ticketMetadataRepository.findMappingColumnByIsKnowledgement("Y")).thenReturn(list);
		List<String> listResp = ticketMetadataService.findMappingColumnByIsKnowledgement("Y");
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
