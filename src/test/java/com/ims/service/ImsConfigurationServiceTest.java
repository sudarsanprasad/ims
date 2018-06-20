package com.ims.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.ImsConfiguration;
import com.ims.repository.ImsConfigurationRepository;

@RunWith(MockitoJUnitRunner.class)
public class ImsConfigurationServiceTest {

	@InjectMocks
	ImsConfigurationService imsConfigurationService;

	@Mock
	ImsConfigurationRepository imsConfigurationRepository;

	@Mock
	ImsJobService imsJobService;

	@Test
	public void updateCronValueForecastType() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("forecast.cronvalue")).thenReturn(imsConfiguration);
		when(imsConfigurationRepository.save(imsConfiguration)).thenReturn(imsConfiguration);
		doNothing().when(imsJobService).deleteJob(anyString(), anyString());
		String message = imsConfigurationService.updateCronValue("forecast", "0 * * * *");
		assertEquals("Successfully updated", message);
	}

	@Test
	public void updateCronValueKRType() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		when(imsConfigurationRepository.findByProperty("kr.cronvalue")).thenReturn(imsConfiguration);
		when(imsConfigurationRepository.save(imsConfiguration)).thenReturn(imsConfiguration);
		doNothing().when(imsJobService).deleteJob(anyString(), anyString());
		String message = imsConfigurationService.updateCronValue("kr", "0 * * * *");
		assertEquals("Successfully updated", message);
	}

	@Test
	public void getForecastModelStatus() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("TEST_VALUE");
		when(imsConfigurationRepository.findByProperty("forecast.model.status")).thenReturn(imsConfiguration);
		String message = imsConfigurationService.getForecastModelStatus();
		assertEquals("TEST_VALUE", message);
	}

	@Test
	public void getKrStatus() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("TEST_VALUE");
		when(imsConfigurationRepository.findByProperty("kr.build.status")).thenReturn(imsConfiguration);
		String message = imsConfigurationService.getKrStatus();
		assertEquals("TEST_VALUE", message);
	}

	@Test
	public void updateKrStatus() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("TEST_VALUE");
		when(imsConfigurationRepository.findByProperty("kr.build.status")).thenReturn(imsConfiguration);
		when(imsConfigurationRepository.save(imsConfiguration)).thenReturn(imsConfiguration);
		String message = imsConfigurationService.updateKrStatus("Normal");
		assertEquals("Status Updated Successfully", message);
	}

	@Test
	public void updateForecastStatus() {
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setValue("TEST_VALUE");
		when(imsConfigurationRepository.findByProperty("forecast.model.status")).thenReturn(imsConfiguration);
		when(imsConfigurationRepository.save(imsConfiguration)).thenReturn(imsConfiguration);
		String message = imsConfigurationService.updateForecastStatus("Normal");
		assertEquals("Status Updated Successfully", message);
	}

}
