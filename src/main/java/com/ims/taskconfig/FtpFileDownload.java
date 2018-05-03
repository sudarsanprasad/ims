package com.ims.taskconfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
 
public class FtpFileDownload{
  
    public static void main(String[] args) {
        String serverAddress = "192.168.241.12"; // ftp server address 
        int port = 21; // ftp uses default port Number 21
        String username = "rbongurala";// username of ftp server
        String password = "April@2018"; // password of ftp server
  
        FTPClient ftpClient = new FTPClient();
        try {
  
            ftpClient.connect(serverAddress, port);
            ftpClient.login(username,password);
 
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE/FTP.ASCII_FILE_TYPE);
            String remoteFilePath = "/data.xls";
            File localfile = new File("C:\\data.xls");
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localfile));
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.close();
  
            if (success) {
                System.out.println("Ftp file successfully download.");
            }
  
        } catch (IOException ex) {
            System.out.println("Error occurs in downloading files from ftp Server : " + ex.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
