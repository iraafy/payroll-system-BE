package com.lawencon.pss.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
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
	
    private void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
	
	public void makeDir(String dirName) {
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
			
            // Creates a directory
            String dirToCreate = "/upload123";
            final var success = ftpClient.makeDirectory(dirName);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created directory: " + dirName);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
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
            final InputStream success = ftpClient.retrieveFileStream(remoteFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            success.transferTo(output);
            final byte[] byteArray = output.toByteArray();
            return byteArray;
 
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