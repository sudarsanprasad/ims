package com.ims.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	private static final Logger LOG = Logger.getLogger(NotificationService.class);

	@Value("${ppm.file.location}")
	private String folderPath;

	public List<JSONObject> readJSON() {
		List<JSONObject> jsonList = new ArrayList<>();
		File folder = new File(folderPath);
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				JSONParser parser = new JSONParser();
				Object obj;
				try {
					obj = parser.parse(new FileReader(fileEntry.getAbsolutePath()));
					JSONObject jsonObject = (JSONObject) obj;
					jsonList.add(jsonObject);
				} catch (IOException | ParseException e) {
					LOG.info(e);
				}

			}
		}
		return jsonList;
	}
}
