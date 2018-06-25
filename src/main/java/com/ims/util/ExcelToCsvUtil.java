package com.ims.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelToCsvUtil {

	private static final Logger LOG = Logger.getAnonymousLogger();

	public void echoAsCSV(Sheet sheet, String file, String ppmFile) {
		Row row;
		StringBuilder fileContent = new StringBuilder("");
		StringBuilder fileContentStatusNew = new StringBuilder("");
		int statusColumnIndex = getColumnIndex(sheet);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			StringBuilder line = new StringBuilder("");
			StringBuilder lineStatusNew = new StringBuilder("");
			for (int j = 0; j < row.getLastCellNum(); j++) {
				String cellValue;
				if (row.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
					cellValue = getValue(row, j);
				} else if (row.getCell(j).getCellTypeEnum() == CellType.STRING) {
					cellValue = row.getCell(j).getStringCellValue()
							.replaceAll("[\r\n]+", " ").replaceAll(",", " ");
				} else if (row.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
					cellValue = Boolean.toString(row.getCell(j).getBooleanCellValue());
				} else if (row.getCell(j).getCellTypeEnum() == CellType.FORMULA) {
					cellValue = "" + row.getCell(j).getCellFormula();
				} else {
					cellValue = row.getCell(j).getStringCellValue();
				}
				line.append(cellValue).append(",");
				lineStatusNew.append(cellValue).append(",");
			}
			fileContent.append(line.toString()).append("\n");
			if ("NEW".equalsIgnoreCase(row.getCell(statusColumnIndex)
					.toString())) {
				fileContentStatusNew.append(line.toString()).append("\n");
			}
		}
		write2File(fileContent.toString(), file);
		write2File(fileContentStatusNew.toString(), ppmFile);
	}

	private int getColumnIndex(Sheet sheet) {
		int statusColumnIndex = 0;
		Row row;
		for (int i = 0; i <= 1; i++) {
			row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if ("Status".equalsIgnoreCase(row.getCell(j).toString())
						|| "State".equalsIgnoreCase(row.getCell(j).toString())) {
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
			LOG.info("Date Before Conversion ==>> "
					+ row.getCell(j).getDateCellValue());
			cellValue = DateUtil.convertDateToString(row.getCell(j)
					.getDateCellValue());
			LOG.info("Date After Conversion ==>> " + cellValue);
		} else {
			cellValue = String.valueOf(row.getCell(j).getNumericCellValue());
		}
		return cellValue;
	}

	public void write2File(String text, String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				boolean created = file.createNewFile();
				if (created) {
					LOG.info("File is created");
				}
			} catch (IOException e) {
				LOG.info("Error === >> " + e);
			}
		}
		try(FileWriter fw = new FileWriter(file)){
			LOG.info("Saving csv file ==== >>" + file);
			Writer writer = new BufferedWriter(fw);
			writer.write(text);
			writer.close();
			fw.close();
		} catch (IOException e) {
			LOG.info("Error == >> " + e);
		}

	}


	/**
	 * @param args
	 *            the command line arguments
	 */
	public void readExcelFile(String fileName, String file, String ppmFile) {
		try (InputStream inp = new FileInputStream(fileName)) {
			try (Workbook wb = WorkbookFactory.create(inp)) {
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					echoAsCSV(wb.getSheetAt(i), file, ppmFile);
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