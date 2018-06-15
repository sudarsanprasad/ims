package com.ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.entity.FieldMask;

public interface FieldMaskRepository extends JpaRepository<FieldMask, Long> {

	public List<FieldMask> findByMaskEnabled(String maskEnabled);
}
