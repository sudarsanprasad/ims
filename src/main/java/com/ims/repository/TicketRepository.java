package com.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, String> {
	
}
