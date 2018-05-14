package com.ims.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
import com.ims.dto.TicketDataDto;
import com.ims.entity.Ticket;
import com.ims.entity.TicketLogStatistics;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
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
	
	public void updateTicketData(String result, TicketStatistics ticketStatistics) throws ImsException {
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
				List<TicketDataDto> dtos = getDataFromHDFS(records);
				updateDataToHDFS(queryBuilder, qBuilder, records, stmt, dtos, ticketStatistics);
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



	private void updateDataToHDFS(QueryBuilder queryBuilder, StringBuilder qBuilder, JSONArray records, Statement stmt, List<TicketDataDto> dtos, TicketStatistics ticketStatistics) throws ImsException, SQLException {
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
			TicketDataDto dto = new TicketDataDto(); 
			boolean isRecordExists = prepareQuery(record, query, ticketStatistics, dtos, dto);
			
			if(!isRecordExists){
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
		}
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

	private boolean prepareQuery(JSONObject record, StringBuilder query, TicketStatistics ticketStatistics, List<TicketDataDto> dtos, TicketDataDto dto) throws ImsException {
		boolean isRecordExists = false;
		try{
		 	 dto.setCol1((String) record.get((String)env.getProperty("ticketid").trim()));
			 dto.setCol2((String) record.get((String)env.getProperty("description").trim()));
			 dto.setCol3((String) record.get((String)env.getProperty("shortdescription").trim()));
			 dto.setCol4((String) record.get((String)env.getProperty("comments").trim()));
			 dto.setCol5((String) record.get((String)env.getProperty("status").trim()));
			 dto.setCol6((String) record.get((String)env.getProperty("createddate").trim()));
			 dto.setCol7((String) record.get((String)env.getProperty("createdby").trim()));
			 dto.setCol8((String) record.get((String)env.getProperty("updateddate").trim()));
			 dto.setCol9((String) record.get((String)env.getProperty("category").trim()));
			 dto.setCol10((String) record.get((String)env.getProperty("priority").trim()));
			 dto.setCol11(String.valueOf(ticketStatistics.getJobId()));
			 isRecordExists = isRecordExists(dtos, dto);
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("ticketid").trim())).append("\"").append(",");
			 query.append("\"");
			 String description = (String) record.get((String)env.getProperty("description").replace("\"", "\\\""));
			 query.append(description.trim()).append("\"").append(",");
			 query.append("\"");
			 String shortDescription = (String) record.get((String)env.getProperty("shortdescription").replace("\"", "\\\""));
			 query.append(shortDescription.trim()).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("comments").trim())).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("status"))).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("createddate"))).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("createdby"))).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("updateddate"))).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("category"))).append("\"").append(",");
			 query.append("\"");
			 query.append((String) record.get((String)env.getProperty("priority"))).append("\"").append(",");
			 query.append("\"");
			 query.append(String.valueOf(ticketStatistics.getJobId())).append("\"").append(",");
			 query.append("\"");
			if(dto.getCol12() == null){
				query.append("1").append("\"").append(")");
			}else{
				query.append(dto.getCol12()).append("\"").append(")");
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
	
	private List<TicketDataDto> getDataFromHDFS(JSONArray records){
		String ticketIds = null;
		StringBuilder ticketIdBuilder = new StringBuilder();
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			ticketIdBuilder.append("'");
			ticketIdBuilder.append((String) record.get((String)env.getProperty("ticketid").trim()));
			ticketIdBuilder.append("'").append(",");
		}
		ticketIds = ticketIdBuilder.toString().substring(0,ticketIdBuilder.lastIndexOf(","));
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
	
	private void closeConnection(Connection con, Statement stmt) {
		try {
			con.close();
			stmt.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		
	}
	
}
