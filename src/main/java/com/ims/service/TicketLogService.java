package com.ims.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ims.entity.TicketLogStatistics;
import com.ims.repository.TicketLogStatisticsRepository;

@Service
public class TicketLogService {
	
	private static final Logger LOG = Logger.getLogger(TicketLogService.class);

	@Autowired
	private TicketLogStatisticsRepository ticketLogStatisticsRepository;

	public ResponseEntity<Object> downloadLogStatistics(Long jobId) throws IOException {
		FileWriter filewriter = null;

		try {
			List<TicketLogStatistics> ticketLogStatisticsList = ticketLogStatisticsRepository.findByjobId(jobId);

			StringBuilder filecontent = new StringBuilder("TicketId : Message\n");
			for (TicketLogStatistics ticketLogStatistics : ticketLogStatisticsList) {
				filecontent.append(ticketLogStatistics.getTicketId())
						.append(" :").append(ticketLogStatistics.getMessage()).append("\n");
			}

			String filename = "ticketlog.log";

			filewriter = new FileWriter(filename);
			filewriter.write(filecontent.toString());
			filewriter.flush();

			File file = new File(filename);

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
		} catch (Exception e) {
			LOG.info(e);
			return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (filewriter != null)
				filewriter.close();
		}
	}

}
