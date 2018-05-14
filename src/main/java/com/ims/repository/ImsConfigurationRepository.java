package com.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.ImsConfiguration;

public interface ImsConfigurationRepository extends JpaRepository<ImsConfiguration, String> {
	
}
