package com.ims.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import com.ims.entity.ImsConfiguration;
import com.ims.entity.TicketMetadata;
import com.ims.entity.TicketStatistics;
import com.ims.entity.TicketSystem;
import com.ims.exception.ImsException;
import com.ims.repository.FieldConfigurationRepository;
import com.ims.repository.ImsConfigurationRepository;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.repository.TicketSystemRepository;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

	@Spy
	@InjectMocks
	private TicketService ticketService;

	@Mock
	private Environment env;

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private TicketMetadataRepository ticketMetadataRepository;

	@Mock
	TicketStatisticsRepository ticketStatisticsRepository;

	@Mock
	FieldConfigurationRepository fieldConfigurationRepository;

	@Mock
	ImsConfigurationRepository imsConfigurationRepository;

	@Mock
	TicketSystemRepository ticketSystemRepository;

	@Test
	public void updateTicketData() throws ImsException, SQLException {
		TicketSystem ticketSystem = constructTicketSystem();
		TicketStatistics ticketStatistics = constructTicketStatistics();
		Mockito.doReturn(ticketStatistics).when(ticketStatisticsRepository).save(Mockito.any(TicketStatistics.class));
		Mockito.doReturn("number").when(env).getProperty("ticketid");
		Mockito.doReturn("/tmp/ims/api/").when(env).getProperty("api.file.location");
		Mockito.doReturn(
				"https://dev29786.service-now.com/api/now/v2/table/incident?sysparm_display_value=true&sysparm_fields=comments&sysparm_query=number=")
				.when(env).getProperty("comments.url");
		ReflectionTestUtils.setField(ticketService, "ppmLocation","/tmp/ims/ppm/");
		Connection con=Mockito.mock(Connection.class);
		Statement stmt=Mockito.mock(Statement.class);
		Mockito.doReturn(con).when(ticketService).getConnection();
		Mockito.doReturn(stmt).when(con).createStatement();
		Mockito.doReturn(constructTicketMetaDataList()).when(ticketMetadataRepository).findBySystemNameAndIsProactiveOrderById(anyString(),anyString());
		Mockito.doReturn(constructTicketMetaDataList()).when(ticketMetadataRepository).findBySystemNameAndCustomerOrderById(anyString(),anyString());
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("servicenow.lastrundate")).thenReturn(imsConfiguration);
		ticketService.updateDataToHDFS(getResult(), ticketSystem);
	}

	private TicketSystem constructTicketSystem() {
		TicketSystem ticketSystem = new TicketSystem();
		ticketSystem.setId(10L);
		ticketSystem.setCustomer("Deloite");
		ticketSystem.setSystemName("Service Now");
		ticketSystem.setUserName("admin");
		ticketSystem.setPassword("Password@1");
		return ticketSystem;
	}

	private TicketStatistics constructTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setCustomer("Deloitte");
		ticketStatistics.setJobId(10L);
		ticketStatistics.setFileName("test.txt");
		ticketStatistics.setAutomationStartDate(new Date());
		return ticketStatistics;
	}
	
	private List<TicketMetadata> constructTicketMetaDataList() {
		List<TicketMetadata> ticketMetadataList = new ArrayList<TicketMetadata>();
		TicketMetadata ticketMetadata = new TicketMetadata();
		ticketMetadata.setId(10L);
		ticketMetadata.setBusinessColumn("jobid");
		ticketMetadata.setSystemName("Service Now");
		ticketMetadata.setMappingColumn("test");
		ticketMetadataList.add(ticketMetadata);
		return ticketMetadataList;
	}

	private String getResult() {
		return "{\"result\":[{\"parent\":\"\",\"made_sla\":\"true\",\"caused_by\":\"\",\"watch_list\":\"\",\"upon_reject\":\"cancel\",\"sys_updated_on\":\"2018-06-22 20:12:50\",\"child_incidents\":\"0\",\"hold_reason\":\"\",\"approval_history\":\"\",\"number\":\"INC0010101\",\"resolved_by\":\"\",\"sys_updated_by\":\"admin\",\"opened_by\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/6816f79cc0a8016401c5a33be04be441\",\"value\":\"6816f79cc0a8016401c5a33be04be441\"},\"user_input\":\"\",\"sys_created_on\":\"2018-06-17 10:25:57\",\"sys_domain\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/global\",\"value\":\"global\"},\"state\":\"1\",\"sys_created_by\":\"admin\",\"knowledge\":\"false\",\"order\":\"\",\"calendar_stc\":\"\",\"closed_at\":\"\",\"cmdb_ci\":\"\",\"delivery_plan\":\"\",\"impact\":\"3\",\"active\":\"true\",\"work_notes_list\":\"\",\"business_service\":\"\",\"priority\":\"3\",\"sys_domain_path\":\"/\",\"rfc\":\"\",\"time_worked\":\"\",\"expected_start\":\"\",\"opened_at\":\"2018-06-17 10:25:57\",\"business_duration\":\"\",\"group_list\":\"\",\"work_end\":\"\",\"caller_id\":\"\",\"reopened_time\":\"\",\"resolved_at\":\"\",\"approval_set\":\"\",\"subcategory\":\"\",\"work_notes\":\"\",\"short_description\":\"Test\",\"close_code\":\"\",\"correlation_display\":\"\",\"delivery_task\":\"\",\"work_start\":\"\",\"assignment_group\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/8a5055c9c61122780043563ef53438e3\",\"value\":\"8a5055c9c61122780043563ef53438e3\"},\"additional_assignee_list\":\"\",\"business_stc\":\"\",\"description\":\"\",\"calendar_duration\":\"\",\"close_notes\":\"\",\"notify\":\"1\",\"sys_class_name\":\"incident\",\"closed_by\":\"\",\"follow_up\":\"\",\"parent_incident\":\"\",\"sys_id\":\"4c0a1a340f7213004c53b36be1050ed0\",\"contact_type\":\"\",\"reopened_by\":\"\",\"incident_state\":\"1\",\"urgency\":\"1\",\"problem_id\":\"\",\"company\":\"\",\"reassignment_count\":\"0\",\"activity_due\":\"\",\"assigned_to\":\"\",\"severity\":\"3\",\"comments\":\"\",\"approval\":\"not requested\",\"sla_due\":\"\",\"comments_and_work_notes\":\"\",\"due_date\":\"\",\"sys_mod_count\":\"2\",\"reopen_count\":\"0\",\"sys_tags\":\"\",\"escalation\":\"0\",\"upon_approval\":\"proceed\",\"correlation_id\":\"\",\"location\":\"\",\"category\":\"inquiry\"},{\"parent\":\"\",\"made_sla\":\"false\",\"caused_by\":\"\",\"watch_list\":\"\",\"upon_reject\":\"\",\"sys_updated_on\":\"2018-03-01 19:51:14\",\"child_incidents\":\"\",\"hold_reason\":\"\",\"approval_history\":\"\",\"number\":\"INC0000003\",\"resolved_by\":\"\",\"sys_updated_by\":\"admin\",\"opened_by\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/681b365ec0a80164000fb0b05854a0cd\",\"value\":\"681b365ec0a80164000fb0b05854a0cd\"},\"user_input\":\"\",\"sys_created_on\":\"2016-07-04 14:41:46\",\"sys_domain\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/global\",\"value\":\"global\"},\"state\":\"2\",\"sys_created_by\":\"admin\",\"knowledge\":\"false\",\"order\":\"\",\"calendar_stc\":\"\",\"closed_at\":\"\",\"cmdb_ci\":\"\",\"delivery_plan\":\"\",\"impact\":\"1\",\"active\":\"true\",\"work_notes_list\":\"\",\"business_service\":\"\",\"priority\":\"1\",\"sys_domain_path\":\"/\",\"rfc\":\"\",\"time_worked\":\"\",\"expected_start\":\"\",\"opened_at\":\"2017-11-30 23:07:30\",\"business_duration\":\"\",\"group_list\":\"\",\"work_end\":\"\",\"caller_id\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/681ccaf9c0a8016400b98a06818d57c7\",\"value\":\"681ccaf9c0a8016400b98a06818d57c7\"},\"reopened_time\":\"\",\"resolved_at\":\"\",\"approval_set\":\"\",\"subcategory\":\"\",\"work_notes\":\"\",\"short_description\":\"Wireless access is down in my area\",\"close_code\":\"\",\"correlation_display\":\"\",\"delivery_task\":\"\",\"work_start\":\"\",\"assignment_group\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/287ebd7da9fe198100f92cc8d1d2154e\",\"value\":\"287ebd7da9fe198100f92cc8d1d2154e\"},\"additional_assignee_list\":\"\",\"business_stc\":\"\",\"description\":\"I just moved from floor 2 to floor 3 and my laptop cannot connect to any wireless network.\",\"calendar_duration\":\"\",\"close_notes\":\"\",\"notify\":\"1\",\"sys_class_name\":\"incident\",\"closed_by\":\"\",\"follow_up\":\"\",\"parent_incident\":\"\",\"sys_id\":\"e8caedcbc0a80164017df472f39eaed1\",\"contact_type\":\"\",\"reopened_by\":\"\",\"incident_state\":\"2\",\"urgency\":\"1\",\"problem_id\":\"\",\"company\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/core_company/31bea3d53790200044e0bfc8bcbe5dec\",\"value\":\"31bea3d53790200044e0bfc8bcbe5dec\"},\"reassignment_count\":\"2\",\"activity_due\":\"2018-03-01 21:51:14\",\"assigned_to\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/46d44a23a9fe19810012d100cca80666\",\"value\":\"46d44a23a9fe19810012d100cca80666\"},\"severity\":\"1\",\"comments\":\"\",\"approval\":\"\",\"sla_due\":\"\",\"comments_and_work_notes\":\"\",\"due_date\":\"\",\"sys_mod_count\":\"12\",\"reopen_count\":\"\",\"sys_tags\":\"\",\"escalation\":\"0\",\"upon_approval\":\"\",\"correlation_id\":\"\",\"location\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/cmn_location/105cf7f3c611227501e75e08b14a38ba\",\"value\":\"105cf7f3c611227501e75e08b14a38ba\"},\"category\":\"network\"},{\"parent\":\"\",\"made_sla\":\"false\",\"caused_by\":\"\",\"watch_list\":\"\",\"upon_reject\":\"\",\"sys_updated_on\":\"2018-06-25 09:41:55\",\"child_incidents\":\"\",\"hold_reason\":\"4\",\"approval_history\":\"\",\"number\":\"INC0000002\",\"resolved_by\":\"\",\"sys_updated_by\":\"system\",\"opened_by\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/681ccaf9c0a8016400b98a06818d57c7\",\"value\":\"681ccaf9c0a8016400b98a06818d57c7\"},\"user_input\":\"\",\"sys_created_on\":\"2016-06-19 22:30:06\",\"sys_domain\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/global\",\"value\":\"global\"},\"state\":\"3\",\"sys_created_by\":\"pat\",\"knowledge\":\"false\",\"order\":\"\",\"calendar_stc\":\"\",\"closed_at\":\"\",\"cmdb_ci\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/cmdb_ci/b0c25d1bc0a800090168be1bfcdcd759\",\"value\":\"b0c25d1bc0a800090168be1bfcdcd759\"},\"delivery_plan\":\"\",\"impact\":\"1\",\"active\":\"true\",\"work_notes_list\":\"\",\"business_service\":\"\",\"priority\":\"1\",\"sys_domain_path\":\"/\",\"rfc\":\"\",\"time_worked\":\"\",\"expected_start\":\"\",\"opened_at\":\"2017-11-23 23:07:12\",\"business_duration\":\"\",\"group_list\":\"\",\"work_end\":\"\",\"caller_id\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/5137153cc611227c000bbd1bd8cd2005\",\"value\":\"5137153cc611227c000bbd1bd8cd2005\"},\"reopened_time\":\"\",\"resolved_at\":\"\",\"approval_set\":\"\",\"subcategory\":\"\",\"work_notes\":\"\",\"short_description\":\"Network file shares access issue problem\",\"close_code\":\"\",\"correlation_display\":\"\",\"delivery_task\":\"\",\"work_start\":\"\",\"assignment_group\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user_group/287ebd7da9fe198100f92cc8d1d2154e\",\"value\":\"287ebd7da9fe198100f92cc8d1d2154e\"},\"additional_assignee_list\":\"\",\"business_stc\":\"\",\"description\":\"User can't get to any of his files on the file server.\",\"calendar_duration\":\"\",\"close_notes\":\"\",\"notify\":\"1\",\"sys_class_name\":\"incident\",\"closed_by\":\"\",\"follow_up\":\"\",\"parent_incident\":\"\",\"sys_id\":\"9d385017c611228701d22104cc95c371\",\"contact_type\":\"\",\"reopened_by\":\"\",\"incident_state\":\"3\",\"urgency\":\"1\",\"problem_id\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/problem/9d3a266ac6112287004e37fb2ceb0133\",\"value\":\"9d3a266ac6112287004e37fb2ceb0133\"},\"company\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/core_company/31bea3d53790200044e0bfc8bcbe5dec\",\"value\":\"31bea3d53790200044e0bfc8bcbe5dec\"},\"reassignment_count\":\"1\",\"activity_due\":\"2018-06-25 11:41:50\",\"assigned_to\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/sys_user/46ca0887a9fe19810191e08e51927ebf\",\"value\":\"46ca0887a9fe19810191e08e51927ebf\"},\"severity\":\"1\",\"comments\":\"\",\"approval\":\"\",\"sla_due\":\"\",\"comments_and_work_notes\":\"\",\"due_date\":\"\",\"sys_mod_count\":\"344\",\"reopen_count\":\"\",\"sys_tags\":\"\",\"escalation\":\"0\",\"upon_approval\":\"\",\"correlation_id\":\"\",\"location\":{\"link\":\"https://dev29786.service-now.com/api/now/v2/table/cmn_location/108486c7c611227500b093211aa88dcc\",\"value\":\"108486c7c611227500b093211aa88dcc\"},\"category\":\"network\"}]}";
	}
}
