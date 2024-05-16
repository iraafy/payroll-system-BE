package com.lawencon.pss.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUtil {

	public static void sendFile(String fileBase64, String remoteFile) {
		final String server = "192.168.20.129";
		final int port = 21;
		final String user = "ahmad.aminullah@hotmail.com";
		final String pass = "#userIPhone6s";

		final FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			System.out.println("======> Uploading file");
			final OutputStream outputStream = ftpClient.storeFileStream(remoteFile);
			final byte[] data = Base64.getDecoder().decode(fileBase64);

			outputStream.write(data);
			outputStream.close();

			final boolean isCompleted = ftpClient.completePendingCommand();
			if (isCompleted) {
				System.out.println("======> Upload successfully");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
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
	
	public static void getFile(String remoteFile, String downloadLocation) {
		final String server = "192.168.20.129";
		final int port = 21;
		final String user = "ahmad.aminullah@hotmail.com";
		final String pass = "#userIPhone6s";
 
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            System.out.println("======> Downloading file");
            final File downloadFile = new File(downloadLocation);
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            final boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
            outputStream.close();
 
            if (success) {
                System.out.println("======> Download successfully");
            }
 
        } catch (IOException ex) {
            ex.printStackTrace();
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