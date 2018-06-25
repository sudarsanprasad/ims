package com.ims.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

public class ImsFileWriter {
	
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
	
}
