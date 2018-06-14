package com.ims.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.ims.dto.AffectedServiceDto;
import com.ims.dto.IncidentDto;
import com.ims.dto.ResponseDto;
import com.ims.dto.ResultDto;
import com.ims.dto.TopicDto;

/**
 * 
 * @author RKB
 *
 */
@Service
public class ElasticSearchService {
	
	private static final Logger LOG = Logger.getLogger(ElasticSearchService.class);
	public ResponseDto elasticsearch(String searchtext) {
		
		String url = "http://192.168.204.13:9200/ims/_search?size=500&q=title:"+searchtext;
		RestTemplate restTemplate = new RestTemplate();
		JSONObject requestObject = new JSONObject();
	    JSONObject requestObject2 = new JSONObject();
	    JSONObject requestObject3 = new JSONObject();
	    requestObject3.put("field", "solution");
	    requestObject2.put("exists", requestObject3);
	    requestObject.put("query", requestObject2);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(requestObject.toString(), headers);
	    String result = restTemplate.postForObject( url, entity, String.class );
		
		List<IncidentDto> incidents = null;
		JSONObject resultObject = null;
	    Set<String> affServices = new HashSet<>();
	    double maxScore = 0.0;
	    ResponseDto responseDto = new ResponseDto();
	    ResultDto resultDto = new ResultDto();
	    List<TopicDto> topicDtos = new ArrayList<>();
		TopicDto topicDto = new TopicDto();
		List<AffectedServiceDto> list = new ArrayList<>();
		
	
	    if(result != null){
	    	resultObject = new JSONObject(result);
	    	JSONObject hitsObject = (JSONObject) resultObject.get("hits");
	    	maxScore = hitsObject.getDouble("max_score");
	    	LOG.info("Max Score --- "+maxScore);
	    	JSONArray hits = (JSONArray) hitsObject.get("hits");
	    	if(hits != null && hits.length() > 0){
	    		for(int i=0; i < hits.length(); i++){
	    			JSONObject hit = (JSONObject)hits.getJSONObject(i); 
	    			JSONObject source = (JSONObject) hit.get("_source");
	    			affServices.add(source.getString("affective_service_captured"));
	    			LOG.info("AFF Service == "+source.getString("affective_service_captured"));
	    		}
	    		int numberOfIncidents = 0;
	    		
	    		for(String service:affServices){
	    			incidents = new ArrayList<>();
	    			numberOfIncidents = 0;
	    			AffectedServiceDto affectedServiceDto = new AffectedServiceDto();
	    			LOG.info("AFF SERV === "+service);
	    			for(int i=0; i < hits.length(); i++){
		    			JSONObject hit = (JSONObject)hits.getJSONObject(i); 
		    			int score = hit.getInt("_score");
		    			JSONObject source = (JSONObject) hit.get("_source");
		    			if(service.equals(source.get("affective_service_captured"))){
		    				LOG.info("Solution == "+source.get("solution"));
		    				if(!org.json.JSONObject.NULL.equals(source.get("solution"))){
		    					IncidentDto dto = new IncidentDto();
			    				numberOfIncidents++;
			    				dto.setAffective_service_captured(service);
			    				dto.setId(source.getString("id"));
			    				dto.setSimilarity(score/maxScore);
			    				dto.setSolution((String)source.get("solution"));
			    				dto.setTitle(source.getString("title"));
			    				incidents.add(dto);
		    				}
		    			}
		    		}
	    			affectedServiceDto.setIncidents(incidents);
	    			affectedServiceDto.setNum_incidents(numberOfIncidents);
	    			list.add(affectedServiceDto);
	    		}
	    		
	    	}
	    	
	    	topicDto.setTopic(29);
	    	topicDto.setAffective_service_captured(list);
	    	topicDtos.add(topicDto);
	    	resultDto.setTopics(topicDtos);
	    	responseDto.setResponse(resultDto);
	    	LOG.info(hits);
	    }
		return responseDto;
	}
}
