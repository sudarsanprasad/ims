package com.ims.util;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.ims.entity.TicketMetadata;
import com.ims.repository.TicketMetadataRepository;

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

	@Mock
	private Environment env;
	
	@InjectMocks
	QueryBuilder queryBuilder;
	
	
	@Test
	public void maskData() {
		String maskeddata=DataMaskUtil.maskData("Hi John, My phone number is 95825258  and mail id abc@gmail.com");
		Assert.notNull(maskeddata);
	}
	
	@Test
	public void maskData2() {
		String maskeddata=DataMaskUtil2.maskData("Hi John, My phone number is 95825258  and mail id abc@gmail.com");
		Assert.notNull(maskeddata);
	}
	
	@Test
	public void convertDateToString() {
		DateUtil.convertDateToString(new Date());
	}
	
	@Test
	public void getDateAndTime() {
		DateUtil.getDateAndTime("12 10 00");
	}
	
	//@Test
	public void echoAsCSV() {
		Sheet sheet=mock(Sheet.class);
		Row row=mock(Row.class);
		Cell cell=mock(Cell.class);
		ExcelToCsvUtil excelToCsvUtil=new ExcelToCsvUtil();
		when(sheet.getLastRowNum()).thenReturn(2).thenReturn(0);
		when(sheet.getRow(1)).thenReturn(row);
		when(row.getLastCellNum()).thenReturn((short) 1).thenReturn((short) 0);
		when(row.getCell(0)).thenReturn(cell);
		when(cell.toString()).thenReturn("Hi John How are you" );
		excelToCsvUtil.echoAsCSV(sheet,"abc.text","");
	}
	
	@Test
	public void buildHiveQueryAPI() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		TicketMetadataRepository ticketMetadataRepository=mock(TicketMetadataRepository.class);
		when(ticketMetadataRepository.findBySystemNameAndCustomerOrderById("ServiceNow", "Deloitte"))
		.thenReturn(ticketMetadataList);
		queryBuilder.buildHiveQuery(ticketMetadataRepository, "ServiceNow", "Deloitte", "API");
	}
	
	@Test
	public void buildHiveQueryFTP() {
		List<TicketMetadata> ticketMetadataList = constructTicketMetaDataList();
		TicketMetadataRepository ticketMetadataRepository=mock(TicketMetadataRepository.class);
		when(ticketMetadataRepository.findBySystemNameAndCustomerOrderById("ServiceNow", "Deloitte"))
		.thenReturn(ticketMetadataList);
		queryBuilder.buildHiveQuery(ticketMetadataRepository, "ServiceNow", "Deloitte", "FTP");
	}
	
	@Test
	public void getInsertQueryWithValue() {
		StringBuilder queryBuilders=new StringBuilder("adf,dtest");
		queryBuilder.getInsertQueryWithValue(queryBuilders);
	}
	
	@Test
	public void getSelectValue() {
		StringBuilder queryBuilders=new StringBuilder("adf,dtest");
		queryBuilder.getSelectValue(queryBuilders);
	}
	
	@Test
	public void getFromValue() {
		StringBuilder queryBuilders=new StringBuilder("adf,dtest");
		queryBuilder.getFromValue(queryBuilders,"ticket_system");
	}
	
	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}

}
