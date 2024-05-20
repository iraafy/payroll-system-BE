package com.lawencon.pss.service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileReqDto;
import com.lawencon.pss.dto.ftp.FtpReqDto;
import com.lawencon.pss.model.File;

public interface FileService {
	InsertResDto addNewFile(FileReqDto newFile);
	InsertResDto addNewFileFtp(FtpReqDto request);
	File getFileById(String id);
	File getFtpFileById(String fileName);
}
