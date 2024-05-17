package com.lawencon.pss.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class FtpUtil {
	
	@Value("${ftp.username}")
	private String username;
	
	@Value("${ftp.password}")
	private String password;
	
	@Value("${ftp.port}")
	private int port;
	
	@Value("${ftp.server}")
	private String server;

	public void sendFile(String fileBase64, String remoteFile) {
		final FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			System.out.println(username);
			System.out.println(password);
			System.out.println(server);
			System.out.println(port);
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
	
	public byte[] getFile(String remoteFile) {
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            System.out.println("======> Downloading file");
//            final File downloadFile = new File(downloadLocation);
//            final OutputStream outputStream = new BufferedOutputStream(new ByteArray);
            final InputStream success = ftpClient.retrieveFileStream(remoteFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            success.transferTo(output);
            final byte[] byteArray = output.toByteArray();
            return byteArray;
            
//            outputStream.close();
 
//            if (success) {
//                System.out.println("======> Download successfully");
//            }
 
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
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