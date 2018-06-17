package com.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.constant.JobType;
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
	
	@Autowired
	ImsJobService imsJobService;
	
	public String updateCronValue(String type, String cronValue){
		if(JobType.FORECAST.getDescription().equalsIgnoreCase(type)){
			ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("forecast.cronvalue");
			imsConfiguration.setValue(cronValue);
			imsConfigurationRepository.save(imsConfiguration);
			imsJobService.deleteJob(JobType.FORECAST.getDescription(), JobType.FORECAST.getDescription());
		}else if(JobType.KR.getDescription().equalsIgnoreCase(type)){
			ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("kr.cronvalue");
			imsConfiguration.setValue(cronValue);
			imsConfigurationRepository.save(imsConfiguration);
			imsJobService.deleteJob(JobType.KR.getDescription(), JobType.KR.getDescription());
		}
		return "Successfully updated";
	}

	public String getForecastModelStatus() {
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("forecast.model.status");
		return imsConfiguration.getValue();
	}

	public String getKrStatus() {
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("kr.build.status");
		return imsConfiguration.getValue();
	}
	
	public String updateKrStatus(String status) {
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("kr.build.status");
		imsConfiguration.setValue(status);
		imsConfigurationRepository.save(imsConfiguration);
		return "Status Updated Successfully";
	}
	
	public String updateForecastStatus(String status) {
		ImsConfiguration imsConfiguration = imsConfigurationRepository.findByProperty("forecast.model.status");
		imsConfiguration.setValue(status);
		imsConfigurationRepository.save(imsConfiguration);
		return "Status Updated Successfully";
	}
	
}
