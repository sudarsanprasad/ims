package com.ims.util;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ims.entity.TicketMetadata;
import com.ims.repository.TicketMetadataRepository;

@Service
public class QueryBuilder {
	
	
	public StringBuilder buildHiveQuery(TicketMetadataRepository ticketMetadataRepository){
		StringBuilder queryBuilder = new StringBuilder("insert into ticket_data (");
		buildInsertQueryWithMetadata(queryBuilder, ticketMetadataRepository);
		return queryBuilder;
	}
	
	
	private void buildInsertQueryWithMetadata(StringBuilder queryBuilder, TicketMetadataRepository ticketMetadataRepository) {
		List<TicketMetadata> metadata =  ticketMetadataRepository.findBySystemNameAndCustomer("Service Now", "Deloitte");
		if(!CollectionUtils.isEmpty(metadata)){
			for(TicketMetadata data : metadata){
				queryBuilder.append(data.getMappingColumn()).append(",");
			}
		}
	}
	
	
	public StringBuilder getInsertQueryWithValue(StringBuilder queryBuilder) {
		String tempQueryBuilder = queryBuilder.toString().substring(0, queryBuilder.lastIndexOf(","));
		StringBuilder query = new StringBuilder(tempQueryBuilder);
		query.append(") values (");
		return query;
	}

}
