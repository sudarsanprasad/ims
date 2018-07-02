package com.ims.service;

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
	
	public List<TicketPpm> findByPpmFlag(){
		return ticketPpmRepository.findByPpmFlag("Y");
	}
	
	public void updateNotifications(){
		ticketPpmRepository.updatePpmFlagAsN(DateUtil.getTimeStamp());
	}
	
}
