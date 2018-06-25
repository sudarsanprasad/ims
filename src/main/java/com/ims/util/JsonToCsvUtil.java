package com.ims.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ims.entity.FieldConfiguration;

public class JsonToCsvUtil {

	private static final Logger LOG = Logger.getAnonymousLogger();

	public void write2File(String text, String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				boolean created = file.createNewFile();
				if (created) {
					LOG.info("File is created");
				}
			} catch (IOException e) {
				LOG.info("Error === >> " + e);
			}
		}
		try(FileWriter fw = new FileWriter(file)) {
			LOG.info("Saving csv file ==== >>" + file);
			Writer writer = new BufferedWriter(fw);
			writer.write(text);
			writer.close();
		} catch (IOException e) {
			LOG.info("Error == >> " + e);
		}

	}

	public void prepareCsv(JSONObject jsonObj, List<FieldConfiguration> fields, String fileName, String ppmFileName) {
		StringBuilder fileContent = new StringBuilder("");
		StringBuilder fileContentStatusNew = new StringBuilder("");
		JSONArray records = jsonObj.getJSONArray("result");
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			StringBuilder line = new StringBuilder("");
			StringBuilder lineStatusNew = new StringBuilder("");
			getLine(fields, record, line, lineStatusNew);
			fileContent.append(line.toString()).append("\n");
			fileContentStatusNew.append(lineStatusNew.toString()).append("\n");
		}

		write2File(fileContent.toString(), fileName);
		write2File(fileContentStatusNew.toString(), ppmFileName);
	}

	private void getLine(List<FieldConfiguration> fields, JSONObject record,
			StringBuilder line, StringBuilder lineStatusNew) {
		String tempField;
		for (FieldConfiguration field : fields) {
			LOG.info("Field   === >> " + field.getProperty());
			if ("assignment_group".equals(field.getProperty())
					|| "cmdb_ci".equals(field.getProperty())) {
				tempField = "";
			} else {
				tempField = ((String) record.get(field.getProperty())).replace(
						"\"", "\\\"");
			}
			line.append(tempField).append(",");
			lineStatusNew.append(tempField).append(",");
		}
	}

}
