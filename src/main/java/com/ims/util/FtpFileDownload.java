package com.ims.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

import java.io.FileOutputStream;

public class FtpFileDownload {

	public static void main(String[] args) {

		FTPClient client = new FTPClient();

		FileOutputStream fos = null;

		try {

			client.connect("192.168.241.12");

			client.login("rbongurala", "April@2018");

			// Create an OutputStream for the file

			String filename = "data.xls";

			fos = new FileOutputStream(filename);

			// Fetch file from server

			client.retrieveFile("/" + filename, fos);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (fos != null) {

					fos.close();

				}

				client.disconnect();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

}
