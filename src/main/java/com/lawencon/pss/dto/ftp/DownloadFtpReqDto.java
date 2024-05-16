package com.lawencon.pss.dto.ftp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DownloadFtpReqDto {

	private String remoteFile;
	private String downloadLocation;
}
