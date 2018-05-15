package com.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public boolean isFtpAutomationOn(){
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("ftpschedulerflag"))){
			return true;
		}
		return false;
	}
	
	public boolean isApiAutomationOn(){
		if("Y".equalsIgnoreCase(imsConfigurationRepository.findByProperty("apischedulerflag"))){
			return true;
		}
		return false;
	}
	
}
