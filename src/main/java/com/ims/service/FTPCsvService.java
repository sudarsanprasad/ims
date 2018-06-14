package com.ims.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
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
import com.ims.util.QueryBuilder;

@Service
public class FTPCsvService {

	private static final Logger LOG = Logger.getLogger(FTPCsvService.class);

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
		
		String location = (String)env.getProperty("file.location");
		for (FTPFile file : files) {
			SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			String formattedDate = formatDate.format(file.getTimestamp().getTime());
			fileName = file.getName();
			LOG.info(file.getName() + "     " + formattedDate);
		}
		String pathName = location+fileName;
		
		try {
			isFileSavedToLocalFlag = template.get(fileName, inputStream -> FileCopyUtils.copy(inputStream, new FileOutputStream(new File(pathName))));
			if (isFileSavedToLocalFlag) {
				TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics(fileName));
				processExcelData(pathName, ticketStatistics, systemName, customer);
			}
		} catch (Exception ex) {
			LOG.error(ex);
			throw new ImsException(
					"Exception occured while processing csv data", ex);
		}
		
		return isFileSavedToLocalFlag;
	}

	private void processExcelData(String filename, TicketStatistics ticketStatistics, String systemName, String customer) {
		Long successCount = 0l;
		Long failureCount = 0l;
		ticketStatistics.setRecordsInserted(0l);
		ticketStatistics.setRecordsFailed(0l);
		boolean isFailed = false;
		Connection con = null;
		Statement stmt = null;
		String ticketId = null;
		int skipFirstRow = 0;
		List<TicketLogStatistics> ticketLogStatisticsList = new ArrayList<>();
		QueryBuilder queryBuilder = new QueryBuilder();
		StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository, systemName, customer,"FTP");
		try {
			con = getConnection();
			stmt = con.createStatement();
			ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
			ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
			ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
			ticketStatistics.setComments("Reading the data from CSV in Progress");
			ticketStatisticsRepository.save(ticketStatistics);
			
            Reader reader = java.nio.file.Files.newBufferedReader(Paths.get(filename));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("Incident ID", "Affected Service", "Affective Service Captured", "Affected CI", "Area", "Subarea", "Title", "Open Time", "Close Time", "Resolution Time", "Priority", "Assignment Group", "Client Ticket", "Solution").withIgnoreHeaderCase().withTrim());
               
            	for (CSVRecord csvRecord : csvParser) {
            		try{ 
            			if(skipFirstRow > 0){
    	            		StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
    		            	
    		                // Accessing values by the names assigned to each column
    		                String incidentId = csvRecord.get("Incident ID");
    		                String aService = csvRecord.get("Affected Service");
    		                String aServiceCaptured = csvRecord.get("Affective Service Captured");
    		                String aCi = csvRecord.get("Affected CI");
    		                String area = csvRecord.get("Area");
    		                String subArea = csvRecord.get("Subarea");
    		                String title = csvRecord.get("Title");
    		                String openTime = csvRecord.get("Open Time");
    		                String closeTime = csvRecord.get("Close Time");
    		                String resolutionTime = csvRecord.get("Resolution Time");
    		                String priority = csvRecord.get("Priority");
    		                String aGroup = csvRecord.get("Assignment Group");
    		                String clientTicket = csvRecord.get("Client Ticket");
    		                String solution = csvRecord.get("Solution");
    		                
    		                ticketId = incidentId;
    		                query.append("\"").append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
    						query.append("\"").append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(",");
    		                query.append("\"").append(incidentId).append("\"").append(",");
    		                query.append("\"").append(aService).append("\"").append(",");
    		                query.append("\"").append(aServiceCaptured).append("\"").append(",");
    		                query.append("\"").append(aCi).append("\"").append(",");
    		                query.append("\"").append(area).append("\"").append(",");
    		                query.append("\"").append(subArea).append("\"").append(",");
    		                query.append("\"").append(title).append("\"").append(",");
    		                query.append("\"").append(openTime).append("\"").append(",");
    		                query.append("\"").append(closeTime).append("\"").append(",");
    		                query.append("\"").append(resolutionTime).append("\"").append(",");
    		                query.append("\"").append(priority).append("\"").append(",");
    		                query.append("\"").append(aGroup).append("\"").append(",");
    		                query.append("\"").append(clientTicket).append("\"").append(",");
    		                query.append("\"").append(solution).append("\"");
    		                query.append(")");
    		                LOG.info("Query --->>"+query.toString());
    		                stmt.execute(query.toString());
    						successCount++;
    						ticketStatistics.setRecordsInserted(successCount);
    						ticketStatistics.setSource(SourceType.FTP.getDescription()); 
    	            		}
            		}catch(Exception e){
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
	            		skipFirstRow++;
	            	}
	            csvParser.close();
	            if(failureCount > 0){
	    			try{
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
	    				String mainQuery = "insert into TICKET_DATA2 (col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50) select col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50 from TICKET_FTP_TEMP_DATA";
	    				LOG.info(mainQuery);
	    				stmt.execute(mainQuery);
    					stmt.execute("truncate table ticket_ftp_temp_data");
	    			}catch (SQLException e) {
	    				LOG.error(e);
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
		} catch (OLE2NotOfficeXmlFileException | IOException | ImsException | SQLException e ) {
			isFailed = true;
			LOG.error(e);
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			ticketStatistics.setComments("Exception occured While Reading the File");
		} 
		if(isFailed){
			ticketStatisticsRepository.save(ticketStatistics);
		}
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
	
	private void closeConnection(Connection con, Statement stmt) {
		try {
			con.close();
			stmt.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		
	}
}
