package com.ims.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.FieldConfiguration;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketMetadata;
import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.util.DataMaskUtil;
import com.ims.util.DateUtil;
import com.ims.util.FileNameUtil;
import com.ims.util.JsonToCsvUtil;
import com.ims.util.QueryBuilder;

/**
 * 
 * @author RKB
 *
 */
@Service
public class TicketService {
	
	private static final Logger LOG = Logger.getLogger(TicketService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TicketMetadataRepository ticketMetadataRepository;
	
	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;
	
	@Autowired
	FieldConfigurationRepository fieldConfigurationRepository;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Value("${ppm.file.location.in}")
	String ppmLocation;
	
	public void updateDataToHDFS(String result, TicketSystem system) {
		List<FieldConfiguration> fields = fieldConfigurationRepository.findPropertyBySystemNameOrderById(system.getSystemName());
		String location = env.getProperty("api.file.location");
		LOG.info("API  File Configured Location === >>"+location);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray records = jsonObj.getJSONArray("result");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(system.getUserName(), system.getPassword()));
		int recordsCount = 0;
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			if("Service Now".equalsIgnoreCase(system.getSystemName())){
				String comments = getUrl(restTemplate, (String) record.get((String)env.getProperty("ticketid").trim()));
				String replaceString=comments.replace("System Administrator  Additional comments  n"," ");
				String maskedData = DataMaskUtil.maskData(replaceString);
				record.put("comments", DataMaskUtil.replaceSpecialChars(maskedData));
			}
			recordsCount++;
		}
		String systemName = system.getSystemName().replaceAll(" ", "").toLowerCase();
		TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics("NA", systemName, system.getCustomer()));
		LOG.info("Records Count ==>> "+recordsCount);
		JsonToCsvUtil jsonToCsvUtil = new JsonToCsvUtil();
		String fileName = FileNameUtil.getFileName(location, system);
		jsonToCsvUtil.prepareCsv(jsonObj, fields, fileName, FileNameUtil.getPpmFileName(ppmLocation, system));
		List<TicketMetadata> systemFields = ticketMetadataRepository.findBySystemNameAndIsProactiveOrderById(system.getSystemName(), "Y");
		StringBuilder tableBuilder = createTempTableQuery(systemName, systemFields);
		StringBuilder queryBuilder = new StringBuilder("load data local inpath \"");
		queryBuilder.append(FileNameUtil.getFileName(location, system)).append("\" into table temp_ims_").append(systemName);
		LOG.info("Query Builder === >>"+queryBuilder.toString());
		try(Connection con = getConnection();Statement stmt = con.createStatement()) {
			LOG.info("Teamp Table Query  ==>> "+tableBuilder);
			stmt.execute(tableBuilder.toString());
			stmt.execute("truncate table temp_ims_"+systemName);
			stmt.execute(queryBuilder.toString());
			QueryBuilder prepareQuery = new QueryBuilder();
			StringBuilder qBuilder = prepareQuery.buildHiveQuery(ticketMetadataRepository, systemName, system.getCustomer(),"API");
			StringBuilder query = prepareQuery.getSelectValue(qBuilder);
			
			List<TicketMetadata> metadata =  ticketMetadataRepository.findBySystemNameAndCustomerOrderById(systemName, system.getCustomer());
			if(!CollectionUtils.isEmpty(metadata)){
				for(TicketMetadata data : metadata){
					buildQuery(systemName, system.getCustomer(), ticketStatistics,query, data);
				}
			}
			StringBuilder finalQuery = prepareQuery.getFromValue(query, "temp_ims_"+systemName);
			LOG.info(finalQuery.toString());
			stmt.execute(finalQuery.toString());
			deleteFile(fileName);
			ticketStatistics.setRecordsInserted(Long.valueOf(recordsCount));
			ticketStatistics.setRecordsFailed(0l);
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatistics.setComments("Data Inserted successfully");
			ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
			ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
			ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsInserted()+ticketStatistics.getRecordsFailed());
			ticketStatisticsRepository.save(ticketStatistics);
			updateLastRunDate(ticketStatistics);
		} catch (SQLException e) {
			LOG.info(e);
		}
	}
	
	private void deleteFile(String csvFileName) {
		File csvFile = new File(csvFileName);
		LOG.info("Deleting csv file ====>> "+csvFile);
		if(csvFile.delete()){
			LOG.info("Deleted csv file ====>> "+csvFile);
		}
	}

	public Connection getConnection() {
		try {
			Class.forName((String)env.getProperty("hive.driver-class-name"));
			return DriverManager.getConnection((String)env.getProperty("hive.url"), (String)env.getProperty("hive.username"), (String)env.getProperty("hive.password"));
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error(e);
		}
		return null;
	}
	
	private String getUrl(RestTemplate restTemplate, String ticketId){
		 StringBuilder ticketURL = new StringBuilder(env.getProperty("comments.url"));
		 ticketURL.append(ticketId);
		 return restTemplate.getForObject(ticketURL.toString(), String.class);
	}
	
	public TicketStatistics getTicketStatistics(String fileName, String systemName, String customer) {
		TicketStatistics ticketApiStatistics = new TicketStatistics();
		ticketApiStatistics.setSystemName(systemName);
		ticketApiStatistics.setCustomer(customer);
		ticketApiStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
		ticketApiStatistics.setAutomationStartDate(new Date());
		ticketApiStatistics.setFileName(fileName);
		ticketApiStatistics.setSource("API");
		ticketApiStatistics.setComments("Scheduler started Successfully");
		List<TicketStatistics>  list = ticketStatisticsRepository.findAllByOrderByJobIdDesc();
		if(!CollectionUtils.isEmpty(list)){
			ticketApiStatistics.setVersionNumber(list.size()+1);
		}else{
			ticketApiStatistics.setVersionNumber(1);
		}
		
		return ticketApiStatistics;
	}
	
	void buildQuery(String systemName, String customer, TicketStatistics ticketApiStatistics, StringBuilder query, TicketMetadata apiData) {
		if("jobid".equalsIgnoreCase(apiData.getBusinessColumn())){
			query.append("\"").append(String.valueOf(ticketApiStatistics.getJobId())).append("\"").append(",");
		}else if("version".equalsIgnoreCase(apiData.getBusinessColumn())){
			query.append("\"").append(String.valueOf(ticketApiStatistics.getVersionNumber())).append("\"").append(",");
		}else if("customername".equalsIgnoreCase(apiData.getBusinessColumn())){
			query.append("\"").append(customer).append("\"").append(",");
		}else if("systemname".equalsIgnoreCase(apiData.getBusinessColumn())){
			query.append("\"").append(systemName).append("\"");
		}else{
			query.append(apiData.getBusinessColumn()).append(",");
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
	
	public void updateLastRunDate(TicketStatistics ticketStatistics) {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("servicenow.lastrundate");
		configuration.setValue(DateUtil.convertDateToString(ticketStatistics.getAutomationStartDate()));
		imsConfigurationRepository.save(configuration);
	}
	
}
