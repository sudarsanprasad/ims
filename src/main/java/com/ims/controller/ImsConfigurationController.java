package com.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.entity.ImsConfiguration;
import com.ims.service.ImsConfigurationService;

@RestController
@RequestMapping("/configuration")
public class ImsConfigurationController {

	@Autowired
	ImsConfigurationService imsConfigurationService;

	@PostMapping(value = "/updateCron")
	public ImsConfiguration updateCronValue(@RequestBody ImsConfiguration imsConfiguration) {
		return imsConfigurationService.updateCronValue(imsConfiguration.getProperty(), imsConfiguration.getValue(),
				imsConfiguration.getFrequencyType(), imsConfiguration.getFrequencyValue());
	}

	@GetMapping(value = "/getForecastStatus")
	public String getForecastModelStatus() {
		return imsConfigurationService.getForecastModelStatus();
	}

	@GetMapping(value = "/getKrStatus")
	public String getKrStatus() {
		return imsConfigurationService.getKrStatus();
	}

	@GetMapping(value = "/updateForecastStatus/{status}")
	public String updateForecastStatus(@PathVariable String status) {
		return imsConfigurationService.updateForecastStatus(status);
	}

	@GetMapping(value = "/updateKrStatus/{status}")
	public String updateKrStatus(@PathVariable String status) {
		return imsConfigurationService.updateKrStatus(status);
	}

	@GetMapping(value = "/getAllCron")
	public List<ImsConfiguration> findAll() {
		return imsConfigurationService.findAll();
	}
}
