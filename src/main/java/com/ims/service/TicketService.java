package com.ims.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ims.constant.SourceType;
import com.ims.constant.StatusType;
import com.ims.entity.FieldConfiguration;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.Ticket;
import com.ims.entity.TicketLogStatistics;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DateUtil;
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
	
	public void updateTicketData(String result, TicketStatistics ticketStatistics) throws ImsException {
		Connection con = null;
		Statement stmt = null;
		List<TicketStatistics>  list = ticketStatisticsRepository.findAllByFileNameOrderByJobIdDesc(null);
		if(!CollectionUtils.isEmpty(list)){
			ticketStatistics.setVersionNumber(list.size());
		}else{
			ticketStatistics.setVersionNumber(1);
		}
		List<FieldConfiguration> fields = fieldConfigurationRepository.findPropertyBySystemNameOrderById((String)env.getProperty("ticketsystem"));
		QueryBuilder queryBuilder = new QueryBuilder();
		ticketStatistics.setComments("Scheduler pulled the data from ticketing system");
		updateTicketStatistics(ticketStatistics);
		String systemName = (String)env.getProperty("ticketsystem");
		String customer = (String)env.getProperty("customer");
		StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository, systemName, customer,"API");
		LOG.info("Result in Service === " + result);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray records = jsonObj.getJSONArray("result");
		try {
			if (records != null && records.length() != 0) {
				con = getConnection();
				ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
				ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
				ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
				ticketStatistics = updateTicketStatistics(ticketStatistics);
				stmt = con.createStatement();
				updateDataToHDFS(queryBuilder, qBuilder, records, stmt, ticketStatistics, fields);
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data inserted into HDFS");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				updateTicketStatistics(ticketStatistics);
				closeConnection(con, stmt);
			}
		} catch (SQLException e) {
			ticketStatistics.setComments("Exception while processign data with Postgresql database");
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			closeConnection(con, stmt);
			throw new ImsException("Exception while processign data with Postgresql database", e);
		} catch (ImsException e) {
			ticketStatistics.setComments("Exception occured while processing the data");
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			closeConnection(con, stmt);
			throw new ImsException("Exception while processing data with Hive database", e);
		}
	}



	private void updateDataToHDFS(QueryBuilder queryBuilder, StringBuilder qBuilder, JSONArray records, Statement stmt, TicketStatistics ticketStatistics, List<FieldConfiguration> fields) throws ImsException, SQLException {
		Long successCount = 0l;
		Long failureCount = 0l;
		ticketStatistics.setRecordsInserted(0l);
		ticketStatistics.setRecordsFailed(0l);
		boolean isFailed = false;
		String ticketId;
		List<TicketLogStatistics> ticketLogStatisticsList = new ArrayList<>();
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
			prepareQuery(record, query, ticketStatistics, fields);
			
				try{
					String tempQueryBuilder = query.toString().substring(0, query.lastIndexOf(","));
					StringBuilder finalQuery = new StringBuilder(tempQueryBuilder).append(")");
					stmt.execute(finalQuery.toString());
					successCount++;
					ticketStatistics.setRecordsInserted(successCount);
				}catch (SQLException e) {
					LOG.error(e);
					ticketId = (String) record.get((String)env.getProperty("ticketid").trim());
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
		
		if(failureCount > 0){
			try{
				stmt.execute("truncate table ticket_ftp_temp_data");
			}catch (SQLException e) {
				LOG.error(e);
				isFailed = true;
			}
		}
		
		if(!isFailed){
			try{
				String mainQuery = "insert into TICKET_DATA2 (col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50) select col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20,col21,col22,col23,col24,col25,col26,col27,col28,col29,col30,col31,col32,col33,col34,col35,col36,col37,col38,col39,col40,col41,col42,col43,col44,col45,col46,col47,col48,col49,col50 from ticket_api_temp_data";
				LOG.info(mainQuery);
				stmt.execute(mainQuery);
				stmt.execute("truncate table ticket_api_temp_data");
			}catch (SQLException e) {
				LOG.error(e);
			}
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatistics.setComments("Data inserted into HDFS");
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
			ticketStatisticsRepository.save(ticketStatistics);
			updateLastRunDate(ticketStatistics);
		}else{
			ticketStatistics.setTotalRecords(ticketStatistics.getRecordsFailed()+ ticketStatistics.getRecordsInserted());
			ticketStatistics.setAutomationEndDate(new Date());
			ticketStatisticsRepository.save(ticketStatistics);
		}
	}

	private boolean prepareQuery(JSONObject record, StringBuilder query, TicketStatistics ticketStatistics, List<FieldConfiguration> fields) throws ImsException {
		boolean isRecordExists = false;
		try{
			 
			 query.append("\"");
			 query.append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
			 query.append("\"");
			 query.append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(",");
			 
			String tempField;
		 	 for(FieldConfiguration field:fields){
		 		LOG.info("Field   === >> "+field.getProperty());
		 		 if("assignment_group".equals(field.getProperty()) || "cmdb_ci".equals(field.getProperty())){
		 			tempField = "";
		 		 }else{
		 			 tempField = ((String) record.get(field.getProperty())).replace("\"", "\\\"");
		 		 }
		 		query.append("\"");
		 		query.append(tempField).append("\"").append(",");
		 	 }
			LOG.info(" \n \n "+query.toString());
		 }catch (Exception e) {
				LOG.error(e);
				throw new ImsException("Exception while processing data with Hive database", e);
		 }
		 return isRecordExists;
	}

	public TicketStatistics updateTicketStatistics(TicketStatistics ticketStatistics) {
		ticketStatistics.setSource(SourceType.API.getDescription());
		return ticketStatisticsRepository.save(ticketStatistics);
	}
	
	public Connection getConnection() throws ImsException {
		try {
			Class.forName((String)env.getProperty("hive.driver-class-name"));
			return DriverManager.getConnection((String)env.getProperty("hive.url"), (String)env.getProperty("hive.username"), (String)env.getProperty("hive.password"));
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error(e);
			throw new ImsException("",e);
		}
	}

	public List<Ticket> getTicketData(){
		return ticketRepository.findAll();
	}
	
	public String updateLastRunDate(TicketStatistics ticketStatistics) {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("apilastrundate");
		configuration.setValue(DateUtil.convertDateToString(ticketStatistics.getAutomationStartDate()));
		imsConfigurationRepository.save(configuration);
		return "Last Run Date Updated";
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
