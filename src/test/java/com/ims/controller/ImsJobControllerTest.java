package com.ims.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Trigger.TriggerState;

import com.ims.jobs.JobDescriptor;
import com.ims.service.ImsJobService;

@RunWith(MockitoJUnitRunner.class)
public class ImsJobControllerTest {

	@InjectMocks
	private ImsJobController imsJobController;

	@Mock
	private ImsJobService imsJobService;

	JobDescriptor descriptor = new JobDescriptor();

	private String group = "TEST_JOB_GROUP";

	private String name = "TEST_NAME";


	@Test
	public void updateJob() {
		doNothing().when(imsJobService).updateJob(group, name);
		imsJobController.updateJob(group, name);
	}

	@Test
	public void deleteJob() {
		doNothing().when(imsJobService).deleteJob(group, name);
		imsJobController.deleteJob(group, name);
	}

	@Test
	public void pauseJob() {
		doNothing().when(imsJobService).pauseJob(group, name);
		imsJobController.pauseJob(group, name);
	}

	@Test
	public void resumeJob() {
		doNothing().when(imsJobService).resumeJob(group, name);
		imsJobController.resumeJob(group, name);
	}

	@Test
	public void triggerForecastModelScheduler() {
		doNothing().when(imsJobService).triggerForecastModelScheduler("Deloite");
		imsJobController.triggerForecastModelScheduler("Deloite");
	}

	@Test
	public void statusJob() {
		when(imsJobService.statusJob(group, name)).thenReturn(TriggerState.NORMAL);
		imsJobController.statusJob(group, name);

	}

}
