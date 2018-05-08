package com.ims.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ims.exception.ImsException;

@Service
public class FTPService {
	
	private static final Logger LOG = Logger.getLogger(FTPService.class);

	@Autowired
    private FtpRemoteFileTemplate template;

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
    	}catch(Exception ex){
    		LOG.error(ex);
			throw new ImsException("Exception occured while processing excep data", ex);
    	}
    	return isFileSavedToLocalFlag;
    }
}
