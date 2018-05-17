package com.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.service.ImsConfigurationService;

@RestController
@RequestMapping("/configuration")
public class ImsConfigurationController {
	
	@Autowired
	ImsConfigurationService imsConfigurationService;
	
	@GetMapping(value = "/turnOnApiAutomation")
	public String turnOnApiAutomation() {
		return imsConfigurationService.turnOnApiAutomation();
	}
	
	@GetMapping(value = "/turnOffApiAutomation")
	public String turnOffApiAutomation() {
		return imsConfigurationService.turnOffApiAutomation();
	}
	
	@GetMapping(value = "/turnOnFtpAutomation")
	public String turnOnFtpAutomation() {
		return imsConfigurationService.turnOnFtpAutomation();
	}
	
	@GetMapping(value = "/turnOffFtpAutomation")
	public String turnOffFtpAutomation() {
		return imsConfigurationService.turnOffFtpAutomation();
	}
	
	@GetMapping(value = "/getApiAutomationStatus")
	public boolean getApiAutomationStatus() {
		return imsConfigurationService.getApiAutomationStatus();
	}
	
	@GetMapping(value = "/getFtpAutomationStatus")
	public boolean getFtpAutomationStatus() {
		return imsConfigurationService.getFtpAutomationStatus();
	}
	
}
