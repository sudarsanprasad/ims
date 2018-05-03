package com.ims.taskconfig;

import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTPDownloadDemo {
    public static void main(String[] args) {
        // The local filename and remote filename to be downloaded.
        String filename = "data.xls";

        FTPClient client = new FTPClient();
        try (OutputStream os = new FileOutputStream(filename)) {
            client.connect("192.168.241.12");
            client.login("rbongurala", "April@20189");

            // Download file from FTP server.
            boolean status = client.retrieveFile(filename, os);
            System.out.println("status = " + status);
            System.out.println("reply  = " + client.getReplyString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
