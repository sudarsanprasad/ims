package com.ims.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.entity.ImsConfiguration;
import com.ims.service.ImsConfigurationService;

@RunWith(MockitoJUnitRunner.class)
public class ImsConfigurationControllerTest {

	@InjectMocks
	private ImsConfigurationController imsConfigurationController;

	@Mock
	private ImsConfigurationService imsConfigurationService;

	@Test
	public void testUpdateCronValue() {
		when(imsConfigurationService.updateCronValue("test", "test")).thenReturn("Normal");
		ImsConfiguration imsConfiguration = new ImsConfiguration();
		imsConfiguration.setId(10L);
		imsConfiguration.setProperty("test");
		imsConfiguration.setValue("test");
		String cronValue = imsConfigurationController.updateCronValue(imsConfiguration);
		assertEquals("Normal", cronValue);
	}

	@Test
	public void testGetForecastModelStatus() {
		when(imsConfigurationService.getForecastModelStatus()).thenReturn("Normal");
		String modelStatus = imsConfigurationController.getForecastModelStatus();
		assertEquals("Normal", modelStatus);
	}

	@Test
	public void testGetKrStatus() {
		when(imsConfigurationService.getKrStatus()).thenReturn("Normal");
		String krStatus = imsConfigurationController.getKrStatus();
		assertEquals("Normal", krStatus);
	}

	@Test
	public void testUpdateForecastStatus() {
		when(imsConfigurationService.updateForecastStatus("Normal")).thenReturn("Normal");
		String foreCastStatus = imsConfigurationController.updateForecastStatus("Normal");
		assertEquals("Normal", foreCastStatus);
	}

	@Test
	public void testUpdateKrStatus() {
		when(imsConfigurationService.updateKrStatus("Normal")).thenReturn("Normal");
		String krStatus = imsConfigurationController.updateKrStatus("Normal");
		assertEquals("Normal", krStatus);

	}
}
