package com.ims.controller;

import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ims.dto.ResponseDto;
import com.ims.service.ElasticSearchService;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchControllerTest {

	@InjectMocks
	private ElasticSearchController elasticSearchController;

	@Mock
	private ElasticSearchService elasticSearchService;

	@Test
	public void elasticsearch() {
		when(elasticSearchService.elasticsearch("searchtext")).thenReturn(new ResponseDto());
		notNull(elasticSearchController.elasticsearch("searchtext"));
	}
}
