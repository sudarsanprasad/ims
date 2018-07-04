package com.ims.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.dto.PpmDto;
import com.ims.entity.TicketPpm;
import com.ims.repository.TicketPpmRepository;
import com.ims.util.DateUtil;

@Service
public class TicketPpmService {
	
	private static final Logger LOG = Logger.getRootLogger();

	@Autowired
	private TicketPpmRepository ticketPpmRepository;

	public PpmDto findByPpmFlag() {
		LocalDate date = LocalDate.now().minusDays(7);
		Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());
		List<TicketPpm> list = ticketPpmRepository.findByPpmFlagAndCreateDateIsAfter("Y", timestamp);
		int size = 0;
		if(!CollectionUtils.isEmpty(list)){
			for(TicketPpm ppm:list){
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(ppm.getData());
					ppm.setPpmData(json);
				} catch (ParseException e) {
					LOG.info(e);
				}
				ppm.setData(null);
			}
			size = list.size();
		}
		PpmDto ppmDto = new PpmDto();
		ppmDto.setNum_incidents(size);
		ppmDto.setResponse(list);
		return ppmDto;
	}

	public void updateNotifications() {
		ticketPpmRepository.updatePpmFlagAsN(DateUtil.getTimeStamp());
	}

}
