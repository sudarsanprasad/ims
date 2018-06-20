package com.ims.util;

/*
 * Dependencies: Apache POI Library from http://poi.apache.org/
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelToCsvUtil {

    public void echoAsCSV(Sheet sheet, String file) {
        Row row;
        StringBuilder fileContent = new StringBuilder();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            String line = "";
            for (int j = 0; j < row.getLastCellNum(); j++) {
                line +="\"" + row.getCell(j).toString().replaceAll("[\r\n]+", " ") + "\",";
            }
            fileContent.append(line).append("\n");
            line = "";
        }
        write2File(fileContent.toString(), file);
    }
    
    public void write2File(String text,String filePath){
        File file = new File(filePath);
        Writer writer;
        if(!file.exists()){
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public void readExcelFile(String fileName, String file) {
        InputStream inp = null;
        try {
            inp = new FileInputStream(fileName);
            Workbook wb = WorkbookFactory.create(inp);

            for(int i=0;i<wb.getNumberOfSheets();i++) {
                echoAsCSV(wb.getSheetAt(i), file);
            }
        } catch (InvalidFormatException | IOException ex) {
            Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
            	if(inp != null){
            		inp.close();
            	}
            } catch (IOException ex) {
                Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public int getRecordsCount(String fileName) {
        InputStream inp = null;
        int recordsCount = 0;
        try {
            inp = new FileInputStream(fileName);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = null;
            for(int i=0;i<wb.getNumberOfSheets();i++) {
            	sheet = wb.getSheetAt(i);
            }
            if(sheet != null){
            	recordsCount = sheet.getLastRowNum();
            }
        } catch (Exception ex) {
            Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
            	if(inp != null){
            		inp.close();
            	}
            } catch (IOException ex) {
                Logger.getLogger(ExcelToCsvUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return recordsCount;
    }
    
}