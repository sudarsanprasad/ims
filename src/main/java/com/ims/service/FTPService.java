package com.ims.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ims.constant.StatusType;
import com.ims.entity.TicketStatistics;
import com.ims.exception.ImsException;
import com.ims.repository.TicketMetadataRepository;
import com.ims.repository.TicketStatisticsRepository;
import com.ims.util.DataMaskUtil;
import com.ims.util.QueryBuilder;

@Service
public class FTPService {
	
	private static final Logger LOG = Logger.getLogger(FTPService.class);

	@Autowired
    private FtpRemoteFileTemplate template;
	
	@Autowired
	private Environment env;
	
	@Autowired
	TicketMetadataRepository ticketMetadataRepository;
	
	@Autowired
	TicketStatisticsRepository ticketStatisticsRepository;

    public boolean downloadExcel() throws ImsException {
    	boolean isFileSavedToLocalFlag;
    	FTPFile[] files = template.list("");
    	try{
    		for (FTPFile file : files) {
        	    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        	    String formattedDate = formatDate.format(file.getTimestamp().getTime());
        	    LOG.info(file.getName() + "     " + formattedDate);
        	}
        	isFileSavedToLocalFlag = template.get("data.xls", inputStream -> FileCopyUtils.copy(inputStream,  new FileOutputStream(new File("C:/test/data.xls"))));
        	if(isFileSavedToLocalFlag){
        		TicketStatistics ticketStatistics = ticketStatisticsRepository.save(getTicketStatistics());
        		processExcelData("C:/test/data.xls", ticketStatistics);
        	}
    	}catch(Exception ex){
    		LOG.error(ex);
			throw new ImsException("Exception occured while processing excel data", ex);
    	}
    	return isFileSavedToLocalFlag;
    }
    
    private void processExcelData(String filename, TicketStatistics ticketStatistics){
    	try {
    		QueryBuilder queryBuilder = new QueryBuilder();
    		StringBuilder qBuilder = queryBuilder.buildHiveQuery(ticketMetadataRepository);
            FileInputStream excelFile = new FileInputStream(new File(filename));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            ticketStatistics.setComments("Reading the data from Excel in Progress");
            ticketStatisticsRepository.save(ticketStatistics);
            Connection con = getConnection();
			Statement stmt = con.createStatement();
            updateDataToHDFS(ticketStatistics, queryBuilder, qBuilder, iterator, stmt);
            stmt.close();
			con.close();
            workbook.close();
        } catch (FileNotFoundException e) {
        	LOG.error(e);
        	ticketStatistics.setAutomationStatus(StatusType.ABORTED.getDescription());
        	ticketStatistics.setComments("File Not Found");
            ticketStatisticsRepository.save(ticketStatistics);
        } catch (IOException | SQLException e) {
        	LOG.error(e);
        	ticketStatistics.setAutomationStatus(StatusType.ABORTED.getDescription());
        	ticketStatistics.setComments("Exception occured While Processing the File");
            ticketStatisticsRepository.save(ticketStatistics);
        } catch (ImsException e) {
        	LOG.error(e);
        	ticketStatistics.setAutomationStatus(StatusType.ABORTED.getDescription());
        	ticketStatistics.setComments("Exception occured Connecting to Postgres");
            ticketStatisticsRepository.save(ticketStatistics);
        }
    }

	private boolean updateDataToHDFS(TicketStatistics ticketStatistics, QueryBuilder queryBuilder, StringBuilder qBuilder, Iterator<Row> iterator, Statement stmt) 	throws SQLException {
		boolean skipFirstRow = false;
		while (iterator.hasNext()) {
			if(!skipFirstRow){
				iterator.next();
			}
			StringBuilder query = queryBuilder.getInsertQueryWithValue(qBuilder);
			skipFirstRow = true;
		    Row currentRow = iterator.next();
		    Iterator<Cell> cellIterator = currentRow.iterator();
		    while (cellIterator.hasNext()) {
		        Cell currentCell = cellIterator.next();
		        query.append("\"");
		        appendCellColumn(query, currentCell);
		    }
		    String insertQuery = query.toString().substring(0, query.lastIndexOf(","));
		    StringBuilder insertHiveQuery = new StringBuilder(insertQuery).append(")");
		    LOG.info(insertHiveQuery.toString());
		    stmt.execute(insertHiveQuery.toString());
		    //Add connection details
		    ticketStatistics.setComments("Data inserted into HDFS");
		    ticketStatistics.setAutomationEndDate(new Date());
		    ticketStatistics.setAutomationStatus(StatusType.COMPLETED.getDescription());
		    ticketStatisticsRepository.save(ticketStatistics);
		}
		return skipFirstRow;
	}

	private void appendCellColumn(StringBuilder query, Cell currentCell) {
		if (currentCell.getCellTypeEnum() == CellType.STRING) {
			String cellValue = DataMaskUtil.maskData(currentCell.getStringCellValue());
			query.append(cellValue).append("\"").append(",");
		} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			query.append(currentCell.getNumericCellValue()).append("\"").append(",");
		}
	}
    
    private TicketStatistics getTicketStatistics() {
		TicketStatistics ticketStatistics = new TicketStatistics();
		ticketStatistics.setSystemName((String)env.getProperty("ticketsystem"));
		ticketStatistics.setCustomer((String)env.getProperty("customer"));
		ticketStatistics.setAutomationStatus(StatusType.INPROGRESS.getDescription());
		ticketStatistics.setAutomationStartDate(new Date());
		ticketStatistics.setComments("Excel downloaded successfully");
		return ticketStatistics;
	}
    
    public Connection getConnection() throws ImsException {
		try {
			Class.forName((String)env.getProperty("hive.driver-class-name"));
			return DriverManager.getConnection((String)env.getProperty("hive.url"), (String)env.getProperty("hive.username"), (String)env.getProperty("hive.password"));
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error(e);
			throw new ImsException("",e);
		}
	}
}
