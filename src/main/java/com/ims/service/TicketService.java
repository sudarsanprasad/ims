package com.ims.service;

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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ims.constant.StatusType;
import com.ims.entity.Ticket;
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
		StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository);
		LOG.info("Result in Service === " + result);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray records = jsonObj.getJSONArray("result");
		try {
			if (records != null && records.length() != 0) {
				Connection con = getConnection();
				ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
				updateTicketStatistics(ticketStatistics);
				Statement stmt = con.createStatement();
				updateDataToHDFS(queryBuilder, qBuilder, records, stmt);
				stmt.close();
				con.close();
				ticketStatistics.setAutomationEndDate(new Date());
				ticketStatistics.setComments("Data inserted into HDFS");
				ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
				updateTicketStatistics(ticketStatistics);
			}
		} catch (SQLException e) {
			ticketStatistics.setComments("Exception while processign data with Postgresql database");
			ticketStatistics.setAutomationStatus(StatusType.ABORTED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			throw new ImsException("Exception while processign data with Postgresql database", e);
		} catch (ImsException e) {
			ticketStatistics.setComments("Exception occured while processing the data");
			ticketStatistics.setAutomationStatus(StatusType.ABORTED.getDescription());
			updateTicketStatistics(ticketStatistics);
			LOG.error(e);
			throw new ImsException("Exception while processing data with Hive database", e);
		}
	}



	private void updateDataToHDFS(QueryBuilder queryBuilder,
			StringBuilder qBuilder, JSONArray records, Statement stmt)
			throws ImsException, SQLException {
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
			prepareQuery(record, query);
			stmt.execute(query.toString());
		}
	}

	

	private void prepareQuery(JSONObject record, StringBuilder query) throws ImsException {
		 try{
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
			 query.append((String) record.get((String)env.getProperty("priority"))).append("\"").append(")");
			 LOG.info(" \n \n "+query.toString());
		 }catch (Exception e) {
				LOG.error(e);
				throw new ImsException("Exception while processing data with Hive database", e);
			}
	}

	public TicketStatistics updateTicketStatistics(TicketStatistics ticketStatistics) {
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
