package com.ims.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.service.HiveService;

@RestController
@RequestMapping(value = "/hive")
public class HiveController {
	private static final Logger logger = LoggerFactory.getLogger(HiveController.class);

	@Autowired
	HiveService hiveService;
	
	@GetMapping(value = "/getData")
    public String getData() throws Exception{
        return hiveService.getData();
    }
	
	@GetMapping(value = "/createTable")
    public String createTable() throws Exception{
        return hiveService.createTable();
    }
	
	@GetMapping(value = "/saveData")
    public String saveRawData() throws Exception{
        return hiveService.saveRawData();
    }
	
}
	
