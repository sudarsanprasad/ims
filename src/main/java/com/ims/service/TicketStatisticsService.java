package com.ims.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DateUtil;

@Service
public class TicketStatisticsService {
	
	private static final Logger LOG = Logger.getRootLogger();
	
	private static String startDate = "automation_start_date >=";
	private static String endDate = "AND automation_end_date <=";
	private static String and = "AND";
	private static String where = "where";
	
	@Autowired
    private EntityManager entityManager;

	@Autowired
    private TicketStatisticsRepository ticketStatisticsRepository;
	
	public TicketStatistics create(TicketStatistics ticketStatistics) {
		return ticketStatisticsRepository.save(ticketStatistics);
	}

	public TicketStatistics update(TicketStatistics ticketStatistics) {
		return ticketStatisticsRepository.save(ticketStatistics);
	}
	
	public TicketStatistics findById(Long ticketStatisticsId) {
		return ticketStatisticsRepository.findOne(ticketStatisticsId);
	}

	public void delete(Long ticketStatisticsId) {
		ticketStatisticsRepository.delete(ticketStatisticsId);
		
	}

	public List<TicketStatistics> findAll() {
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "jobId"));
		return ticketStatisticsRepository.findAll(sort);
	}
	
	public List<TicketStatistics> findAllByFileNameOrderByJobId(){
		return ticketStatisticsRepository.findAllByOrderByJobIdDesc();
	}
	
	public TicketStatistics findMostRecentRecord(){
		TicketStatistics ticketStatistics = null;
		List<TicketStatistics> ticketStatisticsList = ticketStatisticsRepository.findAllByOrderByJobIdDesc();
		if(!CollectionUtils.isEmpty(ticketStatisticsList)){
			ticketStatistics = ticketStatisticsList.get(0);
		}
		return ticketStatistics;
	}

	public List<TicketStatistics> getCurrentRecords() {
		return ticketStatisticsRepository.findAllByAutomationStatusOrderByJobIdDesc(StatusType.INPROGRESS.getDescription());
	}

	public TicketStatistics getCurrentRecordStatus(Long id) {
		return ticketStatisticsRepository.findByJobId(id);
	}

	public List<TicketStatistics> getStatistics(String customerName) {
		return ticketStatisticsRepository.findDistinctByCustomer(customerName);
	}

	public List<String> getSystemNames() {
		return ticketStatisticsRepository.findDistinctSystems();
	}

	@SuppressWarnings("unchecked")
	public List<TicketStatistics> getStatistics(TicketStatistics ticketStatistics) {
		StringBuilder queryBuilder = new StringBuilder("select ts from TicketStatistics ts ");
		StringBuilder systemBuiler = new StringBuilder();
		if (ticketStatistics.getSource() != null) {
			queryBuilder.append("where ");
			queryBuilder.append("source = '");
			queryBuilder.append(ticketStatistics.getSource()).append("'");
			if(ticketStatistics.getSystemNames() != null){
				queryBuilder.append(" ").append(and).append(" ");
				queryBuilder.append("system_name in (");
				for(String name:ticketStatistics.getSystemNames()){
					systemBuiler.append("'").append(name).append("'").append(",");
				}
				String names = systemBuiler.toString().substring(0, systemBuiler.lastIndexOf(","));
				queryBuilder.append(names);
				queryBuilder.append(")");
			}
			if(ticketStatistics.getAutomationStartDate() != null){
				queryBuilder.append(" AND ");
				appendDates(ticketStatistics, queryBuilder);
			}
		}else if(!CollectionUtils.isEmpty(ticketStatistics.getSystemNames())){
			queryBuilder.append(where).append(" ");
			queryBuilder.append("system_name in (");
			for(String name:ticketStatistics.getSystemNames()){
				systemBuiler.append("'").append(name).append("'").append(",");
			}
			String names = systemBuiler.toString().substring(0, systemBuiler.lastIndexOf(","));
			queryBuilder.append(names);
			queryBuilder.append(")");
			if(ticketStatistics.getAutomationStartDate() != null){
				queryBuilder.append(" AND ");
				appendDates(ticketStatistics, queryBuilder);
			}
		}else if(ticketStatistics.getAutomationStartDate() != null){
			queryBuilder.append(where).append(" ");
			appendDates(ticketStatistics, queryBuilder);
		}
		LOG.info("Query ===>>> "+queryBuilder.toString());
		Query query = entityManager.createQuery(queryBuilder.toString());
		return (List<TicketStatistics>) query.getResultList();
	}

	private void appendDates(TicketStatistics ticketStatistics, StringBuilder queryBuilder) {
		queryBuilder.append(" ").append(startDate).append(" '");
		queryBuilder.append(DateUtil.convertDateToString(ticketStatistics.getAutomationStartDate()));
		queryBuilder.append("' ").append(and).append(" ").append(endDate).append(" '");
		queryBuilder.append(DateUtil.convertDateToString(ticketStatistics.getAutomationEndDate())).append("'");
	}
	
}
