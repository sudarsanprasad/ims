package com.ims.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelToCsvUtilTest {

	@InjectMocks
	ExcelToCsvUtil excelToCsvUtil;
	
	@Test
	public void readExcelFile() {
		excelToCsvUtil.readExcelFile("src/test/resources/Deloitte_AMPM_P10_20180522121000.xls", "src/test/resources/", "src/test/resources/", null, null, null, null);
	}
	
	@Test
	public void getRecordsCount() {
		excelToCsvUtil.getRecordsCount("src/test/resources/Deloitte_AMPM_P10_20180522121000.xls");
	}
}
