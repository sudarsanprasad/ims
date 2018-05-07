package com.ims.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class HiveDao {
	
	@Autowired
	private Environment env;
	
	public Connection getConnection() throws Exception {
		Class.forName((String)env.getProperty("hive.driver-class-name"));
		return DriverManager.getConnection((String)env.getProperty("hive.url"), (String)env.getProperty("hive.username"), (String)env.getProperty("hive.password"));
	}
	
	public boolean insertTicketData(String query, Statement stmt){
		boolean result = false;
		try {
			stmt.executeQuery(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void deleteRecords(){
		try {
			Statement stmt = getConnection().createStatement();
			stmt.executeQuery("delete from ticket_data");
			getConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
