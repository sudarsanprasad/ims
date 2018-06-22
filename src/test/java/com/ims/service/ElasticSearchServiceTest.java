package com.ims.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchServiceTest {

	//@Autowired
	private ElasticSearchService elasticSearchService;

	@Test
	public void elasticsearch() {
		try {
			//elasticSearchService.elasticsearch("searchtext");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
