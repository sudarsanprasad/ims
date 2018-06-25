package com.ims.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ims.entity.FieldConfiguration;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.util.DataMaskUtil;
import com.ims.util.FileNameUtil;
import com.ims.util.JsonToCsvUtil;

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
	
	@Value("${ppm.file.location}")
	String ppmLocation;
	
	public void updateDataToHDFS(String result, TicketSystem system) {
		List<FieldConfiguration> fields = fieldConfigurationRepository.findPropertyBySystemNameOrderById(system.getSystemName());
		String location = env.getProperty("api.file.location");
		LOG.info("API  File Configured Location === >>"+location);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray records = jsonObj.getJSONArray("result");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(system.getUserName(), system.getPassword()));
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			if("Service Now".equalsIgnoreCase(system.getSystemName())){
				String comments = getUrl(restTemplate, (String) record.get((String)env.getProperty("ticketid").trim()));
				String replaceString=comments.replace("System Administrator  Additional comments  n"," ");
				String maskedData = DataMaskUtil.maskData(replaceString);
				record.put("comments", DataMaskUtil.replaceSpecialChars(maskedData));
			}
		}
		JsonToCsvUtil jsonToCsvUtil = new JsonToCsvUtil();
		
		jsonToCsvUtil.prepareCsv(jsonObj, fields, FileNameUtil.getFileName(location, system), FileNameUtil.getPpmFileName(ppmLocation, system));
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
	
	private String getUrl(RestTemplate restTemplate, String ticketId){
		 StringBuilder ticketURL = new StringBuilder(env.getProperty("comments.url"));
		 ticketURL.append(ticketId);
		 return restTemplate.getForObject(ticketURL.toString(), String.class);
	}
	
}
