package com.ims.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.ims.entity.TicketMetadata;

@Service
public class ExcelToCsvUtil {

	private static final Logger LOG = Logger.getAnonymousLogger();

	public void echoAsCSV(Sheet sheet, String file, String ppmFile, List<TicketMetadata> krFields, String systemName, String customer) {
		Row row;
		int closeDateIndex = 9;
		StringBuilder fileContent = new StringBuilder("");
		StringBuilder fileContentStatusNew = new StringBuilder(addPpmHeader(krFields)).append("\n");
		int statusColumnIndex = getColumnIndex(sheet);
		LOG.info("Status Index ==>> "+statusColumnIndex);
		Map<String, Integer> headerIndexMap = getColumnIndexMap(sheet, krFields);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			StringBuilder line = new StringBuilder("");
			StringBuilder lineStatusNew = new StringBuilder("");
			for (int j = 0; j < row.getLastCellNum(); j++) {
				String cellValue;
				if (row.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
					cellValue = getValue(row, j);
				} else if (row.getCell(j).getCellTypeEnum() == CellType.STRING) {
					cellValue = row.getCell(j).getStringCellValue().replaceAll("[\r\n]+", " ").replaceAll(",", " ");
				} else if (row.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
					cellValue = Boolean.toString(row.getCell(j).getBooleanCellValue());
				} else if (row.getCell(j).getCellTypeEnum() == CellType.FORMULA) {
					cellValue = "" + row.getCell(j).getCellFormula();
				} else {
					cellValue = row.getCell(j).getStringCellValue();
				}
				line.append(cellValue).append(",");
			}
			
			lineStatusNew.append(createPpmContent(krFields, systemName, customer, row, headerIndexMap));
			
			fileContent.append(line.toString()).append("\n");
			LOG.info("Value ==>> "+row.getCell(closeDateIndex).toString());
			if (StringUtils.isEmpty(row.getCell(closeDateIndex).toString())) {
				fileContentStatusNew.append(lineStatusNew.toString()).append("\n");
			}
		}
		write2File(fileContent.toString(), file);
		write2File(fileContentStatusNew.toString(), ppmFile);
	}


	private String createPpmContent(List<TicketMetadata> krFields,String systemName, String customer, Row row, Map<String, Integer> headerIndexMap) {
		StringBuilder lineStatusNew = new StringBuilder();
		String ppmCellValue = null;
		boolean flag = false;
		for(TicketMetadata data:krFields){
			String businnesColumn = null;
			if(!StringUtils.isEmpty(data.getBusinessColumn())){
				businnesColumn = data.getBusinessColumn().replaceAll("_", " ");
			}
			LOG.info("businnesColumn ==>> "+businnesColumn);
			int index = headerIndexMap.get(businnesColumn);
			LOG.info("index === "+index);
			LOG.info("Cell ===>> "+row.getCell(index));
			
			if(index == 26){
				flag = true;
				ppmCellValue = customer;
			}
			if(index == 27){
				flag = true;
				ppmCellValue = systemName;
			}
			if(!flag){
				ppmCellValue = row.getCell(index).toString();
			}
			lineStatusNew.append(ppmCellValue).append(",");
			LOG.info("Line ==>> "+lineStatusNew.toString());
		}
		
		return lineStatusNew.toString().substring(0, lineStatusNew.lastIndexOf(","));
	}

	
	private String addPpmHeader(List<TicketMetadata> krFields){
		StringBuilder fileContentStatusNew = new StringBuilder();
		for(TicketMetadata data:krFields){
			fileContentStatusNew.append(data.getMappingColumn()).append(",");
		}
		return fileContentStatusNew.toString().substring(0, fileContentStatusNew.lastIndexOf(","));
	}
	

	private Map<String, Integer> getColumnIndexMap(Sheet sheet, List<TicketMetadata> krFields) {
		int statusColumnIndex = 0;
		Map<String, Integer> headerIndexMap = new HashMap<>();
		Row row;
		for (int i = 0; i <= 1; i++) {
			row = sheet.getRow(i);
			for(TicketMetadata data:krFields){
				String businessColumn = data.getBusinessColumn().replaceAll("_", " ");
				statusColumnIndex = createMap(headerIndexMap, row, businessColumn);
			}
			LOG.info("" + row.getCell(statusColumnIndex));
		}
		headerIndexMap.put("customername", 26);
		headerIndexMap.put("systemname", 27);
		return headerIndexMap;
	}


	private int createMap(Map<String, Integer> headerIndexMap, Row row, String businessColumn) {
		int statusColumnIndex=0;
		for (int j = 0; j < row.getLastCellNum(); j++) {
			if (businessColumn.equalsIgnoreCase(row.getCell(j).toString())) {
				statusColumnIndex = row.getCell(j).getColumnIndex();
				headerIndexMap.put(businessColumn, statusColumnIndex);
			}
		}
		return statusColumnIndex;
	}
	

	private int getColumnIndex(Sheet sheet) {
		int statusColumnIndex = 0;
		Row row;
		for (int i = 0; i <= 1; i++) {
			row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if ("Status".equalsIgnoreCase(row.getCell(j).toString()) || "State".equalsIgnoreCase(row.getCell(j).toString())) {
					statusColumnIndex = row.getCell(j).getColumnIndex();
				}
			}
			LOG.info("" + row.getCell(statusColumnIndex));
		}
		return statusColumnIndex;
	}

	private String getValue(Row row, int j) {
		String cellValue;
		if (HSSFDateUtil.isCellDateFormatted(row.getCell(j))) {
			cellValue = DateUtil.convertDateToString(row.getCell(j).getDateCellValue());
		} else {
			cellValue = String.valueOf(row.getCell(j).getNumericCellValue());
		}
		return cellValue;
	}

	public void write2File(String text, String filePath) {
		ImsFileWriter writer = new ImsFileWriter();
		writer.write2File(text, filePath);
	}


	/**
	 * @param args
	 *            the command line arguments
	 */
	public void readExcelFile(String fileName, String file, String ppmFile, List<TicketMetadata> krFields, String systemName, String customer) {
		try (InputStream inp = new FileInputStream(fileName)) {
			try (Workbook wb = WorkbookFactory.create(inp)) {
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					echoAsCSV(wb.getSheetAt(i), file, ppmFile, krFields, systemName, customer);
				}
			}
		} catch (InvalidFormatException | IOException ex) {
			Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public int getRecordsCount(String fileName) {
		int recordsCount = 0;
		try (InputStream inp = new FileInputStream(fileName)) {
			try (Workbook wb = WorkbookFactory.create(inp)) {
				Sheet sheet = null;
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					sheet = wb.getSheetAt(i);
				}
				if (sheet != null) {
					recordsCount = sheet.getLastRowNum();
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return recordsCount;
	}

}