package com.lawencon.pss.dto.ftp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FtpReqDto {
	
	private String fileBase64;
	private String remoteLocation;

}
