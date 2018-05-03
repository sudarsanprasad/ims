package com.ims.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class FTPService {

	@Autowired
    private FtpRemoteFileTemplate template;

    public boolean downloadExcel() throws Exception {
    	FTPFile[] files = template.list("");
    	for (FTPFile file : files) {
    	    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    	    String formattedDate = formatDate.format(file.getTimestamp().getTime());
    	    System.out.println(file.getName() + "     " + formattedDate);
    	}
        return template.get("data.xls", inputStream -> FileCopyUtils.copy(inputStream,  new FileOutputStream(new File("C:/test/data.xls"))));
    }
}
