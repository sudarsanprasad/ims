package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.FieldConfiguration;

public interface FieldConfigurationRepository extends JpaRepository<FieldConfiguration, Long> {
	
	List<FieldConfiguration> findPropertyBySystemNameOrderById(String systemName);
	
}
