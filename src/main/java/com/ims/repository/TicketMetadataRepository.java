package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ims.entity.TicketMetadata;

public interface TicketMetadataRepository extends JpaRepository<TicketMetadata, Long> {
	
	List<TicketMetadata> findBySystemNameAndCustomer(String systemName, String customer);
	
	List<TicketMetadata> findBySystemName(String systemName);
	
	List<TicketMetadata> findByCustomer(String customer);
	
	@Query("SELECT m.mappingColumn  FROM TicketMetadata m where isForecast = 'Y' AND systemName = :systemName")
	List<String> findMappingColumnByIsForecast(@Param("systemName") String systemName);
	
	@Query("SELECT m.mappingColumn  FROM TicketMetadata m where isProactive = 'Y' AND systemName = :systemName")
	List<String> findMappingColumnByIsProactive(@Param("systemName") String systemName);
	
}
