package com.ims.jobs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class TriggerDescriptorTest {

	@InjectMocks
	TriggerDescriptor triggerDescriptor;
	
	@Test
	public void buildDescriptor() {
		Trigger trigger=mock(Trigger.class);
		//LocalDateTime localDateTime=mock(LocalDateTime.class);
		JobDataMap jobDataMap=mock(JobDataMap.class);
		TriggerKey triggerKey=new TriggerKey("Deloite");
		when(trigger.getKey()).thenReturn(triggerKey);
		when(trigger.getJobDataMap()).thenReturn(jobDataMap);
		when(jobDataMap.get("fireTime")).thenReturn(LocalDateTime.now());
		TriggerDescriptor.buildDescriptor(trigger);
	}
	
	@Test
	public void buildTrigger() {
		ReflectionTestUtils.setField(triggerDescriptor, "fireTime",LocalDateTime.now());
		triggerDescriptor.buildTrigger();
	}
	
	@Test(expected=Exception.class)
	public void buildTriggerInvalidCron() {
		ReflectionTestUtils.setField(triggerDescriptor, "cron","test");
		triggerDescriptor.buildTrigger();
	}
	
	@Test(expected=Exception.class)
	public void buildTriggerCronAsNull() {
		ReflectionTestUtils.setField(triggerDescriptor, "cron",null);
		triggerDescriptor.buildTrigger();
	}
}
