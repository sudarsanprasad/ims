package com.ims.util;


import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.ims.entity.FieldConfiguration;
import com.ims.entity.TicketMetadata;
import com.ims.entity.TicketSystem;
import com.ims.repository.TicketMetadataRepository;

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

	@Mock
	private Environment env;
	
	@InjectMocks
	QueryBuilder queryBuilder;
	
	@InjectMocks
	JsonToCsvUtil jsonToCsvUtil;
	
	
	@Test
	public void maskData() {
		String maskeddata=DataMaskUtil.maskData("Hi John, My phone number is 95825258  and mail id abc@gmail.com");
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
	
	public void convertDateParseException() {
		DateUtil.convertDate("31/12/1998");
	}
	
	@Test
	public void convertDate() {
		DateUtil.convertDate("2018-06-06 01:11:50");
	}
	
	@Test
	public void convertDateInvalidDate() {
		DateUtil.convertDate("124hg");
	}
	
	@Test
	public void getTimeStamp() {
		DateUtil.getTimeStamp();
	}
	
	@Test
	public void echoAsCSV() {
		Sheet sheet=mock(Sheet.class);
		Row row=mock(Row.class);
		Cell cell=mock(Cell.class);
		ExcelToCsvUtil excelToCsvUtil=new ExcelToCsvUtil();
		when(sheet.getLastRowNum()).thenReturn(2).thenReturn(0);
		when(sheet.getRow(0)).thenReturn(row);
		when(sheet.getRow(1)).thenReturn(row);
		when(row.getLastCellNum()).thenReturn((short) 1).thenReturn((short) 0);
		when(row.getCell(0)).thenReturn(cell);
		when(cell.toString()).thenReturn("Hi John How are you" );
		excelToCsvUtil.echoAsCSV(sheet,"abc.text","", null, null);
	}
	
	@Test
	public void readExcelFileException() {
		ExcelToCsvUtil excelToCsvUtil=new ExcelToCsvUtil();
		excelToCsvUtil.readExcelFile("abc", "text", "ppm", null, null);
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
	
	@Test
	public void getCustomerName() {
		FileNameUtil.getCustomerName("deloite_servicenow.text");
	}
	
	@Test
	public void getSystemName() {
		FileNameUtil.getSystemName("deloite_servicenow.text");
	}
	
	@Test
	public  void getFileName() {
		TicketSystem ticketSystem = constructTicketSystem();
		FileNameUtil.getFileName("/api/", ticketSystem);
	}

	@Test
	public void  getPpmFileName() {
		TicketSystem ticketSystem = constructTicketSystem();
		FileNameUtil.getPpmFileName("/api/", ticketSystem);
	}
	
	@Test
	public void prepareCsv() {
		JSONObject jsonObjMock=mock(JSONObject.class);
		JSONArray records=mock(JSONArray.class);
		JSONObject record=mock(JSONObject.class);
		FieldConfiguration fieldConfiguration=new FieldConfiguration();
		fieldConfiguration.setId(10l);
		fieldConfiguration.setCustomer("Delloite");
		fieldConfiguration.setSystemName("ServiceNow");
		fieldConfiguration.setProperty("test property");
		List<FieldConfiguration> fieldConfigurationList=new ArrayList<>();
		fieldConfigurationList.add(fieldConfiguration);
		when(jsonObjMock.getJSONArray("result")).thenReturn(records);
		when(records.length()).thenReturn(1).thenReturn(0);
		when(records.getJSONObject(0)).thenReturn(record);
		when(record.get(anyString())).thenReturn("abc");
		jsonToCsvUtil.prepareCsv(jsonObjMock, fieldConfigurationList, "test", "abc");
		
	}

	
	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}
	
		
	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("ServiceNow");
		return ticketSystem;
	}


}
