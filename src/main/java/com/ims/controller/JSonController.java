package com.ims.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.service.JSonService;

@RestController
public class JSonController {

	@Autowired
	JSonService jSonService;

	@GetMapping("/readJson")
	public List<JSONObject> read() {
		return jSonService.readJSON();
	}

}