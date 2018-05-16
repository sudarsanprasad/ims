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
import com.ims.entity.Ticket;
import com.ims.entity.TicketLogStatistics;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
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
	
	public void updateTicketData(String result, TicketStatistics ticketStatistics) throws ImsException {
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
		StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository, systemName, customer);
		LOG.info("Result in Service === " + result);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray records = jsonObj.getJSONArray("result");
		try {
			if (records != null && records.length() != 0) {
				Connection con = getConnection();
				ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
				ticketStatistics.setForecastStatus(StatusType.OPEN.getDescription());
				ticketStatistics.setKnowledgeBaseStatus(StatusType.OPEN.getDescription());
				updateTicketStatistics(ticketStatistics);
				Statement stmt = con.createStatement();
				updateDataToHDFS(queryBuilder, qBuilder, records, stmt, ticketStatistics, fields);
				stmt.close();
				con.close();
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data inserted into HDFS");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				updateTicketStatistics(ticketStatistics);
			}
		} catch (SQLException e) {
			ticketStatistics.setComments("Exception while processign data with Postgresql database");
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			throw new ImsException("Exception while processign data with Postgresql database", e);
		} catch (ImsException e) {
			ticketStatistics.setComments("Exception occured while processing the data");
			ticketStatistics.setAutomationStatus(StatusType.FAILED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			throw new ImsException("Exception while processing data with Hive database", e);
		}
	}



	private void updateDataToHDFS(QueryBuilder queryBuilder, StringBuilder qBuilder, JSONArray records, Statement stmt, TicketStatistics ticketStatistics, List<FieldConfiguration> fields) throws ImsException, SQLException {
		Long successCount = 0l;
		Long failureCount = 0l;
		ticketStatistics.setRecordsInserted(0l);
		ticketStatistics.setRecordsFailed(0l);
		boolean isFailed = false;
		String ticketId = null;
		List<TicketLogStatistics> ticketLogStatisticsList = new ArrayList<>();
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
			prepareQuery(record, query, ticketStatistics, fields);
			
				try{
					stmt.execute(query.toString());
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
		if(!isFailed){
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
	}

	private boolean prepareQuery(JSONObject record, StringBuilder query, TicketStatistics ticketStatistics, List<FieldConfiguration> fields) throws ImsException {
		boolean isRecordExists = false;
		try{
			String tempField = null;
		 	 for(FieldConfiguration field:fields){
		 		 if("assignment_group".equals(field.getProperty()) || "cmdb_ci".equals(field.getProperty())){
		 			tempField = "";
		 		 }else{
		 			 tempField = ((String) record.get(field.getProperty())).replace("\"", "\\\"");
		 		 }
		 		query.append("\"");
		 		query.append(tempField).append("\"").append(",");
		 	 }
			 query.append("\"");
			 query.append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
			 query.append("\"");
			query.append(String.valueOf(ticketStatistics.getVersionNumber())).append("\"").append(")");
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
	
}
