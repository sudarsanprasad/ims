package com.ims.taskconfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ims.constant.StatusType;
import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketSystemRepository;
import com.ims.service.FTPService;
import com.ims.service.ImsConfigurationService;
import com.ims.service.ImsJobService;
import com.ims.service.TicketService;
import com.ims.service.TicketStatisticsService;
import com.ims.util.DateUtil;

@Component
public class ScheduledTasks {
	
	private static final Logger LOG = Logger.getLogger(ScheduledTasks.class);
	
	@Autowired
	TicketSystemRepository ticketSystemRepository;
	
	@Autowired
	ImsJobService imsJobService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketStatisticsService ticketStatisticsService;
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	@Autowired
	ImsConfigurationService imsConfigurationService;
	
	@Autowired
	private FTPService ftpService;
	

	//@Scheduled(cron = "${shedule.time.sec}")
	public void performApiTaskUsingCron() throws ImsException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		LOG.info("Regular API task performed using Cron at "+ dateFormat.format(new Date()));
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("apischedulerflag").getValue())){
			TicketStatistics ticketStatistics = ticketService.updateTicketStatistics(getTicketStatistics());
			ticketService.updateTicketData(getRecords(), ticketStatistics);
		}
	}
	
	private String getRecords() {
		 RestTemplate restTemplate = new RestTemplate();
		 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(env.getProperty("servicenow.username"), env.getProperty("servicenow.password")));
		 return restTemplate.getForObject(getUrl(), String.class);
	}
	
	private TicketStatistics getTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName((String)env.getProperty("ticketsystem"));
		ticketStatistics.setCustomer((String)env.getProperty("customer"));
		ticketStatistics.setAutomationStatus(StatusType.OPEN.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setComments("Scheduler started successfully");
		return ticketStatistics;
	}
	
	private String getUrl(){
		 ImsConfiguration configuration = imsConfigurationRepository.findByProperty("apilastrundate");
		 String dateAndTime[] = DateUtil.getDateAndTime(configuration.getValue());
		 StringBuilder ticketURL = new StringBuilder(env.getProperty("ticketsystem.url"));
		 ticketURL.append("('").append(dateAndTime[0]).append("','").append(dateAndTime[1]).append("')");
		 return ticketURL.toString();
	}
	
	/*@Scheduled(cron = "${shedule.time.sec}")
	public boolean performFtpTaskUsingCron() throws ImsException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		LOG.info("Regular FTP task performed using Cron at "+ dateFormat.format(new Date()));
		if(imsConfigurationService.isFtpAutomationOn()){
			return ftpService.downloadExcel();
		}
		return false;
	}*/
	
	@Scheduled(cron = "0/60 * * * * ?")
	public void createJobs() throws ImsException {
		List<TicketSystem> ticketSystems =  ticketSystemRepository.findAll();
		imsJobService.createJobs(ticketSystems);
	}
		
}
