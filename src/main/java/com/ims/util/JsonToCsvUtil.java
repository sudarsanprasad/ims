package com.ims.util;

import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ims.entity.FieldConfiguration;

public class JsonToCsvUtil {

	private static final Logger LOG = Logger.getAnonymousLogger();

	

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
	
	public void write2File(String text, String filePath) {
		ImsFileWriter writer = new ImsFileWriter();
		writer.write2File(text, filePath);
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
