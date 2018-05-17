package com.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.entity.ImsConfiguration;
import com.ims.repository.ImsConfigurationRepository;

/**
 * 
 * @author RKB
 *
 */
@Service
public class ImsConfigurationService {
	
	@Autowired
	ImsConfigurationRepository imsConfigurationRepository;
	
	
	
	public String turnOnApiAutomation() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("apischedulerflag");
		configuration.setValue("Y");
		imsConfigurationRepository.save(configuration);
		return "API Automation Turned On";
	}
	
	public String turnOffApiAutomation() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("apischedulerflag");
		configuration.setValue("N");
		imsConfigurationRepository.save(configuration);
		return "API Automation Turned Off";
	}
	
	public String turnOnFtpAutomation() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("ftpschedulerflag");
		configuration.setValue("Y");
		imsConfigurationRepository.save(configuration);
		return "FTP Automation Turned On";
	}
	
	public String turnOffFtpAutomation() {
		ImsConfiguration configuration = imsConfigurationRepository.findByProperty("ftpschedulerflag");
		configuration.setValue("N");
		imsConfigurationRepository.save(configuration);
		return "FTP Automation Turned Off";
	}

	public boolean isFtpAutomationOn() {
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("ftpschedulerflag").getValue())){
			return true;
		}
		return false;
	}
	
	public boolean getApiAutomationStatus(){
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("apischedulerflag").getValue())){
			return true;
		}
		return false;
	}
	
	public boolean getFtpAutomationStatus(){
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("ftpschedulerflag").getValue())){
			return true;
		}
		return false;
	}
	
}
