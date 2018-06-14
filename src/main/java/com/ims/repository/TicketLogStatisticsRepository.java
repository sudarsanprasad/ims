package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.TicketLogStatistics;

public interface TicketLogStatisticsRepository  extends JpaRepository<TicketLogStatistics, Long> {

	public List<TicketLogStatistics> findByjobId(Long jobId);
}
