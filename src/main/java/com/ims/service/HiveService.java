package com.ims.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HiveService {
	
	private static final Logger LOG = Logger.getLogger(HiveService.class);
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	public String createTable() {
		String result = null;
		 try {
			 
			StringBuffer sql = new StringBuffer("create table IF NOT EXISTS ");
			sql.append("HIVE_TEST");
			sql.append("(KEY INT, VALUE STRING)");
			/*sql.append("PARTITIONED BY (CTIME DATE)");
			sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' ");
			sql.append("STORED AS TEXTFILE");*/

			LOG.info(sql.toString());

			Class.forName(driverName);
			Connection con = DriverManager.getConnection("jdbc:hive2://192.168.204.13:10000/default", "hive", "hive");
			Statement stmt = con.createStatement();
			LOG.info("SQL ====== "+sql.toString());
			stmt.execute(sql.toString());
			String query = "SELECT VALUE FROM HIVE_TEST";
			ResultSet res = stmt.executeQuery(query);
			if (res.next()) {
				result  = res.getString(1);
			}
			
		    } catch (Exception e) {
		    	LOG.error(e);
		    }
		return result;
	}
	
	public String saveRawData() {
		String result = null;
		 try {
			 
			StringBuffer sql = new StringBuffer("INSERT INTO TABLE  ");
			sql.append("HIVE_TEST VALUES ");
			sql.append("(1, 'AAAA')");

			LOG.info(sql.toString());

			Class.forName(driverName);
			Connection con = DriverManager.getConnection("jdbc:hive2://192.168.204.13:10000/default", "hive", "hive");
			Statement stmt = con.createStatement();
			LOG.info("SQL ====== "+sql.toString());
			stmt.execute(sql.toString());
			String query = "SELECT VALUE FROM HIVE_TEST";
			ResultSet res = stmt.executeQuery(query);
			if (res.next()) {
				result  = res.getString(1);
				LOG.info(result);
			}
			
		    } catch (Exception e) {
		    	LOG.error(e);
		    }
		return result;
	}
	
	public String getData() throws Exception{

	      try {
	      Class.forName(driverName);
	    } catch (ClassNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      System.exit(1);
	    }
	    //replace "hive" here with the name of the user the queries should run as
	      Connection con = DriverManager.getConnection("jdbc:hive2://192.168.204.13:10000/default", "hive", "hive");
	    Statement stmt = con.createStatement();
	    String tableName = "testHiveDriverTable";
	    stmt.execute("drop table if exists " + tableName);
	    stmt.execute("create table " + tableName + " (key int, value string)");
	    // show tables
	    // String sql = "show tables '" + tableName + "'";
	    String sql = ("SELECT VALUE FROM HIVE_TEST");
	    ResultSet res = stmt.executeQuery(sql);
	    String response = null;
	    if (res.next()) {
	    	response = res.getString(1);
	        System.out.println(response);
	      }
	  return response;
	}

}
