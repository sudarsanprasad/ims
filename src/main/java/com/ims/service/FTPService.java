package com.ims.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ims.constant.SourceType;
import com.ims.constant.StatusType;
import com.ims.dto.TicketDataDto;
import com.ims.entity.TicketLogStatistics;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DataMaskUtil;
import com.ims.util.QueryBuilder;

@Service
public class FTPService {

	private static final Logger LOG = Logger.getLogger(FTPService.class);

	@Autowired
	private FtpRemoteFileTemplate template;

	@Autowired
	private Environment env;

	@Autowired
	TicketMetadataRepository ticketMetadataRepository;

	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;

	public boolean downloadExcel() throws ImsException {
		boolean isFileSavedToLocalFlag;
		String fileName = null;
		FTPFile[] files = template.list("");
		String systemName = (String)env.getProperty("ticketsystem");
		String customer = (String)env.getProperty("customer");
		try {
			for (FTPFile file : files) {
				SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				String formattedDate = formatDate.format(file.getTimestamp().getTime());
				fileName = file.getName();
				LOG.info(file.getName() + "     " + formattedDate);
			}
			isFileSavedToLocalFlag = template.get("data.xls", inputStream -> FileCopyUtils.copy(inputStream, new FileOutputStream(new File("C:/test/data.xls"))));
			if (isFileSavedToLocalFlag) {
				TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics(fileName));
				processExcelData("C:/test/data.xls", ticketStatistics, systemName, customer);
			}
		} catch (Exception ex) {
			LOG.error(ex);
			throw new ImsException(
					"Exception occured while processing excel data", ex);
		}
		return isFileSavedToLocalFlag;
	}

	private void processExcelData(String filename, TicketStatistics ticketStatistics, String systemName, String customer) {
		boolean isFailed = false;
		try {
			QueryBuilder queryBuilder = new QueryBuilder();
			StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository, systemName, customer);
			FileInputStream excelFile = new FileInputStream(new File(filename));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			ticketStatistics.setComments("Reading the data from Excel in Progress");
			ticketStatisticsRepository.save(ticketStatistics);
			List<TicketDataDto> dtos = getDataFromHDFS(getTicketIds(filename));
			updateDataToHDFS(ticketStatistics, queryBuilder, qBuilder, iterator, dtos);
			workbook.close();
		} catch (FileNotFoundException e) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("File Not Found");
		} catch (OLE2NotOfficeXmlFileException e) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Reading the File");
			
		} catch (IOException e) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Reading the File");
			
		} 
		if(isFailed){
			ticketStatisticsRepository.save(ticketStatistics);
		}
	}
	
	private List<TicketDataDto> getDataFromHDFS(String ticketIds){
		List<TicketDataDto> dtos = new ArrayList<>();
		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			StringBuilder queryBuilder = new StringBuilder(" select * from ticket_temp_data where col1 in(");
			queryBuilder.append(ticketIds).append(")");
			LOG.info(queryBuilder.toString());
			ResultSet rs = stmt.executeQuery(queryBuilder.toString());
			while (rs.next()) {
				TicketDataDto dto = new TicketDataDto();
				dto.setCol1(rs.getString(1));
				dto.setCol2(rs.getString(2));
				dto.setCol3(rs.getString(3));
				dto.setCol4(rs.getString(4));
				dto.setCol5(rs.getString(5));
				dto.setCol6(rs.getString(6));
				dto.setCol7(rs.getString(7));
				dto.setCol8(rs.getString(8));
				dto.setCol9(rs.getString(9));
				dto.setCol10(rs.getString(10));
				dto.setCol11(rs.getString(11));
				dto.setCol12(rs.getString(12));
				dtos.add(dto);
				LOG.info(rs.getString(1));
			}
			closeConnection(con, stmt);
		} catch (ImsException | SQLException e) {
			LOG.error(e);
		} 
		return dtos;
	}

	private boolean updateDataToHDFS(TicketStatistics ticketStatistics,QueryBuilder queryBuilder, StringBuilder qBuilder, Iterator<Row> iterator, List<TicketDataDto> dtos) {
		Long successCount = 0l;
		Long failureCount = 0l;
		ticketStatistics.setRecordsInserted(0l);
		ticketStatistics.setRecordsFailed(0l);
		boolean isFailed = false;
		boolean skipFirstRow = false;
		Connection con = null;
		Statement stmt = null;
		String ticketId = "";
		List<TicketLogStatistics> ticketLogStatisticsList = new ArrayList<>();
		try {
			con = getConnection();
			stmt = con.createStatement();
			ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
			ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
			ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
			ticketStatisticsRepository.save(ticketStatistics);
			while (iterator.hasNext()) {
				try{
					if (!skipFirstRow) {
						iterator.next();
					}
					StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
					skipFirstRow = true;
					Row currentRow = iterator.next();
					TicketDataDto dto = new TicketDataDto();
					String values[]=new String[12];
					Iterator<Cell> cellIterator = currentRow.iterator();
					int i = 0;
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						query.append("\"");
						values[i] = appendCellColumn(query, currentCell); 
						i++;
					}
					fillDto(values, dto);
					query.append("\"");
					query.append(String.valueOf(ticketStatistics.getJobId()));
					query.append("\"").append(",");
					query.append("\"");
					boolean isRecordExists = isRecordExists(dtos, dto);
					if(dto.getCol12() == null){
						query.append(1);
					}else{
						query.append(dto.getCol12());
					}
					query.append("\"").append(")");
					LOG.info(query.toString());
					ticketId = dto.getCol1();
					if(!isRecordExists){
						stmt.execute(query.toString());
						successCount++;
						ticketStatistics.setRecordsInserted(successCount);
					}
					
					ticketStatistics.setSource(SourceType.FTP.getDescription());
				}catch (SQLException e) {
					LOG.error(e);
					TicketLogStatistics ticketLogStatistics = new TicketLogStatistics();
					ticketLogStatistics.setTicketId(ticketId);
					ticketLogStatistics.setMessage(e.getMessage());
					ticketLogStatisticsList.add(ticketLogStatistics);
					failureCount++;
					ticketStatistics.setRecordsFailed(failureCount);
					ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
					ticketStatistics.setComments("Exception occured While Processing the File");
					ticketStatistics.setSource(SourceType.FTP.getDescription());
					ticketStatistics.setTicketLogStatistics(ticketLogStatisticsList);
					isFailed = true;
					continue;
				}
		}
				
		} catch (ImsException e) {
			failureCount++;
			ticketStatistics.setRecordsFailed(ticketStatistics.getRecordsFailed() + failureCount);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Processing the File");
			LOG.error(e);
			TicketLogStatistics ticketLogStatistics = new TicketLogStatistics();
			ticketLogStatistics.setTicketId(ticketId);
			ticketLogStatistics.setMessage(e.getMessage());
			ticketLogStatisticsList.add(ticketLogStatistics);
			ticketStatistics.setTicketLogStatistics(ticketLogStatisticsList);
			isFailed = true;
		} catch (SQLException e) {
			LOG.error(e);
			TicketLogStatistics ticketLogStatistics = new TicketLogStatistics();
			ticketLogStatistics.setTicketId(ticketId);
			ticketLogStatistics.setMessage(e.getMessage());
			ticketLogStatisticsList.add(ticketLogStatistics);
			ticketStatistics.setTicketLogStatistics(ticketLogStatisticsList);
			failureCount++;
			ticketStatistics.setRecordsFailed(ticketStatistics.getRecordsFailed() + failureCount);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Processing the File");
			isFailed = true;
		}
		closeConnection(con, stmt);
		if(!isFailed){
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatistics.setComments("Data inserted into HDFS");
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
			ticketStatisticsRepository.save(ticketStatistics);
		}else{
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatisticsRepository.save(ticketStatistics);
		}
		return skipFirstRow;
	}

	private void fillDto(String[] values, TicketDataDto dto) {
		dto.setCol1(values[0]);
		dto.setCol2(values[1]);
		dto.setCol3(values[2]);
		dto.setCol4(values[3]);
		dto.setCol5(values[4]);
		dto.setCol6(values[5]);
		dto.setCol7(values[6]);
		dto.setCol8(values[7]);
		dto.setCol9(values[8]);
		dto.setCol10(values[9]);
		dto.setCol11(values[10]);
		dto.setCol12(values[11]);
	}

	private String getTicketIds(String filename) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File(filename));
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		StringBuilder ticketIdBuilder = new StringBuilder("");
		boolean skipRowOne = false;
		while (iterator.hasNext()) {
			if (!skipRowOne) {
				iterator.next();
			}
			Row currentRow = iterator.next();
			skipRowOne = true;
			ticketIdBuilder.append("'").append(currentRow.getCell(0).getStringCellValue()).append("'").append(",");
			LOG.info(currentRow.getCell(0).getStringCellValue());
			LOG.info("");
			
		}
		workbook.close();
		return ticketIdBuilder.toString().substring(0,ticketIdBuilder.lastIndexOf(","));
	}

	private void closeConnection(Connection con, Statement stmt) {
		try {
			con.close();
			stmt.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		
	}

	private String appendCellColumn(StringBuilder query, Cell currentCell) {
		String cellValue = null;
		if (currentCell.getCellTypeEnum() == CellType.STRING) {
			cellValue = DataMaskUtil.maskData(currentCell.getStringCellValue());
			query.append(cellValue).append("\"").append(",");
		} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			double value = currentCell.getNumericCellValue();
			query.append(currentCell.getNumericCellValue()).append("\"").append(",");
			cellValue = String.valueOf(value);
		}
		return cellValue;
	}
	
	private boolean isRecordExists(List<TicketDataDto> dtos, TicketDataDto dto){
		boolean isRecordExists = false;
		if(!CollectionUtils.isEmpty(dtos)){
			for(TicketDataDto record:dtos){
				if(record.getCol1().equals(dto.getCol1())){
					if(record.equals(dto)){
						isRecordExists = true;
					}else{
						dto.setCol12(String.valueOf(Integer.parseInt(record.getCol12())+1));
					}
				}
			}
		}
		return isRecordExists;
	}

	private TicketStatistics getTicketStatistics(String fileName) {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName((String) env.getProperty("ticketsystem"));
		ticketStatistics.setCustomer((String) env.getProperty("customer"));
		ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setFileName(fileName);
		ticketStatistics.setComments("Excel downloaded successfully");
		return ticketStatistics;
	}

	public Connection getConnection() throws ImsException {
		try {
			Class.forName((String) env.getProperty("hive.driver-class-name"));
			return DriverManager.getConnection(
					(String) env.getProperty("hive.url"),
					(String) env.getProperty("hive.username"),
					(String) env.getProperty("hive.password"));
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error(e);
			throw new ImsException("", e);
		}
	}
}
