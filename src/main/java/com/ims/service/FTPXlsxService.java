package com.ims.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import com.ims.entity.TicketLogStatistics;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DateUtil;
import com.ims.util.QueryBuilder;

@Service
public class FTPXlsxService {

	private static final Logger LOG = Logger.getLogger(FTPXlsxService.class);

	@Autowired
	private FtpRemoteFileTemplate template;

	@Autowired
	private Environment env;

	@Autowired
	TicketMetadataRepository ticketMetadataRepository;

	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;

	public boolean downloadExcel() throws ImsException {
		boolean isFileSavedToLocalFlag = false;
		String fileName = null;
		FTPFile[] files = template.list("");
		String systemName = (String)env.getProperty("ftpticketsystem");
		String customer = (String)env.getProperty("customer");
		
		File remoteFile = new File((String)env.getProperty("file.location"));
		String ftpFileName = remoteFile.getName();
		String location = (String)env.getProperty("file.location");
			try {
				for (FTPFile file : files) {
					SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					String formattedDate = formatDate.format(file.getTimestamp().getTime());
					fileName = file.getName();
					LOG.info(file.getName() + "     " + formattedDate);
				}
				isFileSavedToLocalFlag = template.get(ftpFileName, inputStream -> FileCopyUtils.copy(inputStream, new FileOutputStream(new File(location))));
				if (isFileSavedToLocalFlag) {
					TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics(fileName));
					processExcelData(location, ticketStatistics, systemName, customer);
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
			StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository, systemName, customer,"FTP");
			FileInputStream excelFile = new FileInputStream(new File(filename));
			
			
			Workbook workbook = new XSSFWorkbook(excelFile);
			
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			ticketStatistics.setComments("Reading the data from Excel in Progress");
			ticketStatisticsRepository.save(ticketStatistics);
			updateDataToHDFS(ticketStatistics, queryBuilder, qBuilder, iterator);
			workbook.close();
		} catch (FileNotFoundException e) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("File Not Found");
		} catch (OLE2NotOfficeXmlFileException | IOException e) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Reading the File");
			
		} 
		if(isFailed){
			ticketStatisticsRepository.save(ticketStatistics);
		}
	}
	
	private boolean updateDataToHDFS(TicketStatistics ticketStatistics,QueryBuilder queryBuilder, StringBuilder qBuilder, Iterator<Row> iterator) {
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
					Iterator<Cell> cellIterator = currentRow.iterator();
					query.append("\"").append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
					query.append("\"").append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(",");
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						query.append("\"");
						appendCellColumn(query, currentCell); 
					}
					String tempQueryBuilder = query.toString().substring(0, query.lastIndexOf(","));
					StringBuilder finalQuery = new StringBuilder(tempQueryBuilder).append(")");
					LOG.info(finalQuery.toString());
					ticketId = currentRow.getCell(0).toString();
					stmt.execute(finalQuery.toString());
					successCount++;
					ticketStatistics.setRecordsInserted(successCount);
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
					ticketStatistics.setComments("Exception occured while Processing the File");
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
			ticketStatistics.setComments("Exception occured while Processing the File");
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
		if(failureCount > 0){
			try{
				skipFirstRow = false;
				stmt.execute("truncate table ticket_ftp_temp_data");
			}catch (SQLException e) {
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
		}
		
		if(!isFailed){
			try{
				String mainQuery = "insert into TICKET_DATA (col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50) select col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50 from TICKET_FTP_TEMP_DATA";
				LOG.info(mainQuery);
				boolean flag = stmt.execute(mainQuery);
				if(flag){
					stmt.execute("truncate table ticket_ftp_temp_data");
				}
			}catch (SQLException e) {
				LOG.error(e);
				TicketLogStatistics ticketLogStatistics = new TicketLogStatistics();
				ticketLogStatistics.setTicketId(ticketId);
				ticketLogStatistics.setMessage(e.getMessage());
				ticketLogStatisticsList.add(ticketLogStatistics);
				ticketStatistics.setTicketLogStatistics(ticketLogStatisticsList);
				failureCount++;
				ticketStatistics.setRecordsFailed(ticketStatistics.getRecordsFailed() + failureCount);
				ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
				ticketStatistics.setComments("Exception occured while moving data from Hive table to another");
				isFailed = true;
			}
			
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatistics.setComments("Data inserted into HDFS");
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
			ticketStatisticsRepository.save(ticketStatistics);
		}else{
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatisticsRepository.save(ticketStatistics);
		}
		closeConnection(con, stmt);
		return isFailed;
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
			cellValue = currentCell.getStringCellValue(); //DataMaskUtil.maskData(currentCell.getStringCellValue());
			query.append(cellValue).append("\"").append(",");
		} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			double value = currentCell.getNumericCellValue();
			if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
				cellValue = DateUtil.convertDateToString(currentCell.getDateCellValue());
				query.append(cellValue).append("\"").append(",");
			}else{
				query.append(currentCell.getNumericCellValue()).append("\"").append(",");
				cellValue = String.valueOf(value);
			}
			
			
		} else{
			query.append("").append("\"").append(",");
		}
		return cellValue;
	}
	
	private TicketStatistics getTicketStatistics(String fileName) {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName((String) env.getProperty("ticketsystem"));
		ticketStatistics.setCustomer((String) env.getProperty("customer"));
		ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setFileName(fileName);
		ticketStatistics.setComments("Excel downloaded successfully");
		List<TicketStatistics>  list = ticketStatisticsRepository.findAllByFileNameOrderByJobIdDesc(fileName);
		if(!CollectionUtils.isEmpty(list)){
			ticketStatistics.setVersionNumber(list.size()+1);
		}else{
			ticketStatistics.setVersionNumber(1);
		}
		
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
