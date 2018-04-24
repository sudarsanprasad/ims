package com.ims.taskconfig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

public class Test {
	
	public static void main(String args[]) {
		 final String ticketURL = "https://dev29786.service-now.com/api/now/v1/table/incident";
		 RestTemplate restTemplate = new RestTemplate();
		 restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "Password@1"));
		 String result = restTemplate.getForObject(ticketURL, String.class);
		 JSONObject jsonObj = new JSONObject(result);
		 JSONArray records = jsonObj.getJSONArray("result");
		 if(records != null && records.length() != 0){
			 for(int i=0; i < records.length(); i++){
				 JSONObject record = records.getJSONObject(i);
				 record.get("");
			 }
		 }
		 System.out.println();
	 }

}
