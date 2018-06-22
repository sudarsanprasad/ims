package com.ims.jobs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class TriggerDescriptorTest {

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
}
