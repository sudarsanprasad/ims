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
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	private static final Logger LOG = Logger.getRootLogger();

	

	@Autowired
	private Environment env;

	@Autowired
	TicketMetadataRepository ticketMetadataRepository;

	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Value("${ppm.file.location.in}")
	String ppmLocation;

	public boolean downloadExcel(TicketSystem system) throws ImsException {
		
		List<TicketSystem> list = ticketSystemRepository.findByCustomerAndEnableFlagAndType(system.getCustomer(), "Y", "FTP");
		
		
		String location = env.getProperty("file.location");
		
		LOG.info("Configured Location === >>"+location);
		boolean isFileSavedToLocalFlag = false;
		String fileName;
		try{
		for(TicketSystem ticketSystem:list){
			DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
			factory.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE); 
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
				readFileFromFtp(fileName, template, pathName, fileType);
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
				readFile(location, file, pathName, fileType);
		    }
		}
		}catch(Exception e){
			LOG.error(e);
		}
		return isFileSavedToLocalFlag;
	}

	private void readFileFromFtp(String fileName, FtpRemoteFileTemplate template, String pathName, String fileType) {
		if(FileType.XLS.getDescription().equalsIgnoreCase(fileType) || FileType.XLSX.getDescription().equalsIgnoreCase(fileType)|| FileType.CSV.getDescription().equalsIgnoreCase(fileType)){
			template.get(fileName, inputStream -> FileCopyUtils.copy(inputStream, new FileOutputStream(new File(pathName))));
		}
	}

	private void readFile(String location, File file, String pathName, String fileType) throws ImsException {
		if(FileType.XLS.getDescription().equalsIgnoreCase(fileType) || FileType.XLSX.getDescription().equalsIgnoreCase(fileType)|| FileType.CSV.getDescription().equalsIgnoreCase(fileType)){
			LOG.info("XL location ==>> "+pathName);
			processFile(location, FileNameUtil.getSystemName(file.getName()), FileNameUtil.getCustomerName(file.getName()), file, pathName, fileType, file.getName());
		}
	}

	void processFile(String location, String systemName, String customer, File file, String pathName, String fileType, String fileName) throws ImsException {
		TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics(file.getName(), systemName, customer));
		List<TicketMetadata> systemFields = ticketMetadataRepository.findBySystemNameAndIsProactiveOrderById(systemName, "Y");
		List<TicketMetadata> krFields = ticketMetadataRepository.findBySystemNameAndIsKnowledgementOrderById(systemName, "Y");
		StringBuilder tableBuilder = createTempTableQuery(systemName, systemFields);
		
		LOG.info("Teamp Table Query  ==>> "+tableBuilder);
		try(Connection con = getConnection();Statement stmt = con.createStatement()) {
				stmt.execute(tableBuilder.toString());
				LOG.info("location ==>> "+location);
				LOG.info("systemName ==>> "+systemName);
				LOG.info("file ==>> "+file);
				LOG.info("pathName ==>> "+pathName);
				LOG.info("fileName == >> "+fileName);
				
				String csvFileName;
				String ppmFileName = null;
				if(FileType.CSV.getDescription().equals(fileType)){
					csvFileName = file.getName();
				}else{
					String[] fileVar = fileName.split("\\.");
					csvFileName = location+fileVar[0]+".csv";
					ppmFileName = ppmLocation+fileVar[0]+"_PPM"+".csv";
				}
				
				ExcelToCsvUtil excelToCsvUtil = new ExcelToCsvUtil();
				excelToCsvUtil.readExcelFile(pathName, csvFileName, ppmFileName, krFields, systemName, customer);
				LOG.info("csvFileName ==>> "+csvFileName);
				int recordsCount = excelToCsvUtil.getRecordsCount(pathName);
				LOG.info("Count ==>> "+recordsCount);
				
				StringBuilder queryBuilder = new StringBuilder("load data local inpath \"");
				queryBuilder.append(csvFileName).append("\" into table temp_ims_").append(systemName);
				LOG.info("Query Builder === >>"+queryBuilder.toString());
				
				stmt.execute("truncate table temp_ims_"+systemName);
				stmt.execute(queryBuilder.toString());
				
				QueryBuilder prepareQuery = new QueryBuilder();
				StringBuilder qBuilder = prepareQuery.buildHiveQuery(ticketMetadataRepository, systemName, customer,"FTP");
				StringBuilder query = prepareQuery.getSelectValue(qBuilder);
				
				List<TicketMetadata> metadata =  ticketMetadataRepository.findBySystemNameAndCustomerOrderById(systemName, customer);
				if(!CollectionUtils.isEmpty(metadata)){
					for(TicketMetadata data : metadata){
						buildQuery(systemName, customer, ticketStatistics,query, data);
					}
				}
				StringBuilder finalQuery = prepareQuery.getFromValue(query, "temp_ims_"+systemName);
				LOG.info(finalQuery.toString());
				stmt.execute(finalQuery.toString());
				deleteFiles(file, csvFileName);
				ticketStatistics.setRecordsInserted(Long.valueOf(recordsCount));
				ticketStatistics.setRecordsFailed(0l);
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data Inserted successfully");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
				ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
				ticketStatistics.setTotalRecords(ticketStatistics.getRecordsInserted().longValue()+ticketStatistics.getRecordsFailed().longValue());
				ticketStatisticsRepository.save(ticketStatistics);
				closeConnection(con, stmt);
			} catch (Exception ex) {
				if(!ex.getMessage().contains("SASL authentication")){
					ticketStatistics.setRecordsInserted(0l);
					ticketStatistics.setRecordsFailed(0l);
					ticketStatistics.setAutomationEndDate(new Date());
					ticketStatistics.setComments("Failed to insert the data");
					ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
					ticketStatistics.setTotalRecords(0l);
					ticketStatisticsRepository.save(ticketStatistics);
					LOG.error(ex);
					throw new ImsException("Exception occured while processing excel data", ex);
				}
			}
	}

	private StringBuilder createTempTableQuery(String systemName, List<TicketMetadata> systemFields) {
		StringBuilder tempTableBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS temp_ims_").append(systemName).append("(");
		for(TicketMetadata field:systemFields){
			tempTableBuilder.append(field.getBusinessColumn()).append(" string, ");
		}
		String builder = tempTableBuilder.toString().substring(0, tempTableBuilder.lastIndexOf(","));
		StringBuilder tableBuilder = new StringBuilder(builder).append(") COMMENT 'port_data ' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\,' STORED AS TEXTFILE LOCATION '/apps/hive/warehouse/ims.db/temp_ims_");
		tableBuilder.append(systemName).append("'");
		return tableBuilder;
	}

	private void buildQuery(String systemName, String customer,
			TicketStatistics ticketStatistics, StringBuilder query,
			TicketMetadata data) {
		if("jobid".equalsIgnoreCase(data.getBusinessColumn())){
			query.append("\"").append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
		}else if("version".equalsIgnoreCase(data.getBusinessColumn())){
			query.append("\"").append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(",");
		}else if("customername".equalsIgnoreCase(data.getBusinessColumn())){
			query.append("\"").append(customer).append("\"").append(",");
		}else if("systemname".equalsIgnoreCase(data.getBusinessColumn())){
			query.append("\"").append(systemName).append("\"");
		}else{
			query.append(data.getBusinessColumn()).append(",");
		}
	}

	private void deleteFiles(File file, String csvFileName) {
		LOG.info("Deleting file ====>> "+file);
		if(file.delete()){
			LOG.info("Deleted file ====>> "+file);
		}
		File csvFile = new File(csvFileName);
		LOG.info("Deleting csv file ====>> "+csvFile);
		if(csvFile.delete()){
			LOG.info("Deleted csv file ====>> "+csvFile);
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
	
	public TicketStatistics getTicketStatistics(String fileName, String systemName, String customer) {
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
