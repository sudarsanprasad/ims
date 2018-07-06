package com.ims.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.TicketMetadata;

@RunWith(MockitoJUnitRunner.class)
public class ExcelToCsvUtilTest {

	@InjectMocks
	ExcelToCsvUtil excelToCsvUtil;

	@Test(expected=Exception.class)
	public void readExcelFile() {
		List<TicketMetadata> ticketMetaDataList=constructTicketMetaDataList();
		excelToCsvUtil.readExcelFile("src/test/resources/Deloitte_AMPM_P10_20180522121000.xls", "src/test/resources/",
				"src/test/resources/", ticketMetaDataList, "Service Now", "Deloitte");
	}

	@Test
	public void getRecordsCount() {
		excelToCsvUtil.getRecordsCount("src/test/resources/Deloitte_AMPM_P10_20180522121000.xls");
	}
	
	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setSystemName("Service Now");
		ticketMetadata.setMappingColumn("Col1");
		ticketMetadata.setBusinessColumn("Col2");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}

}
