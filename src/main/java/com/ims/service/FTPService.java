package com.ims.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.google.common.io.Files;
import com.ims.constant.FileType;
import com.ims.constant.StatusType;
import com.ims.entity.TicketMetadata;
import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.util.ExcelToCsvUtil;
import com.ims.util.FileNameUtil;
import com.ims.util.QueryBuilder;

@Service
public class FTPService {

	private static final Logger LOG = Logger.getLogger(FTPService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	TicketMetadataRepository ticketMetadataRepository;

	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;

	public boolean downloadExcel() throws ImsException {
		
		List<TicketSystem> list = ticketSystemRepository.findByCustomerAndEnableFlagAndType("Deloitte", "Y", "FTP");
		
		
		String location = (String)env.getProperty("file.location");
		LOG.info("Configured Location === >>"+location);
		boolean isFileSavedToLocalFlag = false;
		String fileName;
		try{
		for(TicketSystem ticketSystem:list){
			DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
			LOG.info("URL === >>"+ticketSystem.getUrl());
			LOG.info("User Name === >>"+ticketSystem.getUserName());
			LOG.info("Password === >>"+ticketSystem.getPassword());
			factory.setHost(ticketSystem.getUrl());
			factory.setUsername(ticketSystem.getUserName());
			factory.setPassword(ticketSystem.getPassword());
			FtpRemoteFileTemplate template = new FtpRemoteFileTemplate(factory); 
			FTPFile[] files = template.list("");
			for (FTPFile file : files) {
				SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				String formattedDate = formatDate.format(file.getTimestamp().getTime());
				fileName = file.getName();
				String pathName = location+fileName;
				String fileType = Files.getFileExtension(pathName);
				LOG.info("Saving to location ===>> "+pathName);
				if("xls".equalsIgnoreCase(fileType)){
					template.get(fileName, inputStream -> FileCopyUtils.copy(inputStream, new FileOutputStream(new File(pathName))));
				}
				
				LOG.info(file.getName() + "     " + formattedDate);
			}
		}
		
		
		
		File folder = new File(location);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	String pathName = location+file.getName();
		    	LOG.info("Reading location == >> "+pathName);
				String fileType = Files.getFileExtension(pathName);
				if(FileType.XLS.getDescription().equalsIgnoreCase(fileType) || FileType.XLSX.getDescription().equalsIgnoreCase(fileType)|| FileType.CSV.getDescription().equalsIgnoreCase(fileType)){
					LOG.info("XL location ==>> "+pathName);
					processFile(location, FileNameUtil.getSystemName(file.getName()), FileNameUtil.getCustomerName(file.getName()), file, pathName, fileType, file.getName());
				}
		    }
		}
		}catch(Exception e){
			LOG.error(e);
		}
		return isFileSavedToLocalFlag;
	}

	private void processFile(String location, String systemName, String customer, File file, String pathName, String fileType, String fileName) throws ImsException {
		TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics(file.getName(), systemName, customer));
			try {
				LOG.info("location ==>> "+location);
				LOG.info("systemName ==>> "+systemName);
				LOG.info("file ==>> "+file);
				LOG.info("pathName ==>> "+pathName);
				LOG.info("fileName == >> "+fileName);
				
				String csvFileName;
				if(FileType.CSV.getDescription().equals(fileType)){
					csvFileName = file.getName();
				}else{
					String fileVar[] = fileName.split("\\.");
					csvFileName = location+fileVar[0]+".csv";
				}
				ExcelToCsvUtil excelToCsvUtil = new ExcelToCsvUtil();
				excelToCsvUtil.readExcelFile(pathName, csvFileName);
				LOG.info("csvFileName ==>> "+csvFileName);
				int recordsCount = excelToCsvUtil.getRecordsCount(pathName);
				LOG.info("Count ==>> "+recordsCount);
				Connection con = getConnection();
				Statement stmt = con.createStatement();
				StringBuilder queryBuilder = new StringBuilder("load data local inpath \"");
				queryBuilder.append(csvFileName).append("\" into table temp_ims_ampm");
				LOG.info("Query Builder === >>"+queryBuilder.toString());
				
				stmt.execute("truncate table temp_ims_ampm");
				stmt.execute(queryBuilder.toString());
				
				QueryBuilder prepareQuery = new QueryBuilder();
				StringBuilder qBuilder = prepareQuery.buildHiveQuery(ticketMetadataRepository, systemName, customer,"FTP");
				StringBuilder query = prepareQuery.getSelectValue(qBuilder);
				
				
				
				List<TicketMetadata> metadata =  ticketMetadataRepository.findBySystemNameAndCustomerOrderById(systemName, customer);
				if(!CollectionUtils.isEmpty(metadata)){
					for(TicketMetadata data : metadata){
						if(data.getBusinessColumn().equalsIgnoreCase("jobid")){
							query.append("\"").append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
						}else if(data.getBusinessColumn().equalsIgnoreCase("version")){
							query.append("\"").append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(",");
						}else if(data.getBusinessColumn().equalsIgnoreCase("customername")){
							query.append("\"").append(customer).append("\"").append(",");
						}else if(data.getBusinessColumn().equalsIgnoreCase("systemname")){
							query.append("\"").append(systemName).append("\"");
						}else{
							query.append(data.getBusinessColumn()).append(",");
						}
					}
				}
				StringBuilder finalQuery = prepareQuery.getFromValue(query, "temp_ims_ampm");
				LOG.info(finalQuery.toString());
				try{
					stmt.execute(finalQuery.toString());
				}catch(Exception ex){
					ex.printStackTrace();
				}
				LOG.info("Deleting file ====>> "+file);
				file.delete();
				File csvFile = new File(csvFileName);
				LOG.info("Deleting csv file ====>> "+csvFile);
				csvFile.delete();
				ticketStatistics.setRecordsInserted(Long.valueOf(recordsCount));
				ticketStatistics.setRecordsFailed(0l);
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data Inserted successfully");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				ticketStatistics.setTotalRecords(ticketStatistics.getRecordsInserted()+ticketStatistics.getRecordsFailed());
				ticketStatisticsRepository.save(ticketStatistics);
				closeConnection(con, stmt);
			} catch (Exception ex) {
				ticketStatistics.setRecordsInserted(Long.valueOf(0));
				ticketStatistics.setRecordsFailed(0l);
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data Inserted successfully");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				ticketStatistics.setTotalRecords(ticketStatistics.getRecordsInserted()+ticketStatistics.getRecordsFailed());
				ticketStatisticsRepository.save(ticketStatistics);
				ex.printStackTrace();
				throw new ImsException("Exception occured while processing excel data", ex);
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
	
	private TicketStatistics getTicketStatistics(String fileName, String systemName, String customer) {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName(systemName);
		ticketStatistics.setCustomer(customer);
		ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setFileName(fileName);
		ticketStatistics.setSource("FTP");
		ticketStatistics.setComments("Excel downloaded successfully");
		List<TicketStatistics>  list = ticketStatisticsRepository.findAllByOrderByJobIdDesc();
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
