package com.ims.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ims.entity.TicketPpm;
import com.ims.repository.TicketPpmRepository;
import com.ims.util.DateUtil;

@Service
public class TicketPpmService {

	@Autowired
	private TicketPpmRepository ticketPpmRepository;

	public List<TicketPpm> findByPpmFlag() {
		LocalDate date = LocalDate.now().minusDays(7);
		Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());
		return ticketPpmRepository.findByPpmFlagAndCreateDateIsAfter("Y", timestamp);
	}

	public void updateNotifications() {
		ticketPpmRepository.updatePpmFlagAsN(DateUtil.getTimeStamp());
	}

}
